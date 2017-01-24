 /* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteoinfo.global.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yaqiang
 */
public class DateUtil {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Add days to a date
     *
     * @param sDate Start date
     * @param days Days
     * @return Added date
     */
    public static Date addDays(Date sDate, float days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sDate);
        int intDays = (int) days;
        cal.add(Calendar.DAY_OF_YEAR, intDays);
        int hours = (int) ((days - intDays) * 24);
        cal.add(Calendar.HOUR, hours);

        return cal.getTime();
    }

    /**
     * Get days of a month
     *
     * @param year The year
     * @param month The month
     * @return The days in the month
     */
    public static int getDaysInMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get time values - Time delta values of base date
     *
     * @param times Time list
     * @param baseDate Base date
     * @param tDelta Time delta type - days/hours/...
     * @return The time delta values
     */
    public static List<Integer> getTimeDeltaValues(List<Date> times, Date baseDate, String tDelta) {
        List<Integer> values = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        long sl = cal.getTimeInMillis();
        long el, delta;
        int value;
        for (int i = 0; i < times.size(); i++) {
            cal.setTime(times.get(i));
            el = cal.getTimeInMillis();
            delta = el - sl;
            if (tDelta.equalsIgnoreCase("hours")) {
                value = (int) (delta / (60 * 60 * 1000));
                values.add(value);
            } else if (tDelta.equalsIgnoreCase("days")) {
                value = (int) (delta / (24 * 60 * 60 * 1000));
                values.add(value);
            }
        }

        return values;
    }

    /**
     * Get time value - Time delta value of base date
     *
     * @param t The time
     * @param baseDate Base date
     * @param tDelta Time delta type - days/hours/...
     * @return The time delta value
     */
    public static int getTimeDeltaValue(Date t, Date baseDate, String tDelta) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        long sl = cal.getTimeInMillis();
        long el, delta;
        int value = 0;
        cal.setTime(t);
        el = cal.getTimeInMillis();
        delta = el - sl;
        if (tDelta.equalsIgnoreCase("hours")) {
            value = (int) (delta / (60 * 60 * 1000));
        } else if (tDelta.equalsIgnoreCase("days")) {
            value = (int) (delta / (24 * 60 * 60 * 1000));
        }

        return value;
    }
    
    /**
     * Get days difference between two dates
     *
     * @param t The time
     * @param baseDate Base date
     * @return The time delta value
     */
    public static int getDays(Date t, Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        long sl = cal.getTimeInMillis();
        long el, delta;
        cal.setTime(t);
        el = cal.getTimeInMillis();
        delta = el - sl;
        int value = (int) (delta / (24 * 60 * 60 * 1000));

        return value;
    }
    
    /**
     * Get hours difference between two dates
     *
     * @param t The time
     * @param baseDate Base date
     * @return The time delta value
     */
    public static int getHours(Date t, Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        long sl = cal.getTimeInMillis();
        long el, delta;
        cal.setTime(t);
        el = cal.getTimeInMillis();
        delta = el - sl;
        int value = (int) (delta / (60 * 60 * 1000));

        return value;
    }
    
    /**
     * Convert OA date to date
     *
     * @param oaDate OA date
     * @return Date
     */
    public static Date fromOADate(double oaDate) {
        Date date = new Date();
        //long t = (long)((oaDate - 25569) * 24 * 3600 * 1000);
        //long t = (long) (oaDate * 1000000);
        long t = (long)BigDecimalUtil.mul(oaDate, 1000000);
        date.setTime(t);
        return date;
    }

    /**
     * Convert date to OA date
     *
     * @param date Date
     * @return OA date
     */
    public static double toOADate(Date date) {
        double oaDate = date.getTime();
        //oaDate = oaDate / (24 * 3600 * 1000) + 25569;
        //oaDate = oaDate / 1000000;
        oaDate = BigDecimalUtil.div(oaDate, 1000000);

        return oaDate;
    }
    
    /**
     * Date equals
     * @param a Date a
     * @param b Date b
     * @return If equals
     */
    public static boolean equals(Date a, Date b) {
        if (a.getTime() == b.getTime())
            return true;
        else
            return false;
    }
    // </editor-fold>
}
