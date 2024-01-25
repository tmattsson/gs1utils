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
 * Based on GS1 General Specifications, Release 17.0.1.
 * <p>
 * http://www.gs1.org/sites/default/files/docs/barcodes/GS1_General_Specifications.pdf
 */
public enum ApplicationIdentifier {

	/**
	 * Identification of a logistic unit (SSCC).
	 */
	SSCC("00", Format.NUMERIC_FIXED, 18),

	/**
	 * Identification of a trade item (GTIN).
	 */
	GTIN("01", Format.NUMERIC_FIXED, 14),

	/**
	 * Identification of trade items contained in a logistic unit. The GTIN of the trade items contained is the GTIN of
	 * the highest level of trade item contained in the logistic unit.
	 *
	 * @see ApplicationIdentifier#COUNT_OF_TRADE_ITEMS
	 */
	CONTAINED_GTIN("02", Format.NUMERIC_FIXED, 14),

	/**
	 * Batch or lot number, associates an item with information the manufacturer considers relevant for traceability of
	 * the trade item to which the element string is applied. The data may refer to the trade item itself or to items
	 * contained.
	 */
	BATCH_OR_LOT_NUMBER("10", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Production date, the production or assembly date determined by the manufacturer. The date may refer to the trade
	 * item itself or to items contained.
	 */
	PRODUCTION_DATE("11", Format.DATE),

	/**
	 * Due date for amount on payment slip.
	 *
	 * @see ApplicationIdentifier#PAYMENT_SLIP_REFERENCE_NUMBER
	 * @see ApplicationIdentifier#INVOICING_PARTY
	 */
	DUE_DATE("12", Format.DATE),

	/**
	 * Packaging date, the date when the goods were packed as determined by the packager. The date may refer to the
	 * trade item itself or to items contained.
	 */
	PACKAGING_DATE("13", Format.DATE),

	/**
	 * Best before date on the label or package signifies the end of the period under which the product will retain
	 * specific quality attributes or claims even though the product may continue to retain positive quality attributes
	 * after this date.
	 */
	BEST_BEFORE_DATE("15", Format.DATE),

	/**
	 * Sell by date, specified by the manufacturer as the last date the retailer is to offer the product for sale to the
	 * consumer.
	 */
	SELL_BY_DATE("16", Format.DATE),

	/**
	 * Expiration date, determines the limit of consumption or use of a product / coupon.
	 */
	EXPIRATION_DATE("17", Format.DATE),

	/**
	 * Product variant, distinguished a variant from the usual item if the variation is not sufficiently significant to
	 * require a separate Global Trade Item Number (GTIN) and the variation is relevant only to the brand owner and any
	 * third party acting on its behalf.
	 */
	VARIANT_NUMBER("20", Format.NUMERIC_FIXED, 2),

	/**
	 * Serial number, assigned to an entity for its lifetime. When combined with a GTIN, a serial number uniquely
	 * identifies an individual item.
	 */
	SERIAL_NUMBER("21", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Additional product identification assigned by the manufacturer.
	 */
	ADDITIONAL_ITEM_ID("240", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Customer part number.
	 */
	CUSTOMER_PART_NUMBER("241", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Made-to-Order variation number, provides the additional data needed to uniquely identify a custom trade item.
	 */
	MADE_TO_ORDER_VARIATION_NUMBER("242", Format.NUMERIC_VARIABLE, 6),

	/**
	 * Packaging component number, assigned to the packaging component for its lifetime. When associated with a GTIN, a
	 * PCN uniquely identifies the relationship between a finished consumer trade item and one of its packaging
	 * components.
	 */
	PACKAGING_COMPONENT_NUMBER("243", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Secondary serial number. A secondary serial number represents the serial number of a component of an item.
	 *
	 * @see ApplicationIdentifier#SERIAL_NUMBER
	 */
	SECONDARY_SERIAL_NUMBER("250", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Reference to source entity, an attribute of a trade item used to refer to the original item from which the trade
	 * item was derived.
	 */
	REFERENCE_TO_SOURCE_ENTITY("251", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Global Document Type Identifier. The GDTI is used to identify a document type with an optional serial number.
	 */
	GDTI("253", Format.ALPHANUMERIC_VARIABLE, 13, 17),

	/**
	 * GLN extension component.
	 *
	 * @see ApplicationIdentifier#PHYSICAL_LOCATION
	 */
	GLN_EXTENSION_COMPONENT("254", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Global Coupon Number, provides a globally unique identification for a coupon, with an optional serial number.
	 */
	GCN("255", Format.NUMERIC_VARIABLE, 13, 25),

	/**
	 * Variable count, number of items contained in a variable measure trade item.
	 */
	VARIABLE_COUNT("30", Format.NUMERIC_VARIABLE, 8),

	ITEM_NET_WEIGHT_KG("310", Format.DECIMAL),
	ITEM_LENGTH_METRES("311", Format.DECIMAL),
	ITEM_WIDTH_METRES("312", Format.DECIMAL),
	ITEM_HEIGHT_METRES("313", Format.DECIMAL),
	ITEM_AREA_SQUARE_METRES("314", Format.DECIMAL),
	ITEM_NET_VOLUME_LITRES("315", Format.DECIMAL),
	ITEM_NET_VOLUME_CUBIC_METRES("316", Format.DECIMAL),
	ITEM_NET_WEIGHT_POUNDS("320", Format.DECIMAL),
	ITEM_LENGTH_INCHES("321", Format.DECIMAL),
	ITEM_LENGTH_FEET("322", Format.DECIMAL),
	ITEM_LENGTH_YARDS("323", Format.DECIMAL),
	ITEM_WIDTH_INCHES("324", Format.DECIMAL),
	ITEM_WIDTH_FEET("325", Format.DECIMAL),
	ITEM_WIDTH_YARDS("326", Format.DECIMAL),
	ITEM_HEIGHT_INCHES("327", Format.DECIMAL),
	ITEM_HEIGHT_FEET("328", Format.DECIMAL),
	ITEM_HEIGHT_YARDS("329", Format.DECIMAL),
	ITEM_AREA_SQUARE_INCHES("350", Format.DECIMAL),
	ITEM_AREA_SQUARE_FEET("351", Format.DECIMAL),
	ITEM_AREA_SQUARE_YARDS("352", Format.DECIMAL),
	ITEM_NET_WEIGHT_TROY_OUNCES("356", Format.DECIMAL),
	ITEM_NET_VOLUME_OUNCES("357", Format.DECIMAL),
	ITEM_NET_VOLUME_QUARTS("360", Format.DECIMAL),
	ITEM_NET_VOLUME_GALLONS("361", Format.DECIMAL),
	ITEM_NET_VOLUME_CUBIC_INCHES("364", Format.DECIMAL),
	ITEM_NET_VOLUME_CUBIC_FEET("365", Format.DECIMAL),
	ITEM_NET_VOLUME_CUBIC_YARDS("366", Format.DECIMAL),

	CONTAINER_GROSS_WEIGHT_KG("330", Format.DECIMAL),
	CONTAINER_LENGTH_METRES("331", Format.DECIMAL),
	CONTAINER_WIDTH_METRES("332", Format.DECIMAL),
	CONTAINER_HEIGHT_METRES("333", Format.DECIMAL),
	CONTAINER_AREA_SQUARE_METRES("334", Format.DECIMAL),
	CONTAINER_VOLUME_LITRES("335", Format.DECIMAL),
	CONTAINER_VOLUME_CUBIC_METRES("336", Format.DECIMAL),
	CONTAINER_GROSS_WEIGHT_POUNDS("340", Format.DECIMAL),
	CONTAINER_LENGTH_INCHES("341", Format.DECIMAL),
	CONTAINER_LENGTH_FEET("342", Format.DECIMAL),
	CONTAINER_LENGTH_YARDS("343", Format.DECIMAL),
	CONTAINER_WIDTH_INCHES("344", Format.DECIMAL),
	CONTAINER_WIDTH_FEET("345", Format.DECIMAL),
	CONTAINER_WIDTH_YARDS("346", Format.DECIMAL),
	CONTAINER_HEIGHT_INCHES("347", Format.DECIMAL),
	CONTAINER_HEIGHT_FEET("348", Format.DECIMAL),
	CONTAINER_HEIGHT_YARDS("349", Format.DECIMAL),
	CONTAINER_AREA_SQUARE_INCHES("353", Format.DECIMAL),
	CONTAINER_AREA_SQUARE_FEET("354", Format.DECIMAL),
	CONTAINER_AREA_SQUARE_YARDS("355", Format.DECIMAL),
	CONTAINER_VOLUME_QUARTS("362", Format.DECIMAL),
	CONTAINER_VOLUME_GALLONS("363", Format.DECIMAL),
	CONTAINER_VOLUME_CUBIC_INCHES("367", Format.DECIMAL),
	CONTAINER_VOLUME_CUBIC_FEET("368", Format.DECIMAL),
	CONTAINER_VOLUME_CUBIC_YARDS("369", Format.DECIMAL),

	/**
	 * Kilograms per square metre, of a particular trade item
	 */
	KILOGRAMS_PER_SQUARE_METRE("337", Format.DECIMAL),

	/**
	 * Count of trade items contained in a logistic unit.
	 *
	 * @see ApplicationIdentifier#CONTAINED_GTIN
	 */
	COUNT_OF_TRADE_ITEMS("37", Format.NUMERIC_VARIABLE, 8),

	/**
	 * The amount payable of a payment slip or the coupon value.
	 *
	 * @see ApplicationIdentifier#PAYMENT_SLIP_REFERENCE_NUMBER
	 * @see ApplicationIdentifier#INVOICING_PARTY
	 * @see ApplicationIdentifier#GCN
	 */
	AMOUNT_PAYABLE("390", Format.DECIMAL, 15),

	/**
	 * Amount payable and ISO currency code.
	 *
	 * @see ApplicationIdentifier#PAYMENT_SLIP_REFERENCE_NUMBER
	 * @see ApplicationIdentifier#INVOICING_PARTY
	 */
	AMOUNT_PAYABLE_WITH_CURRENCY("391", Format.CUSTOM),

	/**
	 * Amount payable for a variable measure trade item. Refers to an item identified by the Global Trade Item Number
	 * (GTIN) of a variable measure trade item and is expressed in local currency.
	 */
	AMOUNT_PAYABLE_PER_SINGLE_ITEM("392", Format.DECIMAL, 15),

	/**
	 * Amount payable for a variable measure trade item and ISO currency code. Refers to an item identified with the
	 * Global Trade Item Number (GTIN) of a variable measure trade item and is expressed in the indicated currency.
	 */
	AMOUNT_PAYABLE_PER_SINGLE_ITEM_WITH_CURRENCY("393", Format.CUSTOM),

	/**
	 * Percentage discount of a coupon.
	 */
	COUPON_DISCOUNT_PERCENTAGE("394", Format.DECIMAL, 4, 4),

	/**
	 * Customer’s purchase order number.  It contains the number of the purchase order assigned by the company that
	 * issued the order. The composition and content of the order number is left to the discretion of the customer.
	 */
	CUSTOMER_PURCHASE_ORDER_NUMBER("400", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Global Identification Number for Consignment. Identifies a logical grouping of goods (one or more physical
	 * entities) that has been consigned to a freight forwarder and is intended to be transported as a whole.
	 */
	CONSIGNMENT_NUMBER("401", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Global Shipment Identification Number.
	 */
	SHIPMENT_NUMBER("402", Format.NUMERIC_FIXED, 17),

	/**
	 * Routing code, assigned by the parcel carrier and is an attribute of the SSCC (Serial Shipping Container Code).
	 */
	ROUTING_CODE("403", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Ship to - Deliver to Global Location Number.
	 */
	SHIP_TO_LOCATION("410", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Bill to - Invoice to Global Location Number.
	 */
	BILL_TO_LOCATION("411", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Purchased from Global Location Number.
	 */
	PURCHASED_FROM_LOCATION("412", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Ship for - Deliver for - Forward to Global Location Number.
	 */
	SHIP_FOR_LOCATION("413", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Identification of a physical location - Global Location Number.
	 */
	PHYSICAL_LOCATION("414", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Global Location Number of the invoicing party.
	 */
	INVOICING_PARTY("415", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * GLN of the production or service location.
	 */
	PRODUCTION_OR_SERVICE_LOCATION("416", Format.NUMERIC_FIXED, GLN.LENGTH),

	/**
	 * Ship to - Deliver to postal code within a single postal authority.
	 */
	SHIP_TO_POSTAL_CODE("420", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Ship to - Deliver to postal code with three-digit ISO country code.
	 */
	SHIP_TO_POSTAL_CODE_WITH_COUNTRY("421", Format.CUSTOM),

	/**
	 * Country of origin of a trade item. ISO-3166.
	 */
	COUNTRY_OF_ORIGIN("422", Format.NUMERIC_FIXED, 3),

	/**
	 * Country or countries of initial processing.
	 */
	COUNTRY_OF_INITIAL_PROCESSING("423", Format.CUSTOM),

	/**
	 * Country of processing. ISO-3166.
	 */
	COUNTRY_OF_PROCESSING("424", Format.NUMERIC_FIXED, 3),

	/**
	 * Country or countries of disassembly.
	 */
	COUNTRY_OF_DISASSEMBLY("425", Format.CUSTOM),

	/**
	 * Country covering full process chain. ISO-3166.
	 */
	COUNTRY_OF_FULL_PROCESS_CHAIN("426", Format.NUMERIC_FIXED, 3),

	/**
	 * Country subdivision of origin code for a trade item. ISO based country subdivision code (e.g. provinces, states,
	 * cantons, etc.) of a country’s local region origin of the trade item. The ISO country subdivision code field
	 * contains up to three alphanumeric characters after separator of ISO 3166-2:2007 that is the principal subdivision
	 * of origin.
	 *
	 * @see ApplicationIdentifier#COUNTRY_OF_ORIGIN
	 */
	COUNTRY_SUBDIVISION_OF_ORIGIN("427", Format.ALPHANUMERIC_VARIABLE, 3),

	/**
	 * NATO Stock Number (NSN).
	 */
	NATO_STOCK_NUMBER("7001", Format.NUMERIC_FIXED, 13),

	/**
	 * UN/ECE meat carcasses and cuts classification.
	 */
	MEAT_CUT("7002", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Expiration date and time.
	 */
	EXPIRATION_DATE_AND_TIME("7003", Format.CUSTOM),

	/**
	 * Active potency.
	 */
	ACTIVE_POTENCY("7004", Format.NUMERIC_VARIABLE, 4),

	/**
	 * Catch area, identifies where the fisheries product was caught using the international fishing areas and subareas
	 * as defined by the United Nations Fisheries and Aquaculture Department of the Food and Agricultural Organization.
	 */
	CATCH_AREA("7005", Format.ALPHANUMERIC_VARIABLE, 12),

	/**
	 * First freeze date.
	 */
	FIRST_FREEZE_DATE("7006", Format.DATE),

	/**
	 * Harvest date. A harvest date or date range.
	 */
	HARVEST_DATE("7007", Format.CUSTOM, 6, 12),

	/**
	 * Species for fishery purposes, according to the 3-alpha Aquatic Sciences and Fisheries Information System (ASFIS)
	 * list of species.
	 */
	AQUATIC_SPECIES("7008", Format.ALPHANUMERIC_VARIABLE, 3),

	/**
	 * Fishing gear type, as defined by the United Nations Fisheries and Aquaculture Department of the Food and
	 * Agricultural Organization (FAO).
	 */
	FISHING_GEAR_TYPE("7009", Format.ALPHANUMERIC_VARIABLE, 10),

	/**
	 * Production method, provides the production method for fish and seafood as specified by the Fisheries and
	 * Aquaculture Department of the Food and Agricultural Organization (FAO) of the United Nations.
	 */
	PRODUCTION_METHOD("7010", Format.ALPHANUMERIC_VARIABLE, 2),

	/**
	 * Refurbishment lot ID, identifies a batch of items that were remanufactured to the original specifications using a
	 * combination of reused, repaired and new parts.
	 */
	REFURBISHMENT_LOT_ID("7020", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Functional status.
	 */
	FUNCTIONAL_STATUS("7021", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Revision status.
	 */
	REVISION_STATUS("7021", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Global Individual Asset Identifier of an assembly.
	 */
	ASSEMBLY_GIAI("7023", Format.ALPHANUMERIC_VARIABLE, 30),

	// No constants defined for 703s Number of processor with three-digit ISO country code. Query parse result using strings.

	// No constants defined for 710-719 National Healthcare Reimbursement Number (NHRN). Query parse result using strings.

	/**
	 * Roll products - width, length, core diameter, direction, splices.
	 */
	ROLL_PRODUCT_DIMENSIONS("8001", Format.NUMERIC_FIXED, 14),

	/**
	 * Cellular mobile telephone identifier.
	 */
	MOBILE_PHONE_IDENTIFIER("8002", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Global Returnable Asset Identifier.
	 */
	GRAI("8003", Format.ALPHANUMERIC_VARIABLE, 14, 30),

	/**
	 * Global Individual Asset Identifier (GIAI).
	 */
	GIAI("8004", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Price per unit of measure.
	 */
	PRICE_PER_UNIT("8005", Format.NUMERIC_FIXED, 6),

	/**
	 * Identification of the components of a trade item.
	 */
	GCTIN("8006", Format.NUMERIC_FIXED, 18),

	/**
	 * International Bank Account Number (IBAN).
	 */
	IBAN("8007", Format.ALPHANUMERIC_VARIABLE, 34),

	/**
	 * Date and time of production.
	 */
	PRODUCTION_DATE_AND_TIME("8008", Format.CUSTOM, 8, 12),

	/**
	 * Component / Part Identifier.
	 */
	COMPONENT_OR_PART_IDENTIFIER("8010", Format.ALPHANUMERIC_VARIABLE, 30),

	/**
	 * Component / Part Identifier serial number.
	 */
	COMPONENT_OR_PART_IDENTIFIER_SERIAL_NUMBER("8011", Format.NUMERIC_VARIABLE, 12),

	/**
	 * Software version.
	 */
	SOFTWARE_VERSION("8012", Format.ALPHANUMERIC_VARIABLE, 20),

	/**
	 * Global Service Relation Number of provider.
	 */
	GSRN_PROVIDER("8017", Format.NUMERIC_FIXED, 18),

	/**
	 * Global Service Relation Number of recipient.
	 */
	GSRN_RECIPIENT("8018", Format.NUMERIC_FIXED, 18),

	/**
	 * Service Relation Instance Number.
	 */
	SRIN("8019", Format.NUMERIC_VARIABLE, 10),

	/**
	 * Payment slip reference number. Assigned by the invoicing party, identifies a payment slip within a given Global
	 * Location Number (GLN) of an invoicing party. Together with the GLN of the invoicing party, the payment slip
	 * reference number uniquely identifies a payment slip.
	 */
	PAYMENT_SLIP_REFERENCE_NUMBER("8020", Format.ALPHANUMERIC_VARIABLE, 25),

	/**
	 * Coupon code identification for use in North America.
	 */
	COUPON_CODE_IDENTIFICATION_NORTH_AMERICA("8110", Format.ALPHANUMERIC_VARIABLE, 70),

	/**
	 * Loyalty points of a coupon.
	 *
	 * @see ApplicationIdentifier#GCN
	 */
	COUPON_LOYALTY_POINTS("8111", Format.NUMERIC_FIXED, 4),

	/**
	 * Paperless coupon code identification for use in North America.
	 */
	PAPERLESS_COUPON_CODE_IDENTIFICATION_NORTH_AMERICA("8112", Format.ALPHANUMERIC_VARIABLE, 70),

	/**
	 * Extended packaging URL.
	 */
	EXTENDED_PACKAGING_URL("8200", Format.ALPHANUMERIC_VARIABLE, 70),

	/**
	 * Information mutually agreed between trading partners.
	 */
	MUTUALLY_AGREED_INFORMATION("90", Format.ALPHANUMERIC_VARIABLE, 30);

	// No constants defined for 91-99 Company internal information. Query parse result using strings.

	private final String key;
	private final Format format;
	private final int minLength;
	private final int maxLength;

	ApplicationIdentifier(String key, Format format, int maxLength) {
		this.key = key;
		this.format = format;
		this.minLength = 1;
		this.maxLength = maxLength;
	}

	ApplicationIdentifier(String key, Format format, int minLength, int maxLength) {
		this.key = key;
		this.format = format;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	ApplicationIdentifier(String key, Format format) {
		this.key = key;
		this.format = format;
		if (format == Format.DECIMAL || format == Format.DATE) {
			this.minLength = 6;
			this.maxLength = 6;
		} else {
			this.minLength = -1;
			this.maxLength = -1;
		}
	}

	public String getKey() {
		return key;
	}

	public Format getFormat() {
		return format;
	}

	public int getMinLength() {
		return minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public enum Format {
		NUMERIC_FIXED,
		NUMERIC_VARIABLE,
		ALPHANUMERIC_FIXED,
		ALPHANUMERIC_VARIABLE,
		DECIMAL,
		DATE,
		CUSTOM
	}
}
