# Utilities for GS1 GTIN GLN and SSCC codes
Java library for GS1 data structures such as GTIN, GLN and SSCC commonly used in barcodes. Provides methods for validation and conversion between different length GTINs.

Supports variable weight GTINs as defined by GS1 Sweden in the (GTIN-14) 02- namespace.

The library is licensed under Apache License 2 and has no dependencies on other libraries.

## Global Trade Item Number (GTIN)

GTINs come in 4 lengths, GTIN-14, GTIN-13, GTIN-12 and GTIN-8, also known as UPC-A, EAN, UCC-8, UCC-13, ITF-14. The library has methods for identification of each length and conversion between them.

* Verification of either format or format and check digit.
* Identification of GTINs that are ISBN, ISSN, ISMN.
* Identification and value extraction from GS1 Sweden weight item GTINs.
* Canonicalization of GS1 Sweden weight item GTINs to their representative form without weight/price.
* Canonicalization of GTINs to their shortest canonical form.

```java
// Identification and validation
GTIN.isGTIN("12345678")
GTIN.isGTIN("12345678901234")
GTIN.isGTIN14("12345678901234")
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
GTIN.canBecomeGTIN14("12345678")
GTIN.canBecomeGTIN13("12345678")
GTIN.canBecomeGTIN12("12345678")
GTIN.canBecomeGTIN8("12345678")
GTIN.shorten("000012345678") // returns "12345678"
GTIN.canonicalize("000012345678") // returns "12345678"
```

```java
// GS1 Sweden variable weight item GTINs
GTIN.isWeightItem("2388060112344")
GTIN.isWeightItemWithPrice("2088060112344")
GTIN.isWeightItemWithWeight("2388060112344")
GTIN.getPriceFromWeightItem("02188060112344") // returns 123.40
GTIN.getWeightFromWeightItem("02388060112344") // returns 1234
GTIN.canonicalizeWeightItem("2388060112344") // returns "2388060100006"
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
