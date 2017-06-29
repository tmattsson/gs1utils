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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ElementStringsTests {

    private static final String SIMPLE_SSCC = "00106141411234567897";
    private static final String SIMPLE_GCN = "2551234567890123";
    private static final String SIMPLE_BATCH = "10123456";

    @Test
    public void testParseThrowsNPEOnNullSequence() {
        try {
            ElementStrings.parse(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("Sequence must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseReturnsEmptyNonPartialResultOnEmptyString() {
        ElementStrings.ParseResult result = ElementStrings.parse("");
        assertFalse(result.isPartial());
        assertTrue(result.isEmpty());
        assertNull(result.getErrorMessage());
    }

    @Test
    public void testParseFailsOnLeadingWhitespace() {
        ElementStrings.ParseResult result = ElementStrings.parse(" " + SIMPLE_SSCC);
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Unrecognized AI at position 0", result.getErrorMessage());
    }

    @Test
    public void testParseIgnoresSeparatorCharacterAtEndOfSequence() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_SSCC + "\u001d");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertNotNull(result.getObject(ApplicationIdentifier.SSCC.getKey()));
        assertNotNull(result.getObject(ApplicationIdentifier.SSCC));
        assertNull(result.getErrorMessage());
    }

    @Test
    public void testParseNumericFixed() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_SSCC);
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals("106141411234567897", result.getString(ApplicationIdentifier.SSCC));
        assertEquals("106141411234567897", result.getString(ApplicationIdentifier.SSCC.getKey()));
        assertEquals(1, result.getElementsByString().size());
        assertEquals(1, result.getElementsByEnum().size());
    }

    @Test
    public void testParseFailsWhenNumericFixedTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_SSCC.substring(0, 10));
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 00 at position 0, data field must be exactly 18 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseFailWhenNumericFixedHasEmptyDataField() {
        ElementStrings.ParseResult result = ElementStrings.parse("00");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 00 at position 0, data field must be exactly 18 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseFailsWhenNumericFixedNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_SSCC.substring(0, SIMPLE_SSCC.length() - 2) + "AB");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 00 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseNumericVariable() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_GCN);
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.GCN));
        assertTrue(result.contains(ApplicationIdentifier.GCN.getKey()));
        assertEquals("1234567890123", result.getString(ApplicationIdentifier.GCN));
    }

    @Test
    public void testParseFailsWhenNumericVariableTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_GCN.substring(0, SIMPLE_GCN.length() - 1));
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertFalse(result.contains(ApplicationIdentifier.GCN));
        assertFalse(result.contains(ApplicationIdentifier.GCN.getKey()));
        assertEquals("Error parsing data field for AI 255 at position 0, data field must be at least 13 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseFailsWhenNumericVariableNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_GCN.substring(0, SIMPLE_GCN.length() - 2) + "AB");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 255 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseAlphanumericVariable() {
        ElementStrings.ParseResult result = ElementStrings.parse("10123456");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertTrue(result.contains(ApplicationIdentifier.BATCH_OR_LOT_NUMBER.getKey()));
        assertEquals("123456", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
    }

    @Test
    public void testParseMultipleVariable() {
        ElementStrings.ParseResult result = ElementStrings.parse(SIMPLE_BATCH + '\u001D' + SIMPLE_GCN);
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertTrue(result.contains(ApplicationIdentifier.BATCH_OR_LOT_NUMBER.getKey()));
        assertTrue(result.contains(ApplicationIdentifier.GCN));
        assertEquals("123456", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertEquals("1234567890123", result.getString(ApplicationIdentifier.GCN));
    }

    @Test
    public void testParseDecimal() {
        ElementStrings.ParseResult result = ElementStrings.parse("3103123456");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertEquals(new BigDecimal("123.456"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertEquals(new BigDecimal("123.456"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG.getKey()));
    }

    @Test
    public void testParseDecimalWithZeroDecimalPointIndicator() {
        ElementStrings.ParseResult result = ElementStrings.parse("3100123456");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertEquals(new BigDecimal("123456"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
    }

    @Test
    public void testParseVariableLengthDecimal() {
        ElementStrings.ParseResult result = ElementStrings.parse("3902789");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.AMOUNT_PAYABLE));
        assertEquals(new BigDecimal("7.89"), result.getDecimal(ApplicationIdentifier.AMOUNT_PAYABLE));
    }

    @Test
    public void testParseDecimalFailsWhenDecimalPointIndicatorNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("310A123456");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 310 at position 0, decimal point indicator must be a digit", result.getErrorMessage());
    }

    @Test
    public void testParseDecimalFailsWhenNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("310312345X");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 310 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseDate() {
        ElementStrings.ParseResult result = ElementStrings.parse("15170501");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertDate("2017-05-01", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertDate("2017-05-01", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE.getKey()));
    }

    @Test
    public void testParseDateInLastCentury() {
        ElementStrings.ParseResult result = ElementStrings.parse("15990501");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertDate("1999-05-01", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
    }

    @Test
    public void testParseDateFailsWhenTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse("151705");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 15 at position 0, data field must be exactly 6 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseDateFailsWhenNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("151705XX");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 15 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseDateFailsWhenDateIsInvalid() {
        ElementStrings.ParseResult result = ElementStrings.parse("15170229");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 15 at position 0, invalid date", result.getErrorMessage());
    }

    @Test
    public void testParseDateFailsWhenDateIsNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("1517060X");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 15 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseDateTreatsDayZeroAsEndOfMonth() {
        ElementStrings.ParseResult result = ElementStrings.parse("15170400");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertDate("2017-04-30", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
    }

    @Test
    public void testParseDateCalculatesCentury() {
        ElementStrings.ParseResult result = ElementStrings.parse("15990601");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertDate("1999-06-01", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
    }

    @Test
    public void testParseDateRange() {
        ElementStrings.ParseResult result = ElementStrings.parse("7007170522170702");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.HARVEST_DATE);
        assertEquals(2, list.size());
        assertDate("2017-05-22", (Date) list.get(0));
        assertDate("2017-07-02", (Date) list.get(1));
    }

    @Test
    public void testParseDateRangeWithSingleDate() {
        ElementStrings.ParseResult result = ElementStrings.parse("7007170522");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.HARVEST_DATE.getKey());
        assertEquals(1, list.size());
        assertDate("2017-05-22", (Date) list.get(0));
    }

    @Test
    public void testParseDateRangeFailsWhenFieldTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse("70071705");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 7007 at position 0, data field must be at least 6 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseDateRangeFailsWhenFieldOfInvalidLength() {
        ElementStrings.ParseResult result = ElementStrings.parse("700717052211");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 7007 at position 0, invalid data field length", result.getErrorMessage());
    }

    @Test
    public void testParseDateAndTimeWithoutSeconds() {
        ElementStrings.ParseResult result = ElementStrings.parse("70031705011208");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        Date date = result.getDate(ApplicationIdentifier.EXPIRATION_DATE_AND_TIME);
        assertDateTime("2017-05-01 12:08:00", date);
    }

    @Test
    public void testParseDateAndTimeWithoutSecondsFailsWhenTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse("700317050112");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 7003 at position 0, data field must be exactly 10 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseDateAndTimeWithoutSecondsFailsWhenNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("700317050112XX");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 7003 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseDateAndTimeWithOptionalMinutesAndSeconds() {
        ElementStrings.ParseResult result = ElementStrings.parse("800817050112");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertDateTime("2017-05-01 12:00:00", result.getDate(ApplicationIdentifier.PRODUCTION_DATE_AND_TIME));
    }

    @Test
    public void testParseDateAndTimeWithOptionalMinutesAndSecondsPresent() {
        ElementStrings.ParseResult result = ElementStrings.parse("8008170501123045");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertDateTime("2017-05-01 12:30:45", result.getDate(ApplicationIdentifier.PRODUCTION_DATE_AND_TIME));
    }

    @Test
    public void testParseDateAndTimeWithOptionalMinutesAndSecondsFailsWhenNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("8008170501XX");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 8008 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseDateAndTimeWithOptionalMinutesAndSecondsFailsWhenTooShort() {
        ElementStrings.ParseResult result = ElementStrings.parse("8008170501");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 8008 at position 0, data field must be at least 8 characters long", result.getErrorMessage());
    }

    @Test
    public void testParseDateAndTimeWithOptionalMinutesAndSecondsFailsWhenInvalidLength() {
        ElementStrings.ParseResult result = ElementStrings.parse("80081705011230");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 8008 at position 0, invalid data field length", result.getErrorMessage());
    }

    @Test
    public void testParseAmountPayableWithCurrency() {
        ElementStrings.ParseResult result = ElementStrings.parse("391212345678");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.AMOUNT_PAYABLE_WITH_CURRENCY);
        assertEquals(2, list.size());
        assertEquals("123", list.get(0));
        assertEquals(new BigDecimal("456.78"), list.get(1));
    }

    @Test
    public void testParseAmountPayablePerSingleItemWithCurrency() {
        ElementStrings.ParseResult result = ElementStrings.parse("393212345678");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.AMOUNT_PAYABLE_PER_SINGLE_ITEM_WITH_CURRENCY);
        assertEquals(2, list.size());
        assertEquals("123", list.get(0));
        assertEquals(new BigDecimal("456.78"), list.get(1));
    }

    @Test
    public void testParseShipToPostalCodeWithCountry() {
        ElementStrings.ParseResult result = ElementStrings.parse("421123ABCDEF");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.SHIP_TO_POSTAL_CODE_WITH_COUNTRY);
        assertEquals(2, list.size());
        assertEquals("123", list.get(0));
        assertEquals("ABCDEF", list.get(1));
    }

    @Test
    public void testParseCountryOfInitialProcessing() {
        ElementStrings.ParseResult result = ElementStrings.parse("423111222333");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.COUNTRY_OF_INITIAL_PROCESSING);
        assertEquals(3, list.size());
        assertEquals("111", list.get(0));
        assertEquals("222", list.get(1));
        assertEquals("333", list.get(2));
    }

    @Test
    public void testParseCountryOfInitialProcessingOfInvalidLength() {
        ElementStrings.ParseResult result = ElementStrings.parse("42311122233");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 423 at position 0, invalid data field length", result.getErrorMessage());
    }

    @Test
    public void testParseCountryOfInitialProcessingNotNumeric() {
        ElementStrings.ParseResult result = ElementStrings.parse("42311122233X");
        assertTrue(result.isPartial());
        assertTrue(result.isEmpty());
        assertEquals("Error parsing data field for AI 423 at position 0, data field must be numeric", result.getErrorMessage());
    }

    @Test
    public void testParseCountryOfDisassembly() {
        ElementStrings.ParseResult result = ElementStrings.parse("425111222333");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList(ApplicationIdentifier.COUNTRY_OF_DISASSEMBLY);
        assertEquals(3, list.size());
        assertEquals("111", list.get(0));
        assertEquals("222", list.get(1));
        assertEquals("333", list.get(2));
    }

    @Test
    public void testParseNumberOfProcessor() {
        ElementStrings.ParseResult result = ElementStrings.parse("7030111ABCDEF\u001d7039222XYZ");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        List list = result.getList("7030");
        assertEquals(2, list.size());
        assertEquals("111", list.get(0));
        assertEquals("ABCDEF", list.get(1));
        list = result.getList("7039");
        assertEquals(2, list.size());
        assertEquals("222", list.get(0));
        assertEquals("XYZ", list.get(1));
    }

    @Test
    public void testParseNhrn() {
        ElementStrings.ParseResult result = ElementStrings.parse("710ABC\u001d719XYZ");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals("ABC", result.getString("710"));
        assertEquals("XYZ", result.getString("719"));
    }

    @Test
    public void testParseCompanyInternalInformation() {
        ElementStrings.ParseResult result = ElementStrings.parse("91ABC\u001d99XYZ");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals("ABC", result.getString("91"));
        assertEquals("XYZ", result.getString("99"));
    }

    @Test
    public void testParseRealWorldExample1() {
        ElementStrings.ParseResult result = ElementStrings.parse("011730032756000410170522243\u001D17170619");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(3, result.getElementsByString().size());
        assertEquals("17300327560004", result.getString(ApplicationIdentifier.GTIN));
        assertEquals("170522243", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertDate("2017-06-19", result.getDate(ApplicationIdentifier.EXPIRATION_DATE));
    }

    @Test
    public void testParseRealWorldExample2() {
        ElementStrings.ParseResult result = ElementStrings.parse("019739372006134231020007141517071010170411\u001D9010");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(5, result.getElementsByString().size());
        assertEquals("97393720061342", result.getString(ApplicationIdentifier.GTIN));
        assertEquals(new BigDecimal("7.14"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertDate("2017-07-10", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertEquals("170411", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertEquals("10", result.getString(ApplicationIdentifier.MUTUALLY_AGREED_INFORMATION));
    }

    @Test
    public void testParseRealWorldExample3() {
        ElementStrings.ParseResult result = ElementStrings.parse("011739119609790615170612310300120080050000001001714362");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(5, result.getElementsByString().size());
        assertEquals("17391196097906", result.getString(ApplicationIdentifier.GTIN));
        assertDate("2017-06-12", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertEquals(new BigDecimal("1.200"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertEquals("000000", result.getString(ApplicationIdentifier.PRICE_PER_UNIT));
        assertEquals("01714362", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
    }

    @Test
    public void testParseRealWorldExample4() {
        ElementStrings.ParseResult result = ElementStrings.parse("0197311876341811310300752015170809");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(3, result.getElementsByString().size());
        assertEquals("97311876341811", result.getString(ApplicationIdentifier.GTIN));
        assertEquals(new BigDecimal("7.520"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertDate("2017-08-09", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
    }

    @Test
    public void testParseRealWorldExample5() {
        ElementStrings.ParseResult result = ElementStrings.parse("01173500371918073103003840151706171000013115");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(4, result.getElementsByString().size());
        assertEquals("17350037191807", result.getString(ApplicationIdentifier.GTIN));
        assertEquals(new BigDecimal("3.840"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertDate("2017-06-17", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertEquals("00013115", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
    }

    @Test
    public void testParseRealWorldExample6() {
        ElementStrings.ParseResult result = ElementStrings.parse("0197300328121002310300226010170529038\u001D17170608");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(4, result.getElementsByString().size());
        assertEquals("97300328121002", result.getString(ApplicationIdentifier.GTIN));
        assertEquals(new BigDecimal("2.260"), result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG));
        assertEquals("170529038", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
        assertDate("2017-06-08", result.getDate(ApplicationIdentifier.EXPIRATION_DATE));
    }

    @Test
    public void testParseRealWorldExample7() {
        ElementStrings.ParseResult result = ElementStrings.parse("011730020641446615170628131705081017050838");
        assertFalse(result.isPartial());
        assertFalse(result.isEmpty());
        assertEquals(4, result.getElementsByString().size());
        assertEquals("17300206414466", result.getString(ApplicationIdentifier.GTIN));
        assertDate("2017-06-28", result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE));
        assertDate("2017-05-08", result.getDate(ApplicationIdentifier.PACKAGING_DATE));
        assertEquals("17050838", result.getString(ApplicationIdentifier.BATCH_OR_LOT_NUMBER));
    }

    private void assertDate(String expected, Date actual) {
        assertEquals(expected, new SimpleDateFormat("yyy-MM-dd").format(actual));
    }

    private void assertDateTime(String expected, Date actual) {
        assertEquals(expected, new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(actual));
    }
}
