package vn.utc.hotelmanager.hotel.utils;

public enum PaymentStateConstants {
    PAID("PAID"),
    UNPAID("UNPAID"),
    CANCELLED("CANCELLED");

    private final String value;

    PaymentStateConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
