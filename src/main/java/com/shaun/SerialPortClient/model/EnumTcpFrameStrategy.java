package com.shaun.SerialPortClient.model;

public enum EnumTcpFrameStrategy {

    FIXED_LENGTH("FixedLength"), LINE("Line"), DELIMITER("Delimiter"), LENGTH_FIELD("LengthField"), CUSTOM("Custom");

    private String value;

    EnumTcpFrameStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
