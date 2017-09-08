# Utilities for GS1 barcodes
Java library for GS1 data structures commonly used in barcodes, such as GTIN, GLN, SSCC and element strings. Provides methods for validation and conversion between different length GTINs.

Provides parsing of element strings used in GS1-128 barcodes.

Supports variable measure GTINs as defined by GS1 Sweden in the (GTIN-14) 02- namespace.

The library is licensed under Apache License 2 and has no dependencies on other libraries.

## Global Trade Item Number (GTIN)

GTINs come in 4 lengths, GTIN-14, GTIN-13, GTIN-12 and GTIN-8, also known as UPC-A, EAN, UCC-8, UCC-13, ITF-14. The library has methods for identification of each length and conversion between them.

* Verification of either format or format and check digit.
* Identification of GTINs that are ISBN, ISSN, ISMN.
* Identification and value extraction from GS1 Sweden variable measure GTINs.
* Normalization of GS1 Sweden variable measure GTINs to their representative form without weight or price.
* Normalization of GTINs to their shortest representative form.

```java
// Identification and validation
GTIN.isGTIN("12345678")
GTIN.isGTIN("12345678901234")
GTIN.isGTIN14("12345678901234")
GTIN.isGTIN13("1234567890123")
GTIN.isGTIN12("123456789012")
GTIN.isGTIN8("12345678")
GTIN.isValid("12345678")  // checks both format and check digit
GTIN.validateFormat("12345678") // throws exception if format is invalid
GTIN.validateFormatAndCheckDigit("12345678") // throws exception if format or check digit invalid
```

```java
// Conversion
GTIN.toGTIN14("12345678")
GTIN.toGTIN13("12345678")
GTIN.toGTIN12("12345678")
GTIN.toGTIN8("12345678")
GTIN.convertibleToGTIN14("12345678")
GTIN.convertibleToGTIN13("12345678")
GTIN.convertibleToGTIN12("12345678")
GTIN.convertibleToGTIN8("12345678")
GTIN.shorten("000012345678") // returns "12345678"
GTIN.normalize("000012345678") // returns "12345678"
```

```java
// GS1 Sweden variable measure item GTINs
GTIN.isVariableMeasureItem("2388060112344")
GTIN.isVariableMeasureItemWithPrice("2088060112344")
GTIN.isVariableMeasureItemWithWeight("2388060112344")
GTIN.extractPriceFromVariableMeasureItem("02188060112344") // returns 123.40
GTIN.extractWeightFromVariableMeasureItem("02388060112344") // returns 1234
GTIN.normalizeVariableMeasureItem("2388060112344") // returns "2388060100006"
```

```java
// Identification of ISBN, ISSN, and ISMN numbers
GTIN.isISBN("9799137138114")
GTIN.isISSN("09772049363002")
GTIN.isISMN("9790137138114")
```

## Global Location Number (GLN)

GLNs are 13 digit long numbers used to identify a specific physical or logical location.

```java
GLN.isGLN("7594567000014")
GLN.isValid("7594567000014")
GLN.validateFormat("7594567000014") // throws exception if format is not valid
GLN.validateFormatAndCheckDigit("7594567000014") // throws exception if format or check digit invalid
```

## Serial Shipping Container Code (SSCC)

SSCCs are 18 digit numbers used to identify logistics units.

```java
SSCC.isSSCC("106141411234567897")
SSCC.isValid("106141411234567897")
SSCC.validateFormat("106141411234567897") // throws exception if format is not valid
SSCC.validateFormatAndCheckDigit("106141411234567897") // throws exception if format or check digit invalid
```

### Element strings (GS1-128)

Element strings are the data structure used by GS1-128 barcodes and contain a sequence of key-value pairs. The keys are called application identifiers. The library provides support for parsing these sequences.

Values of variable length must be separated using the ascii character 29 (hex 1D).

This example parses the GS1-128 printed as: `(01)97311876341811(3103)007520(15)170809`.

```java
ElementStrings.ParseResult result = ElementStrings.parse("0197311876341811310300752015170809");
result.isPartial() // true if the entirety of the strings could not be parsed
result.getErrorMessage() // in case of a partial parse results this describes the error encountered
result.getString(ApplicationIdentifier.GTIN) // returns "97311876341811"
result.getDecimal(ApplicationIdentifier.ITEM_NET_WEIGHT_KG) // returns 7.520 (BigDecimal)
result.getDate(ApplicationIdentifier.BEST_BEFORE_DATE) // 2017-08-09 (java.util.Date)
```

## Check digits

The CheckDigit class provides methods for calculation and validation of check digits.

```java
CheckDigit.calculate("400638133393") // returns '1'
CheckDigit.calculateAndAppend("400638133393") // returns "4006381333931"
CheckDigit.recalculate("4006381333930") // returns '1'
CheckDigit.recalculateAndApply("4006381333930") // returns "4006381333931"
CheckDigit.isValid("4006381333930")
CheckDigit.validate("4006381333930") // throws exception if check digit is incorrect
```
