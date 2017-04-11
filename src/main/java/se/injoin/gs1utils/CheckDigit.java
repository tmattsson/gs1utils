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
 * Utility methods for calculating and validating check digits as used by GS1 in data structures such as GTIN, GLN and SSCC.
 *
 * @link https://en.wikipedia.org/wiki/International_Article_Number#Calculation_of_checksum_digits
 */
public final class CheckDigit {

    /**
     * Calculates the check digit on a sequence of digits.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of at least one digit
     */
    public static char calculate(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
        if (!Internals.isDigits(s)) {
            throw new IllegalArgumentException("Invalid sequence, must be digits");
        }
        return checksum(s);
    }

    /**
     * Calculates the check digit on a sequence of digits and returns it appended to the end of the sequence.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of at least one digit
     */
    public static String calculateAndAppend(String s) {
        char checkDigit = calculate(s);
        return s + checkDigit;
    }

    /**
     * Calculates the check digit on a sequence of digits ignoring the last digit which is assumed to already be a check
     * digit.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of at least two digits
     */
    public static char recalculate(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
        if (!Internals.isDigits(s)) {
            throw new IllegalArgumentException("Invalid sequence, must be digits");
        }
        if (s.length() < 2) {
            throw new IllegalArgumentException("Invalid sequence, must be at least 2 digits");
        }
        return checksum(s.substring(0, s.length() - 1));
    }

    /**
     * Calculates the check digit on a sequence of digits ignoring the last digit which is assumed to already be a check
     * digit and returns the sequence with the check digit replaced.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of at least two digits
     */
    public static String recalculateAndApply(String s) {
        char checkDigit = recalculate(s);
        return s.substring(0, s.length() - 1) + checkDigit;
    }

    /**
     * Determines if the check digit in a digit sequence is correct. Returns null if the input string is null or not a
     * sequence of at least two digits.
     */
    public static boolean isValid(String s) {
        if (s == null || !Internals.isDigits(s) || s.length() < 2) {
            return false;
        }
        char calculated = checksum(s.substring(0, s.length() - 1));
        char actual = s.charAt(s.length() - 1);
        return actual == calculated;
    }

    /**
     * Checks if the check digit in a digit sequence is correct.
     *
     * @throws IllegalArgumentException if the input string is null, not a sequence of at least two digits or if the check digit is not correct
     */
    public static String validate(String s) {
        if (!isValid(s)) {
            throw new IllegalArgumentException("Check digit is not correct");
        }
        return s;
    }

    /**
     * Calculates a check digit for a sequence of digits where digits in odd positions have weight 3 and even positions
     * have a weight of 1.
     */
    private static char checksum(String s) {
        int sum = 0;
        for (int i = 0, position = s.length(); i < s.length(); i++, position--) {
            int n = s.charAt(i) - '0';
            sum += n + (n + n) * (position & 1);
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }
}
