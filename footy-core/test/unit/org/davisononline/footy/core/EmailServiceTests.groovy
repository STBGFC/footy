package org.davisononline.footy.core

import org.davisononline.footy.core.IdentityService
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage;

import grails.test.*

class EmailServiceTests extends GrailsUnitTestCase {

    def emailService
    
    protected void setUp() {
        super.setUp()
		emailService = new EmailService()
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    public void testSendOk() {
//        def mail = new SimpleMailMessage(to:'fred@bloggs.com', subject:'foo', text: 'foobar')
//        def mailSenderControl = mockFor(MailSender)
//        def mails = [mail]
//        mailSenderControl.demand.send(mails as SimpleMailMessage[])
//        emailService.mailSender = mailSenderControl.createMock()
//        emailService.sendEmails(mails)
//        mailSenderControl.verify()
    }
}
