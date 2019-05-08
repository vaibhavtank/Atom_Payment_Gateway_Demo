package com.atom.enums;

import java.util.HashMap;
import java.util.Map;

public enum PaymentType {

	NET_BANKING(1, "NB"), CREDIT_CARD(2, "CC"), DEBIT_CARD(3, "DC"), AMERICAN_EXPRESS(4, "MX");

	private int code;
	private String value;
	private static Map<Integer, PaymentType> map = new HashMap();

	static {
		for (PaymentType type : PaymentType.values()) {
			map.put(type.code, type);
		}
	}

	public static PaymentType valueOf(int code) {
		return map.get(code);
	}

	private PaymentType(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
