package vn.utc.hotelmanager.utils;

public enum ApplicationUserRoleConstants {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    ApplicationUserRoleConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
