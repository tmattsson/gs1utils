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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static se.injoin.gs1utils.Internals.*;

/**
 * Utility methods for global trade item numbers (GTIN).
 */
public final class GTIN {

    /**
     * Determines if a string is a valid GTIN without verifying its check digit.
     */
    public static boolean isGTIN(String gtin) {
        return isDigits(gtin) && (gtin.length() == 14 || gtin.length() == 13 || gtin.length() == 12 || gtin.length() == 8);
    }

    /**
     * Determines if a string is a valid GTIN-14 without verifying its check digit.
     */
    public static boolean isGTIN14(String gtin) {
        return isDigits(gtin) && gtin.length() == 14;
    }

    /**
     * Determines if a string is a valid GTIN-13 without verifying its check digit.
     */
    public static boolean isGTIN13(String gtin) {
        return isDigits(gtin) && gtin.length() == 13;
    }

    /**
     * Determines if a string is a valid GTIN-12 without verifying its check digit.
     */
    public static boolean isGTIN12(String gtin) {
        return isDigits(gtin) && gtin.length() == 12;
    }

    /**
     * Determines if a string is a valid GTIN-8 without verifying its check digit.
     */
    public static boolean isGTIN8(String gtin) {
        return isDigits(gtin) && gtin.length() == 8;
    }

    /**
     * Validates that a string is a GTIN with a correct check digit.
     */
    public static boolean isValid(String gtin) {
        return isGTIN(gtin) && CheckDigit.isValid(gtin);
    }

    /**
     * Checks if a string is a correctly formatted GTIN without verifying its check digit.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of 14, 13, 12 or 8 digits
     */
    public static String validateFormat(String gtin) {
        if (gtin == null) {
            throw new NullPointerException("GTIN must not be null");
        }
        if (!isDigits(gtin)) {
            throw new IllegalArgumentException("Invalid GTIN " + gtin + ", must be digits");
        }
        if (gtin.length() != 14 && gtin.length() != 13 && gtin.length() != 12 && gtin.length() != 8) {
            throw new IllegalArgumentException("Invalid GTIN " + gtin + ", must be 14, 13, 12 or 8 digits long");
        }
        return gtin;
    }

    /**
     * Checks if a string is a GTIN with correct check digit.
     *
     * @throws NullPointerException     if the input string is null
     * @throws IllegalArgumentException if the input string is not a sequence of 14, 13, 12 or 8 digits or if check digit is not correct
     */
    public static String validateFormatAndCheckDigit(String gtin) {
        return CheckDigit.validate(validateFormat(gtin));
    }

    /**
     * Determines if a GTIN is an ISSN item.
     * <p>
     * https://en.wikipedia.org/wiki/International_Standard_Serial_Number
     */
    public static boolean isISSN(String gtin) {
        if (!isGTIN(gtin)) {
            return false;
        }
        if (gtin.length() == 14) {
            return gtin.startsWith("0977");
        }
        if (gtin.length() == 13) {
            return gtin.startsWith("977");
        }
        return false;
    }

    /**
     * Determines if a GTIN is an ISBN item.
     *
     * @link https://en.wikipedia.org/wiki/International_Standard_Book_Number
     */
    public static boolean isISBN(String gtin) {
        if (!isGTIN(gtin)) {
            return false;
        }
        if (gtin.length() == 14) {
            return gtin.startsWith("0978") || (gtin.startsWith("0979") && !gtin.startsWith("09790"));
        }
        if (gtin.length() == 13) {
            return gtin.startsWith("978") || (gtin.startsWith("979") && !gtin.startsWith("9790"));
        }
        return false;
    }

    /**
     * Determines if a GTIN is an ISMN item.
     *
     * @link https://en.wikipedia.org/wiki/International_Standard_Music_Number
     */
    public static boolean isISMN(String gtin) {
        if (!isGTIN(gtin)) {
            return false;
        }
        if (gtin.length() == 14) {
            return gtin.startsWith("09790");
        }
        if (gtin.length() == 13) {
            return gtin.startsWith("9790");
        }
        return false;
    }

    /**
     * Converts a GTIN to its GTIN-14 form.
     *
     * @throws IllegalArgumentException if the input string is not a GTIN
     */
    public static String toGTIN14(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        return leftPadWithZeroes(gtin, 14);
    }

    /**
     * Converts a GTIN to its GTIN-13 form.
     *
     * @throws IllegalArgumentException if the input string is not a GTIN or a GTIN-14 without a leading zero
     */
    public static String toGTIN13(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        if (gtin.length() > 13) {
            if (!startsWithNZeroes(gtin, gtin.length() - 13)) {
                throw new IllegalArgumentException("GTIN " + gtin + " could not be converted to GTIN-13");
            }
            return gtin.substring(gtin.length() - 13);
        }
        return leftPadWithZeroes(gtin, 13);
    }

    /**
     * Converts a GTIN to its GTIN-12 form.
     *
     * @throws IllegalArgumentException if the input string is not a GTIN or a GTIN-14 or GTIN-13 without leading zeros
     */
    public static String toGTIN12(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        if (gtin.length() > 12) {
            if (!startsWithNZeroes(gtin, gtin.length() - 12)) {
                throw new IllegalArgumentException("GTIN " + gtin + " could not be converted to GTIN-12");
            }
            return gtin.substring(gtin.length() - 12);
        }
        return leftPadWithZeroes(gtin, 12);
    }

    /**
     * Converts a GTIN to its GTIN-8 form.
     *
     * @throws IllegalArgumentException if the input string is not a GTIN or a GTIN-14, GTIN-13 or GTIN-12 without leading zeros
     */
    public static String toGTIN8(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        if (gtin.length() > 8) {
            if (!startsWithNZeroes(gtin, gtin.length() - 8)) {
                throw new IllegalArgumentException("GTIN " + gtin + " could not be converted to GTIN-8");
            }
            return gtin.substring(gtin.length() - 8);
        }
        return gtin;
    }

    /**
     * Determines if a GTIN can be converted to a GTIN-14 or is a GTIN-14 already.
     */
    public static boolean convertibleToGTIN14(String gtin) {
        return isGTIN(gtin);
    }

    /**
     * Determines if a GTIN can be converted to a GTIN-13 or is a GTIN-13 already.
     */
    public static boolean convertibleToGTIN13(String gtin) {
        return isGTIN(gtin) && (gtin.length() <= 13 || Internals.startsWithNZeroes(gtin, 1));
    }

    /**
     * Determines if a GTIN can be converted to a GTIN-12 or is a GTIN-12 already.
     */
    public static boolean convertibleToGTIN12(String gtin) {
        return isGTIN(gtin) && gtin.length() <= 12 || Internals.startsWithNZeroes(gtin, gtin.length() - 12);
    }

    /**
     * Determines if a GTIN can be converted to a GTIN-8 or is a GTIN-8 already.
     */
    public static boolean convertibleToGTIN8(String gtin) {
        return isGTIN(gtin) && gtin.length() == 8 || Internals.startsWithNZeroes(gtin, gtin.length() - 8);
    }

    /**
     * Shortens a GTIN to the shortest possible length.
     */
    public static String shorten(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        if (gtin.length() == 8) {
            return gtin;
        }
        int leadingZeroes = Internals.countLeadingZeroes(gtin);
        if (leadingZeroes >= gtin.length() - 8) {
            return gtin.substring(gtin.length() - 8, gtin.length());
        }
        if (gtin.length() == 12) {
            return gtin;
        }
        if (leadingZeroes >= gtin.length() - 12) {
            return gtin.substring(gtin.length() - 12, gtin.length());
        }
        if (gtin.length() == 13) {
            return gtin;
        }
        if (leadingZeroes >= 1) {
            return gtin.substring(1, gtin.length());
        }
        return gtin;
    }

    /**
     * Returns the normal form for a GTIN by shortening it to its shortest possible length and if it is a weight item
     * normalizes it by removing the weight or price. Does not validate the check digit and only recalculates it if it
     * is a weight item.
     */
    public static String normalize(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        if (isWeightItem(gtin)) {
            gtin = normalizeWeightItem(gtin);
        }
        return shorten(gtin);
    }

    public static List<String> allPossibleFormats(String gtin) {
        if (gtin == null) {
            return null;
        }
        validateFormat(gtin);
        ArrayList<String> formats = new ArrayList<String>(4);
        if (convertibleToGTIN8(gtin)) {
            formats.add(toGTIN8(gtin));
        }
        if (convertibleToGTIN12(gtin)) {
            formats.add(toGTIN12(gtin));
        }
        if (convertibleToGTIN13(gtin)) {
            formats.add(toGTIN13(gtin));
        }
        formats.add(toGTIN14(gtin));
        return formats;
    }

    /**
     * Determines if a GTIN is a variable weight GTIN with either weight or price.
     *
     * @link http://www.gs1.se/sv/vara-standarder/identifiera/Viktvarunummer/
     * @link http://www.gs1.se/globalassets/pub/artiklar_med_varierande_vikt.pdf
     */
    public static boolean isWeightItem(String gtin) {
        String s = getVariableWeightGTIN13(gtin);
        return s != null && (s.charAt(1) >= '0' && s.charAt(1) <= '5');
    }

    /**
     * Determines if a GTIN is a variable weight GTIN with price.
     */
    public static boolean isWeightItemWithPrice(String gtin) {
        String s = getVariableWeightGTIN13(gtin);
        return s != null && (s.charAt(1) >= '0' && s.charAt(1) <= '2');
    }

    /**
     * Determines if a GTIN is a variable weight GTIN with weight.
     */
    public static boolean isWeightItemWithWeight(String gtin) {
        String s = getVariableWeightGTIN13(gtin);
        return s != null && (s.charAt(1) >= '3' && s.charAt(1) <= '5');
    }

    /**
     * Extracts the price from a variable weight GTIN.
     *
     * @throws NullPointerException     if the GTIN is null
     * @throws IllegalArgumentException if the GTIN is null or not a variable weight GTIN with price
     */
    public static BigDecimal extractPriceFromWeightItem(String gtin) {
        validateFormat13or14(gtin);
        String s = getVariableWeightGTIN13(gtin);
        if (s == null || !(s.charAt(1) >= '0' && s.charAt(1) <= '2')) {
            throw new IllegalArgumentException("GTIN " + gtin + " is not a weight item with price");
        }
        int n = Integer.parseInt(s.substring(8, 12));
        if (s.charAt(1) == '0') {
            return new BigDecimal(n).movePointLeft(2);
        }
        if (s.charAt(1) == '1') {
            return new BigDecimal(n).movePointLeft(1).setScale(2, RoundingMode.UNNECESSARY);
        }
        return new BigDecimal(n).setScale(2, RoundingMode.UNNECESSARY);
    }

    /**
     * Extracts the weight in grams from a variable weight GTIN.
     *
     * @throws NullPointerException     if the GTIN is null
     * @throws IllegalArgumentException if the GTIN is null or not a variable weight GTIN with weight
     */
    public static int extractWeightFromWeightItem(String gtin) {
        validateFormat13or14(gtin);
        String s = getVariableWeightGTIN13(gtin);
        if (s == null || !(s.charAt(1) >= '3' && s.charAt(1) <= '5')) {
            throw new IllegalArgumentException("GTIN " + gtin + " is not a weight item with weight");
        }
        int n = Integer.parseInt(s.substring(8, 12));
        if (s.charAt(1) == '3') {
            return n;
        }
        if (s.charAt(1) == '4') {
            return n * 10;
        }
        return n * 100;
    }

    /**
     * Generates a matching GTIN without price or weight for variable weight GTIN with correct check digit. Preserves
     * the length of the GTIN.
     *
     * @throws NullPointerException     if the GTIN is null
     * @throws IllegalArgumentException if the GTIN is null or not a variable weight GTIN
     */
    public static String normalizeWeightItem(String gtin) {
        validateFormat13or14(gtin);
        if (isWeightItem(gtin)) {
            if (gtin.length() == 13) {
                return CheckDigit.calculateAndAppend(gtin.substring(0, 8) + "0000");
            }
            if (gtin.length() == 14) {
                return CheckDigit.calculateAndAppend(gtin.substring(0, 9) + "0000");
            }
        }
        throw new IllegalArgumentException("GTIN " + gtin + " is not a weight item");
    }

    private static String getVariableWeightGTIN13(String gtin) {
        if (!isGTIN(gtin)) {
            return null;
        }
        if (gtin.length() == 14 && gtin.startsWith("02")) {
            return gtin.substring(1);
        }
        if (gtin.length() == 13 && gtin.startsWith("2")) {
            return gtin;
        }
        return null;
    }

    private static void validateFormat13or14(String gtin) {
        if (gtin == null) {
            throw new NullPointerException("GTIN must not be null");
        }
        if (!isDigits(gtin)) {
            throw new IllegalArgumentException("Invalid GTIN " + gtin + ", must be digits");
        }
        if (gtin.length() != 14 && gtin.length() != 13) {
            throw new IllegalArgumentException("Invalid GTIN " + gtin + ", must be 13 or 14 digits long");
        }
    }
}
