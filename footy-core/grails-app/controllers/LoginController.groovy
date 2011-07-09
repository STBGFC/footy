import grails.converters.JSON

import javax.servlet.http.HttpServletResponse

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.Person
import org.davisononline.footy.core.utils.TemplateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.davisononline.footy.core.Club


class LoginController {
    
    private static final String PASSWORD_REGEX = '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$'


	/**
	 * Dependency injection for the authenticationTrustResolver.
	 */
	def authenticationTrustResolver

	/**
	 * Dependency injection for the springSecurityService.
	 */
	def springSecurityService

    def passwordEncoder

    def mailService


	/**
	 * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
	 */
	def index = {
		if (springSecurityService.isLoggedIn()) {
			redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
		}
		else {
			redirect action: auth, params: params
		}
	}

	/**
	 * Show the login page.
	 */
	def auth = {

		def config = SpringSecurityUtils.securityConfig

		if (springSecurityService.isLoggedIn()) {
			redirect uri: config.successHandler.defaultTargetUrl
			return
		}

		String view = 'auth'
		String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
		render view: view, model: [postUrl: postUrl,
		                           rememberMeParameter: config.rememberMe.parameter]
	}

	/**
	 * The redirect action for Ajax requests. 
	 */
	def authAjax = {
		response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
		response.sendError HttpServletResponse.SC_UNAUTHORIZED
	}

	/**
	 * Show denied page.
	 */
	def denied = {
		if (springSecurityService.isLoggedIn() &&
				authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
			// have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
			redirect action: full, params: params
		}
	}

	/**
	 * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
	 */
	def full = {
		def config = SpringSecurityUtils.securityConfig
		render view: 'auth', params: params,
			model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
			        postUrl: "${request.contextPath}${config.apf.filterProcessesUrl}"]
	}

	/**
	 * Callback after a failed login. Redirects to the auth page with a warning message.
	 */
	def authfail = {

		def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		String msg = ''
		def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
		if (exception) {
			if (exception instanceof AccountExpiredException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.expired
			}
			else if (exception instanceof CredentialsExpiredException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.passwordExpired
                if (!springSecurityService.isAjax(request))
                    redirect (action:'changePassword')
			}
			else if (exception instanceof DisabledException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.disabled
			}
			else if (exception instanceof LockedException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.locked
			}
			else {
				msg = SpringSecurityUtils.securityConfig.errors.login.fail
			}
		}

		if (springSecurityService.isAjax(request)) {
			render([error: msg] as JSON)
		}
		else {
			flash.message = msg
			redirect controller: 'login', action:'auth', params: params
		}
	}

	/**
	 * The Ajax success redirect url.
	 */
	def ajaxSuccess = {
		render([success: true, username: springSecurityService.authentication.name] as JSON)
	}

	/**
	 * The Ajax denied redirect url.
	 */
	def ajaxDenied = {
		render([error: 'access denied'] as JSON)
	}

    /**
     * render the change pwd form
     */
    def changePassword = {
        [username: session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY] ?: springSecurityService.authentication.name]
    }

    /**
     * called from the change pwd form: chnages the user's pwd if all checks pass
     */
    def updatePassword = {
        String username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY] ?: springSecurityService.authentication.name
        if (!username) {
            flash.message = 'Sorry, an error has occurred'
            redirect controller: 'login', action:'auth'
            return
        }
        String password = params.password
        String newPassword = params.password_new
        String newPassword2 = params.password_new_2
        if (!password || !newPassword || !newPassword2 || newPassword != newPassword2) {
            flash.message = 'Please enter your current password and a valid new password'
            render view: 'changePassword', model: [username: username]
            return
        }
        SecUser user = SecUser.findByUsername(username)
        if (!passwordEncoder.isPasswordValid(user.password, password, null /*salt*/)) {
            flash.message = 'Current password is incorrect'
            render view: 'changePassword', model: [username: username]
            return
        }
        if (passwordEncoder.isPasswordValid(user.password, newPassword, null /*salt*/)) {
            flash.message = 'Please choose a different password from your current one'
            render view: 'changePassword', model: [username: username]
            return
        }
        if (newPassword.length() < 8 || !newPassword.matches(PASSWORD_REGEX)) {
            flash.message = 'Password does not meet minimum requirements'
            render view: 'changePassword', model: [username: username]
            return            
        }
        
        user.password = springSecurityService.encodePassword(newPassword)
        user.passwordExpired = false
        user.save() 

        flash.message = 'Password changed successfully, you can now login'
        redirect controller: 'login', action: 'auth'
    }

    /**
     * called from the reset dialog.. look up user and send email with reset link
     */
    def resetPassword = {
        if (request.method == "GET")
            render (template: 'resetPassword', contentType: 'text/plain', plugin: 'footy-core')
        else {
            def user = SecUser.findByUsername(params.username)
            def person = user ? Person.findByUser(user) : null
            if (!person) {
                flash.message = "No such user found"
                redirect action: 'auth'
            }
            else {
                // send mail with token saved on SecUser object
                user.resetToken = UUID.randomUUID().toString()
                user.resetTokenDate = new Date()
                user.save(flush:true)

                try {
                    mailService.sendMail {
                        // ensure mail address override is set in dev/test in Config.groovy
                        to      user.username
                        subject "Password Reset"
                        body    TemplateUtils.eval(
                                    ConfigurationHolder.config?.org?.davisononline?.footy?.core?.resetPassword?.emailbody,
                                    [link: user.resetToken, person: person, club: Club.homeClub]
                                )
                    }

                    flash.message = "An email has been sent to the registered address for this account."

                }
                catch (Exception ex) {
                    flash.message = "Error occurred attempting to send your email.  Contact site admin."
                    log.warn "Unable to send email for password reset attempt ($user.username); $ex"
                }

                redirect uri: '/'
            }
        }
    }

    /**
     * user clicks the reset link in the email containing the token and gets to here.
     * If the token isn't found, user must get a 404
     */
    def reset = {
        SecUser user = SecUser.findByResetToken(params.token)
        if (!user) {
            response.sendError 404
            return
        }

        def person = Person.findByUser(user)
        if (!person) {
            response.sendError 404
            return
        }
        
        if (new Date() > (user.resetTokenDate + 1)) {
            render text: 'This token expired.  Please request a new reset'
            return
        }

        // ok to continue
        def pwd = UUID.randomUUID().toString()[0..7]
        user.password = springSecurityService.encodePassword(pwd)
        user.passwordExpired = true
        user.resetTokenDate = null
        user.resetToken = null
        user.save(flush:true)

        try {
            mailService.sendMail {
                // ensure mail address override is set in dev/test in Config.groovy
                to      user.username
                subject "Password Reset Complete"
                body    TemplateUtils.eval(
                            ConfigurationHolder.config?.org?.davisononline?.footy?.core?.resetComplete?.emailbody,
                            [pwd:pwd, person: person, club: Club.homeClub]
                        )
            }

            flash.message = "An email has been sent to the registered address for this account."

        }
        catch (Exception ex) {
            render text: 'Error occurred attempting to send your email.  Contact site admin.'
            log.warn "Unable to send email for password reset attempt ($user.username); $ex"

        }

        redirect uri: '/'

    }

}
