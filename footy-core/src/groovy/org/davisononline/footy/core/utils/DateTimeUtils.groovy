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
        getMidnightForDate(theDate).getTime()
    }

    private static Calendar getMidnightForDate(Date theDate) {
        Calendar cal = Calendar.getInstance()
        cal.setTime(theDate)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal
    }

    public static Date getCurrentSeasonStart() {
        getCurrentSeasonStart(new Date())
    }

    public static Date getCurrentSeasonEnd() {
        getCurrentSeasonEnd(new Date())
    }

    /**
     * h/coded to assume 1/8 is season start to account for friendlies
     */
    public static Date getCurrentSeasonStart(Date current) {
        Calendar cal = getMidnightForDate(current)
        cal.set(Calendar.MONTH, Calendar.AUGUST)
        cal.set(Calendar.DAY_OF_MONTH, 1)

        // if prior to 1/8..
        if (current.month < 7) cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1)
        cal.time
    }


    /**
     * h/coded to assume 31/7 is season end to account for tourneys
     */
    public static Date getCurrentSeasonEnd(Date current) {
        Calendar cal = getMidnightForDate(current)
        cal.set(Calendar.MONTH, Calendar.JULY)
        cal.set(Calendar.DAY_OF_MONTH, 31)

        // if later than 1/8..
        if (current.month > 6) cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1)
        cal.time
    }

}
