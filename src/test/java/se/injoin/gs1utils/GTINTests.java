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

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class GTINTests {

    @Test
    public void testIsGTIN() {
        assertFalse(GTIN.isGTIN(null));
        assertFalse(GTIN.isGTIN(""));
        assertFalse(GTIN.isGTIN(" "));
        assertFalse(GTIN.isGTIN("1234567"));
        assertTrue(GTIN.isGTIN("12345678"));
        assertFalse(GTIN.isGTIN("123456789"));
        assertFalse(GTIN.isGTIN("1234567890"));
        assertFalse(GTIN.isGTIN("12345678901"));
        assertTrue(GTIN.isGTIN("123456789012"));
        assertTrue(GTIN.isGTIN("1234567890123"));
        assertTrue(GTIN.isGTIN("12345678901234"));
        assertFalse(GTIN.isGTIN("123456789012345"));
        assertFalse(GTIN.isGTIN("1234567890123456"));
    }

    @Test
    public void testIsGTIN14() {
        assertFalse(GTIN.isGTIN14(null));
        assertFalse(GTIN.isGTIN14(""));
        assertFalse(GTIN.isGTIN14(" "));
        assertFalse(GTIN.isGTIN14("1234567"));
        assertFalse(GTIN.isGTIN14("12345678"));
        assertFalse(GTIN.isGTIN14("123456789"));
        assertFalse(GTIN.isGTIN14("1234567890"));
        assertFalse(GTIN.isGTIN14("12345678901"));
        assertFalse(GTIN.isGTIN14("123456789012"));
        assertFalse(GTIN.isGTIN14("1234567890123"));
        assertTrue(GTIN.isGTIN14("12345678901234"));
        assertFalse(GTIN.isGTIN14("123456789012345"));
        assertFalse(GTIN.isGTIN14("1234567890123456"));
    }

    @Test
    public void testIsGTIN13() {
        assertFalse(GTIN.isGTIN13(null));
        assertFalse(GTIN.isGTIN13(""));
        assertFalse(GTIN.isGTIN13(" "));
        assertFalse(GTIN.isGTIN13("1234567"));
        assertFalse(GTIN.isGTIN13("12345678"));
        assertFalse(GTIN.isGTIN13("123456789"));
        assertFalse(GTIN.isGTIN13("1234567890"));
        assertFalse(GTIN.isGTIN13("12345678901"));
        assertFalse(GTIN.isGTIN13("123456789012"));
        assertTrue(GTIN.isGTIN13("1234567890123"));
        assertFalse(GTIN.isGTIN13("12345678901234"));
        assertFalse(GTIN.isGTIN13("123456789012345"));
        assertFalse(GTIN.isGTIN13("1234567890123456"));
    }

    @Test
    public void testIsGTIN12() {
        assertFalse(GTIN.isGTIN12(null));
        assertFalse(GTIN.isGTIN12(""));
        assertFalse(GTIN.isGTIN12(" "));
        assertFalse(GTIN.isGTIN12("1234567"));
        assertFalse(GTIN.isGTIN12("12345678"));
        assertFalse(GTIN.isGTIN12("123456789"));
        assertFalse(GTIN.isGTIN12("1234567890"));
        assertFalse(GTIN.isGTIN12("12345678901"));
        assertTrue(GTIN.isGTIN12("123456789012"));
        assertFalse(GTIN.isGTIN12("1234567890123"));
        assertFalse(GTIN.isGTIN12("12345678901234"));
        assertFalse(GTIN.isGTIN12("123456789012345"));
        assertFalse(GTIN.isGTIN12("1234567890123456"));
    }

    @Test
    public void testIsGTIN8() {
        assertFalse(GTIN.isGTIN8(null));
        assertFalse(GTIN.isGTIN8(""));
        assertFalse(GTIN.isGTIN8(" "));
        assertFalse(GTIN.isGTIN8("1234567"));
        assertTrue(GTIN.isGTIN8("12345678"));
        assertFalse(GTIN.isGTIN8("123456789"));
        assertFalse(GTIN.isGTIN8("1234567890"));
        assertFalse(GTIN.isGTIN8("12345678901"));
        assertFalse(GTIN.isGTIN8("123456789012"));
        assertFalse(GTIN.isGTIN8("1234567890123"));
        assertFalse(GTIN.isGTIN8("12345678901234"));
        assertFalse(GTIN.isGTIN8("123456789012345"));
        assertFalse(GTIN.isGTIN8("1234567890123456"));
    }

    @Test
    public void testIsValid() {
        assertFalse(GTIN.isValid(null));
        assertFalse(GTIN.isValid(""));
        assertFalse(GTIN.isValid(" "));
        assertTrue(GTIN.isValid("4006381333931"));
        assertFalse(GTIN.isValid("4006381333930"));
    }

    @Test
    public void testIsISSN() {
        assertFalse(GTIN.isISSN(null));
        assertFalse(GTIN.isISSN(""));
        assertFalse(GTIN.isISSN(" "));
        assertTrue(GTIN.isISSN("9772049363002"));
        assertTrue(GTIN.isISSN("09772049363002"));
        assertFalse(GTIN.isISSN("123456789012"));
    }

    @Test
    public void testIsISBN() {
        assertFalse(GTIN.isISBN(null));
        assertFalse(GTIN.isISBN(""));
        assertFalse(GTIN.isISBN(" "));
        assertTrue(GTIN.isISBN("9789137138114"));
        assertTrue(GTIN.isISBN("09789137138114"));
        assertTrue(GTIN.isISBN("9799137138114"));
        assertTrue(GTIN.isISBN("09799137138114"));
        assertFalse(GTIN.isISBN("9790137138114"));
        assertFalse(GTIN.isISBN("09790137138114"));
        assertFalse(GTIN.isISBN("123456789012"));
    }

    @Test
    public void testIsISMN() {
        assertFalse(GTIN.isISMN(null));
        assertFalse(GTIN.isISMN(""));
        assertFalse(GTIN.isISMN(" "));
        assertFalse(GTIN.isISMN("9791137138114"));
        assertTrue(GTIN.isISMN("9790137138114"));
        assertTrue(GTIN.isISMN("09790137138114"));
        assertFalse(GTIN.isISMN("123456789012"));
    }

    @Test
    public void testToGTIN14() {
        assertNull(GTIN.toGTIN14(null));
        assertEquals("12345678901234", GTIN.toGTIN14("12345678901234"));
        assertEquals("01234567890123", GTIN.toGTIN14("1234567890123"));
        assertEquals("00123456789012", GTIN.toGTIN14("123456789012"));
        assertEquals("00000012345678", GTIN.toGTIN14("12345678"));
        try {
            GTIN.toGTIN8("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 14, 13, 12 or 8 digits long", e.getMessage());
        }
        try {
            GTIN.toGTIN8("ABCDEFGHIJKL");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCDEFGHIJKL, must be digits", e.getMessage());
        }
    }

    @Test
    public void testToGTIN13() {
        assertNull(GTIN.toGTIN13(null));
        assertEquals("1234567890123", GTIN.toGTIN13("01234567890123"));
        assertEquals("1234567890123", GTIN.toGTIN13("1234567890123"));
        assertEquals("0123456789012", GTIN.toGTIN13("123456789012"));
        assertEquals("0000012345678", GTIN.toGTIN13("12345678"));
        try {
            GTIN.toGTIN13("12345678901234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12345678901234 could not be converted to GTIN-13", e.getMessage());
        }
        try {
            GTIN.toGTIN8("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 14, 13, 12 or 8 digits long", e.getMessage());
        }
        try {
            GTIN.toGTIN8("ABCDEFGHIJKL");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCDEFGHIJKL, must be digits", e.getMessage());
        }
    }

    @Test
    public void testToGTIN12() {
        assertNull(GTIN.toGTIN12(null));
        assertEquals("000012345678", GTIN.toGTIN12("00000012345678"));
        assertEquals("000012345678", GTIN.toGTIN12("0000012345678"));
        assertEquals("123456789012", GTIN.toGTIN12("123456789012"));
        assertEquals("000012345678", GTIN.toGTIN12("12345678"));
        try {
            GTIN.toGTIN12("12345678901234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12345678901234 could not be converted to GTIN-12", e.getMessage());
        }
        try {
            GTIN.toGTIN12("1234567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 1234567890123 could not be converted to GTIN-12", e.getMessage());
        }
        try {
            GTIN.toGTIN8("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 14, 13, 12 or 8 digits long", e.getMessage());
        }
        try {
            GTIN.toGTIN8("ABCDEFGHIJKL");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCDEFGHIJKL, must be digits", e.getMessage());
        }
    }

    @Test
    public void testToGTIN8() {
        assertNull(GTIN.toGTIN8(null));
        assertEquals("12345678", GTIN.toGTIN8("00000012345678"));
        assertEquals("12345678", GTIN.toGTIN8("0000012345678"));
        assertEquals("12345678", GTIN.toGTIN8("000012345678"));
        assertEquals("12345678", GTIN.toGTIN8("12345678"));
        try {
            GTIN.toGTIN8("12345678901234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12345678901234 could not be converted to GTIN-8", e.getMessage());
        }
        try {
            GTIN.toGTIN8("1234567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 1234567890123 could not be converted to GTIN-8", e.getMessage());
        }
        try {
            GTIN.toGTIN8("123456789012");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 123456789012 could not be converted to GTIN-8", e.getMessage());
        }
        try {
            GTIN.toGTIN8("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 14, 13, 12 or 8 digits long", e.getMessage());
        }
        try {
            GTIN.toGTIN8("ABCDEFGHIJKL");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCDEFGHIJKL, must be digits", e.getMessage());
        }
    }

    @Test
    public void testShorten() {
        assertNull(GTIN.shorten(null));
        assertEquals("12345678", GTIN.shorten("12345678"));
        assertEquals("12345678", GTIN.shorten("000012345678"));
        assertEquals("12345678", GTIN.shorten("0000012345678"));
        assertEquals("12345678", GTIN.shorten("00000012345678"));
        assertEquals("123456789012", GTIN.shorten("123456789012"));
        assertEquals("123456789012", GTIN.shorten("0123456789012"));
        assertEquals("123456789012", GTIN.shorten("00123456789012"));
        assertEquals("1234567890123", GTIN.shorten("1234567890123"));
        assertEquals("1234567890123", GTIN.shorten("01234567890123"));
        assertEquals("12345678901234", GTIN.shorten("12345678901234"));
    }

    @Test
    public void testIsWeightItem() {
        assertFalse(GTIN.isWeightItem(null));
        assertFalse(GTIN.isWeightItem(""));
        assertFalse(GTIN.isWeightItem(" "));
        assertFalse(GTIN.isWeightItem("1234"));
        assertFalse(GTIN.isWeightItem("ABCD"));
        assertFalse(GTIN.isWeightItem("22345678"));
        assertFalse(GTIN.isWeightItem("223456789012"));
        assertTrue(GTIN.isWeightItem("2088060112344"));
        assertTrue(GTIN.isWeightItem("2188060112344"));
        assertTrue(GTIN.isWeightItem("2288060112344"));
        assertTrue(GTIN.isWeightItem("2388060112344"));
        assertTrue(GTIN.isWeightItem("2488060112344"));
        assertTrue(GTIN.isWeightItem("2588060112344"));
        assertFalse(GTIN.isWeightItem("2688060112344"));
        assertFalse(GTIN.isWeightItem("2788060112344"));
        assertFalse(GTIN.isWeightItem("2888060112344"));
        assertFalse(GTIN.isWeightItem("2988060112344"));
        assertTrue(GTIN.isWeightItem("02388060112344"));
    }

    @Test
    public void testIsWeightItemWithPrice() {
        assertFalse(GTIN.isWeightItemWithPrice(null));
        assertFalse(GTIN.isWeightItemWithPrice(""));
        assertFalse(GTIN.isWeightItemWithPrice(" "));
        assertFalse(GTIN.isWeightItemWithPrice("1234"));
        assertFalse(GTIN.isWeightItemWithPrice("ABCD"));
        assertFalse(GTIN.isWeightItemWithPrice("22345678"));
        assertFalse(GTIN.isWeightItemWithPrice("223456789012"));
        assertTrue(GTIN.isWeightItemWithPrice("2088060112344"));
        assertTrue(GTIN.isWeightItemWithPrice("2188060112344"));
        assertTrue(GTIN.isWeightItemWithPrice("2288060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2388060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2488060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2588060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2688060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2788060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2888060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("2988060112344"));
        assertFalse(GTIN.isWeightItemWithPrice("02388060112344"));
    }

    @Test
    public void testIsWeightItemWithWeight() {
        assertFalse(GTIN.isWeightItemWithWeight(null));
        assertFalse(GTIN.isWeightItemWithWeight(""));
        assertFalse(GTIN.isWeightItemWithWeight(" "));
        assertFalse(GTIN.isWeightItemWithWeight("1234"));
        assertFalse(GTIN.isWeightItemWithWeight("ABCD"));
        assertFalse(GTIN.isWeightItemWithWeight("22345678"));
        assertFalse(GTIN.isWeightItemWithWeight("223456789012"));
        assertFalse(GTIN.isWeightItemWithWeight("2088060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2188060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2288060112344"));
        assertTrue(GTIN.isWeightItemWithWeight("2388060112344"));
        assertTrue(GTIN.isWeightItemWithWeight("2488060112344"));
        assertTrue(GTIN.isWeightItemWithWeight("2588060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2688060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2788060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2888060112344"));
        assertFalse(GTIN.isWeightItemWithWeight("2988060112344"));
        assertTrue(GTIN.isWeightItemWithWeight("02388060112344"));
    }

    @Test
    public void testGetPriceFromWeightItem() {
        try {
            GTIN.extractPriceFromWeightItem(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            GTIN.extractPriceFromWeightItem("ABCD");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCD, must be digits", e.getMessage());
        }
        try {
            GTIN.extractPriceFromWeightItem("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 13 or 14 digits long", e.getMessage());
        }
        try {
            GTIN.extractPriceFromWeightItem("12334567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12334567890123 is not a weight item with price", e.getMessage());
        }
        assertEquals(new BigDecimal("12.34"), GTIN.extractPriceFromWeightItem("02088060112344"));
        assertEquals(new BigDecimal("123.40"), GTIN.extractPriceFromWeightItem("02188060112344"));
        assertEquals(new BigDecimal("1234.00"), GTIN.extractPriceFromWeightItem("02288060112344"));
    }

    @Test
    public void testExtractWeightFromWeightItem() {
        try {
            GTIN.extractWeightFromWeightItem(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            GTIN.extractWeightFromWeightItem("ABCD");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCD, must be digits", e.getMessage());
        }
        try {
            GTIN.extractWeightFromWeightItem("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 13 or 14 digits long", e.getMessage());
        }
        try {
            GTIN.extractWeightFromWeightItem("12334567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12334567890123 is not a weight item with weight", e.getMessage());
        }
        assertEquals(1234, GTIN.extractWeightFromWeightItem("02388060112344"));
        assertEquals(12340, GTIN.extractWeightFromWeightItem("02488060112344"));
        assertEquals(123400, GTIN.extractWeightFromWeightItem("02588060112344"));
    }

    @Test
    public void testNormalizeWeightItem() {
        try {
            GTIN.normalizeWeightItem(null);
            fail();
        } catch (NullPointerException ignored) {
        }
        try {
            GTIN.normalizeWeightItem("ABCD");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN ABCD, must be digits", e.getMessage());
        }
        try {
            GTIN.normalizeWeightItem("1234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GTIN 1234, must be 13 or 14 digits long", e.getMessage());
        }
        try {
            GTIN.normalizeWeightItem("12334567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("GTIN 12334567890123 is not a weight item", e.getMessage());
        }
        assertEquals("02388060100006", GTIN.normalizeWeightItem("02388060112344"));
        assertEquals("2388060100006", GTIN.normalizeWeightItem("2388060112344"));
    }
}
