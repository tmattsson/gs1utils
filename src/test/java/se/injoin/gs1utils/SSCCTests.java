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

public class SSCCTests {

    @Test
    public void testLengthConstant() {
        assertEquals(18, SSCC.LENGTH);
    }

    @Test
    public void testIsSSCC() {
        assertFalse(SSCC.isSSCC(null));
        assertFalse(SSCC.isSSCC(""));
        assertFalse(SSCC.isSSCC(" "));
        assertFalse(SSCC.isSSCC("A"));
        assertFalse(SSCC.isSSCC("ABCDEFGHIJKLMNOPQR"));
        assertFalse(SSCC.isSSCC("1"));
        assertFalse(SSCC.isSSCC("12"));
        assertFalse(SSCC.isSSCC("123"));
        assertFalse(SSCC.isSSCC("1234"));
        assertFalse(SSCC.isSSCC("12345"));
        assertFalse(SSCC.isSSCC("123456"));
        assertFalse(SSCC.isSSCC("12345678901"));
        assertFalse(SSCC.isSSCC("12345678901234567"));
        assertTrue(SSCC.isSSCC("123456789012345678"));
        assertFalse(SSCC.isSSCC("1234567890123456789"));
    }

    @Test
    public void testIsValid() {
        assertFalse(SSCC.isValid(null));
        assertFalse(SSCC.isValid(""));
        assertFalse(SSCC.isValid(" "));
        assertFalse(SSCC.isValid("A"));
        assertFalse(SSCC.isValid("ABCDEFGHIJKLMNOPQR"));
        assertFalse(SSCC.isValid("1"));
        assertFalse(SSCC.isValid("123456789012"));
        assertFalse(SSCC.isValid("12345678901234567"));
        assertFalse(SSCC.isValid("1234567890123456789"));
        assertTrue(SSCC.isValid("106141411234567897"));
        assertFalse(SSCC.isValid("106141411234567890"));
    }

    @Test
    public void testValidateFormatAndCheckDigit() {
        try {
            SSCC.validateFormatAndCheckDigit(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("SSCC must not be null", e.getMessage());
        }
        try {
            SSCC.validateFormatAndCheckDigit("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid SSCC , must be digits", e.getMessage());
        }
        try {
            SSCC.validateFormatAndCheckDigit("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid SSCC A, must be digits", e.getMessage());
        }
        try {
            SSCC.validateFormatAndCheckDigit("123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid SSCC 123, must be 18 digits long", e.getMessage());
        }
        try {
            SSCC.validateFormatAndCheckDigit("123456789012345678");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Check digit is not correct", e.getMessage());
        }
        try {
            SSCC.validateFormatAndCheckDigit("1234567890123456789");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid SSCC 1234567890123456789, must be 18 digits long", e.getMessage());
        }
        SSCC.validateFormatAndCheckDigit("106141411234567897");
    }
}
