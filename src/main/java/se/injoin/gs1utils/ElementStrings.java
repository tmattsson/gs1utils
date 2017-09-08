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
import java.util.*;

/**
 * Parses GS1 element strings as found in data carriers such as GS1-128.
 */
public class ElementStrings {

    public static class ParseResult {

        private boolean partial = false;
        private String errorMessage = null;

        private Map<String, Object> elementsByString = new LinkedHashMap<String, Object>();
        private Map<ApplicationIdentifier, Object> elementsByEnum = new LinkedHashMap<ApplicationIdentifier, Object>();

        public boolean contains(String key) {
            return elementsByString.containsKey(key);
        }

        public boolean contains(ApplicationIdentifier identifier) {
            return elementsByEnum.containsKey(identifier);
        }

        public String getString(ApplicationIdentifier identifier) {
            return (String) elementsByEnum.get(identifier);
        }

        public String getString(String key) {
            return (String) elementsByString.get(key);
        }

        public Date getDate(ApplicationIdentifier identifier) {
            return (Date) elementsByEnum.get(identifier);
        }

        public Date getDate(String key) {
            return (Date) elementsByString.get(key);
        }

        public BigDecimal getDecimal(ApplicationIdentifier identifier) {
            return (BigDecimal) elementsByEnum.get(identifier);
        }

        public BigDecimal getDecimal(String key) {
            return (BigDecimal) elementsByString.get(key);
        }

        public List getList(ApplicationIdentifier identifier) {
            return (List) elementsByEnum.get(identifier);
        }

        public List getList(String key) {
            return (List) elementsByString.get(key);
        }

        public Object getObject(String key) {
            return elementsByString.get(key);
        }

        public Object getObject(ApplicationIdentifier identifier) {
            return elementsByEnum.get(identifier);
        }

        public boolean isEmpty() {
            return elementsByString.isEmpty();
        }

        public boolean isPartial() {
            return partial;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Map<String, Object> getElementsByString() {
            return elementsByString;
        }

        public Map<ApplicationIdentifier, Object> getElementsByEnum() {
            return elementsByEnum;
        }
    }

    /**
     * Parses an element strings and returns a parse result. On error returns a partial parse result containing what
     * could be successfully parsed and an error string describing what went wrong and at what position in the sequence
     * the error occurred. The position reported is zero-based.
     */
    public static ParseResult parse(String sequence) {

        if (sequence == null) {
            throw new NullPointerException("Sequence must not be null");
        }

        ParseResult result = new ParseResult();
        SequenceReader reader = new SequenceReader(sequence);

        while (!(reader.remainingLength() == 0)) {

            int identifierPosition = reader.getPosition();
            String key = null;
            Object data = null;
            ApplicationIdentifier identifier = null;

            try {

                // Standard AIs defined in ApplicationIdentifier
                for (ApplicationIdentifier candidate : ApplicationIdentifier.values()) {
                    if (reader.startsWith(candidate.getKey())) {
                        identifier = candidate;
                        key = reader.read(identifier.getKey().length());
                        if (identifier.getFormat() == ApplicationIdentifier.Format.CUSTOM) {
                            data = readDataFieldInCustomFormat(identifier, reader);
                        } else {
                            data = readDataFieldInStandardFormat(identifier, reader);
                        }
                        break;
                    }
                }

                // Support for AIs 703s Number of processor with three-digit ISO country code
                if (key == null && reader.remainingLength() >= 4 && reader.startsWith("703")) {
                    String start = reader.peek(4);
                    if (start.charAt(3) >= '0' && start.charAt(3) <= '9') {
                        key = reader.read(4);
                        data = Arrays.asList(reader.readFixedLengthNumeric(3), reader.readVariableLengthAlphanumeric(1, 27));
                    }
                }

                // Support for AIs 710-719 National Healthcare Reimbursement Number (NHRN)
                if (key == null && reader.remainingLength() >= 3 && reader.startsWith("71")) {
                    String start = reader.peek(3);
                    if (start.charAt(2) >= '0' && start.charAt(2) <= '9') {
                        key = reader.read(3);
                        data = reader.readVariableLengthAlphanumeric(1, 20);
                    }
                }

                // Support for AIs 91-99 Company internal information
                if (key == null && reader.remainingLength() >= 2 && reader.startsWith("9")) {
                    String start = reader.peek(2);
                    if (start.charAt(1) >= '1' && start.charAt(1) <= '9') {
                        key = reader.read(2);
                        data = reader.readVariableLengthAlphanumeric(1, 30);
                    }
                }

            } catch (Exception e) {
                result.partial = true;
                result.errorMessage = "Error parsing data field for AI " + key + " at position " + identifierPosition + ", " + e.getMessage();
                break;
            }
            if (key == null) {
                result.partial = true;
                result.errorMessage = "Unrecognized AI at position " + identifierPosition;
                break;
            }
            if (data == null) {
                result.partial = true;
                result.errorMessage = "Error parsing data field for AI " + key + " at position " + identifierPosition;
                break;
            }
            result.elementsByString.put(key, data);
            if (identifier != null) {
                result.elementsByEnum.put(identifier, data);
            }
        }

        return result;
    }

    private static Object readDataFieldInStandardFormat(ApplicationIdentifier identifier, SequenceReader reader) {
        switch (identifier.getFormat()) {
            case NUMERIC_FIXED:
                return reader.readFixedLengthNumeric(identifier.getMaxLength());
            case NUMERIC_VARIABLE:
                return reader.readVariableLengthNumeric(identifier.getMinLength(), identifier.getMaxLength());
            case ALPHANUMERIC_FIXED:
                return reader.readFixedLengthAlphanumeric(identifier.getMaxLength());
            case ALPHANUMERIC_VARIABLE:
                return reader.readVariableLengthAlphanumeric(identifier.getMinLength(), identifier.getMaxLength());
            case DECIMAL:
                return reader.readDecimal(identifier.getMinLength(), identifier.getMaxLength());
            case DATE:
                return reader.readDate();
        }
        return null;
    }

    private static Object readDataFieldInCustomFormat(ApplicationIdentifier identifier, SequenceReader reader) {
        switch (identifier) {
            case AMOUNT_PAYABLE_WITH_CURRENCY:
            case AMOUNT_PAYABLE_PER_SINGLE_ITEM_WITH_CURRENCY:
                return reader.readCurrencyAndAmount();
            case SHIP_TO_POSTAL_CODE_WITH_COUNTRY: {
                String countryCode = reader.readNumericDataField(3, 3);
                String postalCode = reader.readDataField(1, 9);
                return Arrays.asList(countryCode, postalCode);
            }
            case COUNTRY_OF_INITIAL_PROCESSING:
            case COUNTRY_OF_DISASSEMBLY:
                return reader.readCountryList();
            case EXPIRATION_DATE_AND_TIME:
                return reader.readDateAndTimeWithoutSeconds();
            case HARVEST_DATE:
                return reader.readDateOrDateRange();
            case PRODUCTION_DATE_AND_TIME:
                return reader.readDateAndTimeWithOptionalMinutesAndSeconds();
        }
        return null;
    }

    static class SequenceReader {

        private static final char SEPARATOR_CHAR = 0x1D;

        private String sequence;
        private int position = 0;

        SequenceReader(String sequence) {
            this.sequence = sequence;
        }

        String readFixedLengthNumeric(int length) {
            return readVariableLengthNumeric(length, length);
        }

        String readVariableLengthNumeric(int minLength, int maxLength) {
            String dataField = readNumericDataField(minLength, maxLength);
            skipSeparatorIfPresent();
            return dataField;
        }

        String readFixedLengthAlphanumeric(int length) {
            return readVariableLengthAlphanumeric(length, length);
        }

        String readVariableLengthAlphanumeric(int minLength, int maxLength) {
            String dataField = readDataField(minLength, maxLength);
            skipSeparatorIfPresent();
            return dataField;
        }

        Date readDate() {
            try {
                return parseDateAndTime(readNumericDataField(6, 6));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("data field must be numeric");
            }
        }

        List readDateOrDateRange() {
            String dataField = readNumericDataField(6, 12);
            if (dataField.length() == 6) {
                return Collections.singletonList(parseDateAndTime(dataField.substring(0, 6)));
            } else if (dataField.length() == 12) {
                Date first = parseDateAndTime(dataField.substring(0, 6));
                Date second = parseDateAndTime(dataField.substring(6, 12));
                return Arrays.asList(first, second);
            }
            throw new IllegalArgumentException("invalid data field length");
        }

        Date readDateAndTimeWithoutSeconds() {
            try {
                return parseDateAndTime(readNumericDataField(10, 10));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("data field must be numeric");
            }
        }

        Date readDateAndTimeWithOptionalMinutesAndSeconds() {
            try {
                String dataField = readNumericDataField(8, 12);
                if (dataField.length() != 8 && dataField.length() != 12) {
                    throw new IllegalArgumentException("invalid data field length");
                }
                return parseDateAndTime(dataField);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("data field must be numeric");
            }
        }

        Date parseDateAndTime(String s) {
            int year, month, day, hour, minutes, seconds;
            try {
                year = Integer.parseInt(s.substring(0, 2));
                month = Integer.parseInt(s.substring(2, 4));
                day = Integer.parseInt(s.substring(4, 6));
                hour = s.length() >= 8 ? Integer.parseInt(s.substring(6, 8)) : 0;
                minutes = s.length() >= 10 ? Integer.parseInt(s.substring(8, 10)) : 0;
                seconds = s.length() >= 12 ? Integer.parseInt(s.substring(10, 12)) : 0;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("data field must be numeric");
            }
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setLenient(false);
                calendar.clear();
                // Calculate the century to use using the algorithm in GS1 General specification section 7.12.
                int century;
                int currentYear = calendar.get(Calendar.YEAR);
                int currentCentury = currentYear - (currentYear % 100);
                int x = year - (currentYear % 100);
                if (x >= 51 && x <= 99) {
                    century = currentCentury - 100;
                } else if (x >= -99 && x <= -50) {
                    century = currentCentury + 100;
                } else {
                    century = currentCentury;
                }
                // When day is zero that means last day of the month
                boolean lastOfMonth = day == 0;
                day = day == 0 ? 1 : day;
                calendar.set(century + year, month - 1, day, hour, minutes, seconds);
                if (lastOfMonth) {
                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                return calendar.getTime();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("invalid date");
            }
        }

        BigDecimal readDecimal(int minLength, int maxLength) {
            try {
                int decimalPointPosition = readDecimalPointPosition();
                String dataField = readNumericDataField(minLength, maxLength);
                return new BigDecimal(dataField).movePointLeft(decimalPointPosition);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("invalid decimal");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("data field must be numeric");
            }
        }

        int readDecimalPointPosition() {
            char decimalPointIndicator = readChar();
            if (decimalPointIndicator < '0' || decimalPointIndicator > '9') {
                throw new IllegalArgumentException("decimal point indicator must be a digit");
            }
            return decimalPointIndicator - '0';
        }

        List readCurrencyAndAmount() {
            int decimalPointPosition = readDecimalPointPosition();
            String currencyCode = readNumericDataField(3, 3);
            String digits = readDataField(1, 15);
            BigDecimal amount = new BigDecimal(digits).movePointLeft(decimalPointPosition);
            return Arrays.asList(currencyCode, amount);
        }

        List readCountryList() {
            String dataField = readNumericDataField(3, 15);
            if (dataField.length() % 3 != 0) {
                throw new IllegalArgumentException("invalid data field length");
            }
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < dataField.length(); i += 3) {
                list.add(dataField.substring(i, i + 3));
            }
            return list;
        }

        String readNumericDataField(int minLength, int maxLength) {
            String dataField = readDataField(minLength, maxLength);
            if (!Internals.isDigits(dataField)) {
                throw new IllegalArgumentException("data field must be numeric");
            }
            return dataField;
        }

        String readDataField(int minLength, int maxLength) {
            int length = 0;
            int endIndex = position;
            while (length < maxLength && endIndex < sequence.length()) {
                if (sequence.charAt(endIndex) == SEPARATOR_CHAR) {
                    break;
                }
                endIndex++;
                length++;
            }
            if (length < minLength && minLength == maxLength) {
                throw new IllegalArgumentException("data field must be exactly " + minLength + " characters long");
            }
            if (length < minLength) {
                throw new IllegalArgumentException("data field must be at least " + minLength + " characters long");
            }
            String dataField = sequence.substring(position, endIndex);
            position = endIndex;
            return dataField;
        }

        void skipSeparatorIfPresent() {
            if (position < sequence.length() && sequence.charAt(position) == SEPARATOR_CHAR) {
                position++;
            }
        }

        int remainingLength() {
            return sequence.length() - position;
        }

        int getPosition() {
            return position;
        }

        boolean startsWith(String prefix) {
            return sequence.startsWith(prefix, position);
        }

        char readChar() {
            return sequence.charAt(position++);
        }

        String read(int length) {
            String s = peek(length);
            position += length;
            return s;
        }

        String peek(int length) {
            return sequence.substring(position, position + length);
        }
    }
}
