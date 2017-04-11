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

public class CheckDigitTests {

    private static final String EAN13 = "4006381333931";
    private static final String ISSN = "9772049363002";
    private static final String ISBN = "9789137138114";
    private static final String GTIN14_WEIGHT = "02388060112344";
    private static final String UPC_A = "036000291452";

    @Test
    public void testIsValid() {
        assertFalse(CheckDigit.isValid(null));
        assertFalse(CheckDigit.isValid(""));
        assertFalse(CheckDigit.isValid(" "));
        assertFalse(CheckDigit.isValid("a"));
        assertFalse(CheckDigit.isValid("1"));
        assertTrue(CheckDigit.isValid("17"));
        assertFalse(CheckDigit.isValid("4006381333932"));
        assertFalse(CheckDigit.isValid("ABCDEFGHIJKLM"));
        assertTrue(CheckDigit.isValid(EAN13));
        assertTrue(CheckDigit.isValid(ISSN));
        assertTrue(CheckDigit.isValid(ISBN));
        assertTrue(CheckDigit.isValid(GTIN14_WEIGHT));
        assertTrue(CheckDigit.isValid(UPC_A));
    }

    @Test
    public void testCalculate() {
        try {
            CheckDigit.calculate(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            CheckDigit.calculate("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.calculate("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        assertEquals('1', CheckDigit.calculate("400638133393"));
        assertEquals('0', CheckDigit.calculate("200739410000"));
    }

    @Test
    public void testCalculateAndAppend() {
        try {
            CheckDigit.calculateAndAppend(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            CheckDigit.calculateAndAppend("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.calculateAndAppend("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        assertEquals(EAN13, CheckDigit.calculateAndAppend("400638133393"));
    }

    @Test
    public void testRecalculate() {
        try {
            CheckDigit.recalculate(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            CheckDigit.recalculate("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.recalculate("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.recalculate("1");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be at least 2 digits", e.getMessage());
        }
        assertEquals('1', CheckDigit.recalculate(EAN13));
    }

    @Test
    public void testRecalculateAndApply() {
        try {
            CheckDigit.recalculateAndApply(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            CheckDigit.recalculateAndApply("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.recalculateAndApply("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be digits", e.getMessage());
        }
        try {
            CheckDigit.recalculateAndApply("1");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid sequence, must be at least 2 digits", e.getMessage());
        }
        assertEquals(EAN13, CheckDigit.recalculateAndApply("4006381333930"));
    }
}
