/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * --------------
 * HourTests.java
 * --------------
 * (C) Copyright 2002-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 29-Jan-2002 : Version 1 (DG);
 * 17-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Added serialization test (DG);
 * 21-Oct-2003 : Added hashCode test (DG);
 * 11-Jan-2005 : Added test for non-clonability (DG);
 * 05-Oct-2006 : Added new tests (DG);
 * 21-Jun-2007 : Removed JCommon dependencies (DG);
 * 11-Jul-2007 : Fixed bad time zone assumption (DG);
 *
 */

package org.jfree.data.time.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.MonthConstants;

/**
 * Tests for the {@link Hour} class.
 */
public class HourTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(HourTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public HourTests(String name) {
        super(name);
    }

    /**
     * Common test setup.
     */
    protected void setUp() {
        // no setup
    }

    /**
     * Check that an Hour instance is equal to itself.
     *
     * SourceForge Bug ID: 558850.
     */
    public void testEqualsSelf() {
        Hour hour = new Hour();
        assertTrue(hour.equals(hour));
    }

    /**
     * Tests the equals method.
     */
    public void testEquals() {
        Hour hour1 = new Hour(15, new Day(29, MonthConstants.MARCH, 2002));
        Hour hour2 = new Hour(15, new Day(29, MonthConstants.MARCH, 2002));
        assertTrue(hour1.equals(hour2));
    }

    /**
     * In GMT, the 4pm on 21 Mar 2002 is java.util.Date(1,014,307,200,000L).
     * Use this to check the hour constructor.
     */
    public void testDateConstructor1() {

        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar c = new GregorianCalendar(zone);
        Hour h1 = new Hour(new Date(1014307199999L), zone);
        Hour h2 = new Hour(new Date(1014307200000L), zone);

        assertEquals(15, h1.getHour());
        assertEquals(1014307199999L, h1.getLastMillisecond(c));

        assertEquals(16, h2.getHour());
        assertEquals(1014307200000L, h2.getFirstMillisecond(c));

    }

    /**
     * In Sydney, the 4pm on 21 Mar 2002 is java.util.Date(1,014,267,600,000L).
     * Use this to check the hour constructor.
     */
    public void testDateConstructor2() {

        TimeZone zone = TimeZone.getTimeZone("Australia/Sydney");
        Calendar c = new GregorianCalendar(zone);
        Hour h1 = new Hour(new Date(1014267599999L), zone);
        Hour h2 = new Hour (new Date(1014267600000L), zone);

        assertEquals(15, h1.getHour());
        assertEquals(1014267599999L, h1.getLastMillisecond(c));

        assertEquals(16, h2.getHour());
        assertEquals(1014267600000L, h2.getFirstMillisecond(c));

    }

    /**
     * Set up an hour equal to hour zero, 1 January 1900.  Request the
     * previous hour, it should be null.
     */
    public void testFirstHourPrevious() {

        Hour first = new Hour(0, new Day(1, MonthConstants.JANUARY, 1900));
        Hour previous = (Hour) first.previous();
        assertNull(previous);

    }

    /**
     * Set up an hour equal to hour zero, 1 January 1900.  Request the next
     * hour, it should be null.
     */
    public void testFirstHourNext() {
        Hour first = new Hour(0, new Day(1, MonthConstants.JANUARY, 1900));
        Hour next = (Hour) first.next();
        assertEquals(1, next.getHour());
        assertEquals(1900, next.getYear());
    }

    /**
     * Set up an hour equal to hour zero, 1 January 1900.  Request the previous
     * hour, it should be null.
     */
    public void testLastHourPrevious() {

        Hour last = new Hour(23, new Day(31, MonthConstants.DECEMBER, 9999));
        Hour previous = (Hour) last.previous();
        assertEquals(22, previous.getHour());
        assertEquals(9999, previous.getYear());

    }

    /**
     * Set up an hour equal to hour zero, 1 January 1900.  Request the next
     * hour, it should be null.
     */
    public void testLastHourNext() {

        Hour last = new Hour(23, new Day(31, MonthConstants.DECEMBER, 9999));
        Hour next = (Hour) last.next();
        assertNull(next);

    }

    /**
     * Problem for date parsing.
     */
    public void testParseHour() {

        // test 1...
        Hour h = Hour.parseHour("2002-01-29 13");
        assertEquals(13, h.getHour());

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        Hour h1 = new Hour();
        Hour h2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(h1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
            h2 = (Hour) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(h1, h2);

    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    public void testHashcode() {
        Hour h1 = new Hour(7, 9, 10, 1999);
        Hour h2 = new Hour(7, 9, 10, 1999);
        assertTrue(h1.equals(h2));
        int hash1 = h1.hashCode();
        int hash2 = h2.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * The {@link Hour} class is immutable, so should not be {@link Cloneable}.
     */
    public void testNotCloneable() {
        Hour h = new Hour(7, 9, 10, 1999);
        assertFalse(h instanceof Cloneable);
    }

/**
     * Some checks for the getFirstMillisecond() method.
     */
    public void testGetFirstMillisecond() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        TimeZone savedZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Hour h = new Hour(15, 1, 4, 2006);
        assertEquals(1143900000000L, h.getFirstMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    public void testGetFirstMillisecondWithTimeZone() {
        Hour h = new Hour(15, 1, 4, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar c = new GregorianCalendar(zone);
        assertEquals(-623293200000L, h.getFirstMillisecond(c));

        // try null calendar
        boolean pass = false;
        try {
            h.getFirstMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    public void testGetFirstMillisecondWithCalendar() {
        Hour h = new Hour(2, 15, 4, 2000);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(955764000000L, h.getFirstMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            h.getFirstMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getLastMillisecond() method.
     */
    public void testGetLastMillisecond() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        TimeZone savedZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Hour h = new Hour(1, 1, 1, 1970);
        assertEquals(3599999L, h.getLastMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    public void testGetLastMillisecondWithTimeZone() {
        Hour h = new Hour(2, 7, 7, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar c = new GregorianCalendar(zone);
        assertEquals(-614959200001L, h.getLastMillisecond(c));

        // try null calendar
        boolean pass = false;
        try {
            h.getLastMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    public void testGetLastMillisecondWithCalendar() {
        Hour h = new Hour(21, 21, 4, 2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(987890399999L, h.getLastMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            h.getLastMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getSerialIndex() method.
     */
    public void testGetSerialIndex() {
        Hour h = new Hour(1, 1, 1, 2000);
        assertEquals(876625L, h.getSerialIndex());
        h = new Hour(1, 1, 1, 1900);
        assertEquals(49L, h.getSerialIndex());
    }

    /**
     * Some checks for the testNext() method.
     */
    public void testNext() {
        Hour h = new Hour(1, 12, 12, 2000);
        h = (Hour) h.next();
        assertEquals(2000, h.getYear());
        assertEquals(12, h.getMonth());
        assertEquals(12, h.getDayOfMonth());
        assertEquals(2, h.getHour());
        h = new Hour(23, 31, 12, 9999);
        assertNull(h.next());
    }

    /**
     * Some checks for the getStart() method.
     */
    public void testGetStart() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.ITALY);
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.set(2006, Calendar.JANUARY, 16, 3, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Hour h = new Hour(3, 16, 1, 2006);
        assertEquals(cal.getTime(), h.getStart());
        Locale.setDefault(saved);
    }

    /**
     * Some checks for the getEnd() method.
     */
    public void testGetEnd() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.ITALY);
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.set(2006, Calendar.JANUARY, 8, 1, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Hour h = new Hour(1, 8, 1, 2006);
        assertEquals(cal.getTime(), h.getEnd());
        Locale.setDefault(saved);
    }

}