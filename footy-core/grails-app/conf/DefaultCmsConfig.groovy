grails.plugins.springsecurity.controllerAnnotations.staticRules = [
    "/wcmEditor/**":         ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmPortal/**":         ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmRepository/**":     ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmSpace/**":          ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmSychronization/**": ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmVersion/**":        ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"]
]

// weceem settings
weceem.security.policy.path='/home/darren/projects/stbgfc/weceem.policy'
weceem.content.prefix = 'content'
weceem.tools.prefix = 'wcm-tools'
weceem.admin.prefix = 'wcm-admin'
weceem.upload.dir = 'file:///tmp/weceem'
weceem.logout.url = [controller:'logout']
weceem.admin.layout='wcmadmin'
weceem.springsecurity.details.mapper = { ->
    [ // Stuff required by weceem spring sec
        username: username,
        password: password,
        enabled: enabled,
        authorities: authorities,
        id: id
    ]
}

