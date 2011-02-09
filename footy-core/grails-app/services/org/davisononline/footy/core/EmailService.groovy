package org.davisononline.footy.core

import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

/**
* Simple service for sending emails.
*
* Work is planned in the Grails roadmap to implement first-class email
* support, so there's no point in making this code any more sophisticated
*
* <your_app>/grails-app/conf/spring/resources.groovy should contain 
* something like:
*
*  beans = {
*      mailSender(org.springframework.mail.javamail.JavaMailSenderImpl) {
*          host = 'mail.davisononline.org'
*      }
*
*      mailMessage(org.springframework.mail.SimpleMailMessage) { 
*          from = 'SomeClub Tournament <tournament@club.co.uk>' 
*      }
*  }
*
*/
class EmailService {
    boolean transactional = false
    MailSender mailSender
    SimpleMailMessage mailMessage // a "prototype" email instance

    /**
     * send a list of emails
     * 
     * @param mails a list of mails
     */
    def sendEmails(mails) {
        // Build the mail messages
        def messages = []
        try {
            for (mail in mails) {
                // Create a thread safe "sandbox" of the message
                SimpleMailMessage message = new SimpleMailMessage(mailMessage)
                message.to = mail.to
                message.text = mail.text
                message.subject = mail.subject
                messages << message
            }
            // Send them all together
            log.debug "about to send ${messages.size()} messages to: ${messages.to.join('\n')}"
            mailSender.send(messages as SimpleMailMessage[])

        } catch (MailException ex) {
            log.error "Failed to send emails: $ex"
        }
    }
}
