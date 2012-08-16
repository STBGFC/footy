package org.davisononline.footy.core.utils

import grails.test.GrailsUnitTestCase
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem

class DateTimeUtilsTests extends GrailsUnitTestCase {

    void testMidnight() {
        Date d = DateTimeUtils.setMidnight(new Date())
        assert d.hours == 0
        assert d.minutes == 0
        assert d.seconds == 0
    }


    void testSeasonStartEnd() {
        Date correctStart = new Date('2012/08/01 00:00:00')
        Date correctEnd = new Date('2013/07/31 00:00:00')
        Date d = new Date('2012/10/1 12:13:14')
        assert DateTimeUtils.getCurrentSeasonStart(d) == correctStart
        assert DateTimeUtils.getCurrentSeasonEnd(d) == correctEnd
        d = new Date('2012/8/1 12:13:14')
        assert DateTimeUtils.getCurrentSeasonStart(d) == correctStart
        assert DateTimeUtils.getCurrentSeasonEnd(d) == correctEnd

        d = new Date('2013/7/31 12:13:14')
        assert DateTimeUtils.getCurrentSeasonStart(d) == correctStart
        assert DateTimeUtils.getCurrentSeasonEnd(d) == correctEnd
        d = new Date('2013/3/31 12:13:14')
        assert DateTimeUtils.getCurrentSeasonStart(d) == correctStart
        assert DateTimeUtils.getCurrentSeasonEnd(d) == correctEnd
    }

}
