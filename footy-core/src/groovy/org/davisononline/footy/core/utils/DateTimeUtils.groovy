package org.davisononline.footy.core.utils

/**
 * utilities for some "still difficult to do" date stuff (even in Groovy)
 */
class DateTimeUtils {

    public static Date getToday() {
        return setMidnight(new Date())
    }

    public static Date getTomorrow() {
        return (getToday() + 1) as Date
    }

    public static Date setMidnight(Date theDate) {
        Calendar cal = Calendar.getInstance()
        cal.setTime(theDate)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.getTime()
    }

}
