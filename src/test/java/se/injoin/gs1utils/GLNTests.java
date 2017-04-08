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

public class GLNTests {

    @Test
    public void testLengthConstant() {
        assertEquals(13, GLN.LENGTH);
    }

    @Test
    public void testIsGLN() {
        assertFalse(GLN.isGLN(null));
        assertFalse(GLN.isGLN(""));
        assertFalse(GLN.isGLN(" "));
        assertFalse(GLN.isGLN("A"));
        assertFalse(GLN.isGLN("ABCDEFGHIJKLM"));
        assertFalse(GLN.isGLN("1"));
        assertFalse(GLN.isGLN("12"));
        assertFalse(GLN.isGLN("123"));
        assertFalse(GLN.isGLN("1234"));
        assertFalse(GLN.isGLN("12345"));
        assertFalse(GLN.isGLN("123456"));
        assertFalse(GLN.isGLN("12345678901"));
        assertFalse(GLN.isGLN("123456789012"));
        assertTrue(GLN.isGLN("1234567890123"));
        assertFalse(GLN.isGLN("12345678901234"));
        assertFalse(GLN.isGLN("123456789012345"));
    }

    @Test
    public void testIsValid() {
        assertFalse(GLN.isValid(null));
        assertFalse(GLN.isValid(""));
        assertFalse(GLN.isValid(" "));
        assertFalse(GLN.isValid("A"));
        assertFalse(GLN.isValid("ABCDEFGHIJKLM"));
        assertFalse(GLN.isValid("1"));
        assertFalse(GLN.isValid("123456789012"));
        assertFalse(GLN.isValid("12345678901234"));
        assertTrue(GLN.isValid("7594567000014"));
        assertTrue(GLN.isValid("8431472300015"));
        assertFalse(GLN.isValid("7594567000010"));
        assertFalse(GLN.isValid("8431472300010"));
    }

    @Test
    public void testValidateFormatAndCheckDigit() {
        try {
            GLN.validateFormatAndCheckDigit(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("GLN must not be null", e.getMessage());
        }
        try {
            GLN.validateFormatAndCheckDigit("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GLN , must be digits", e.getMessage());
        }
        try {
            GLN.validateFormatAndCheckDigit("A");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GLN A, must be digits", e.getMessage());
        }
        try {
            GLN.validateFormatAndCheckDigit("123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GLN 123, must be 13 digits long", e.getMessage());
        }
        try {
            GLN.validateFormatAndCheckDigit("1234567890123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Check digit is not correct", e.getMessage());
        }
        try {
            GLN.validateFormatAndCheckDigit("12345678901234");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid GLN 12345678901234, must be 13 digits long", e.getMessage());
        }
        GLN.validateFormatAndCheckDigit("7594567000014");
    }
}
