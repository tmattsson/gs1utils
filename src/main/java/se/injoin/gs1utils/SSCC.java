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

/**
 * Utility methods for serial shipping container codes (SSCC).
 */
public final class SSCC {

    public static final int LENGTH = 18;

    /**
     * Determines if a string is a valid SSCC without verifying its check digit.
     */
    public static boolean isSSCC(String sscc) {
        return Internals.isDigits(sscc) && sscc.length() == LENGTH;
    }

    /**
     * Validates that a string is a SSCC with a correct check digit.
     */
    public static boolean isValid(String sscc) {
        return isSSCC(sscc) && CheckDigit.isValid(sscc);
    }

    /**
     * Checks if a string is a correctly formatted SSCC without verifying its check digit.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of exactly 18 digits
     */
    public static String validateFormat(String gln) {
        return Internals.validateFormat("SSCC", LENGTH, gln);
    }

    /**
     * Checks if a string is a SSCC with correct check digit.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of exactly 18 digits or if the check digit is not correct
     */
    public static String validateFormatAndCheckDigit(String gln) {
        return CheckDigit.validate(validateFormat(gln));
    }
}
