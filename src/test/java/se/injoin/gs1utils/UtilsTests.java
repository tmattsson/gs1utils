/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.injoin.gs1utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTests {

    @Test
    public void testStartsWithNZeroes() {
        assertFalse(Internals.startsWithNZeroes(null, -1));
        assertFalse(Internals.startsWithNZeroes(null, 0));
        assertFalse(Internals.startsWithNZeroes(null, 1));

        assertFalse(Internals.startsWithNZeroes("0", -1));
        assertTrue(Internals.startsWithNZeroes("0", 0));
        assertTrue(Internals.startsWithNZeroes("0", 1));
        assertFalse(Internals.startsWithNZeroes("0", 2));

        assertFalse(Internals.startsWithNZeroes("A", -1));
        assertTrue(Internals.startsWithNZeroes("A", 0));
        assertFalse(Internals.startsWithNZeroes("A", 1));
        assertFalse(Internals.startsWithNZeroes("A", 2));

        assertFalse(Internals.startsWithNZeroes("1", -1));
        assertTrue(Internals.startsWithNZeroes("1", 0));
        assertFalse(Internals.startsWithNZeroes("1", 1));
        assertFalse(Internals.startsWithNZeroes("1", 2));

        assertFalse(Internals.startsWithNZeroes("01", -1));
        assertTrue(Internals.startsWithNZeroes("01", 0));
        assertTrue(Internals.startsWithNZeroes("01", 1));
        assertFalse(Internals.startsWithNZeroes("01", 2));

        assertFalse(Internals.startsWithNZeroes("001", -1));
        assertTrue(Internals.startsWithNZeroes("001", 0));
        assertTrue(Internals.startsWithNZeroes("001", 1));
        assertTrue(Internals.startsWithNZeroes("001", 2));
        assertFalse(Internals.startsWithNZeroes("001", 3));

        assertFalse(Internals.startsWithNZeroes("010", -1));
        assertTrue(Internals.startsWithNZeroes("010", 0));
        assertTrue(Internals.startsWithNZeroes("010", 1));
        assertFalse(Internals.startsWithNZeroes("010", 2));

        assertFalse(Internals.startsWithNZeroes("0X0", -1));
        assertTrue(Internals.startsWithNZeroes("0X0", 0));
        assertTrue(Internals.startsWithNZeroes("0X0", 1));
        assertFalse(Internals.startsWithNZeroes("0X0", 2));
    }

    @Test
    public void testCountLeadingZeroes() {
        assertEquals(0, Internals.countLeadingZeroes(null));
        assertEquals(0, Internals.countLeadingZeroes(""));
        assertEquals(0, Internals.countLeadingZeroes("A"));
        assertEquals(1, Internals.countLeadingZeroes("0"));
        assertEquals(1, Internals.countLeadingZeroes("0A"));
        assertEquals(2, Internals.countLeadingZeroes("00"));
        assertEquals(0, Internals.countLeadingZeroes("A0"));
    }

    @Test
    public void testLeftPadWithZeroes() {
        assertNull(Internals.leftPadWithZeroes(null, -1));
        assertNull(Internals.leftPadWithZeroes(null, 0));
        assertNull(Internals.leftPadWithZeroes(null, 1));

        assertEquals("", Internals.leftPadWithZeroes("", -1));
        assertEquals("", Internals.leftPadWithZeroes("", 0));
        assertEquals("0", Internals.leftPadWithZeroes("", 1));
        assertEquals("00", Internals.leftPadWithZeroes("", 2));
        assertEquals("000", Internals.leftPadWithZeroes("", 3));

        assertEquals("ABC", Internals.leftPadWithZeroes("ABC", -1));
        assertEquals("ABC", Internals.leftPadWithZeroes("ABC", 0));
        assertEquals("ABC", Internals.leftPadWithZeroes("ABC", 1));
        assertEquals("ABC", Internals.leftPadWithZeroes("ABC", 2));
        assertEquals("ABC", Internals.leftPadWithZeroes("ABC", 3));
        assertEquals("0ABC", Internals.leftPadWithZeroes("ABC", 4));
        assertEquals("00ABC", Internals.leftPadWithZeroes("ABC", 5));
        assertEquals("000ABC", Internals.leftPadWithZeroes("ABC", 6));
    }

    @Test
    public void testIsDigits() {
        assertFalse(Internals.isDigits(null));
        assertFalse(Internals.isDigits(""));
        assertFalse(Internals.isDigits(" "));
        assertFalse(Internals.isDigits("A"));
        assertTrue(Internals.isDigits("1"));
        assertTrue(Internals.isDigits("123"));
        assertFalse(Internals.isDigits("123A"));
        assertFalse(Internals.isDigits("A123"));
    }

    @Test
    public void testValidateFormat() {

    }
}
