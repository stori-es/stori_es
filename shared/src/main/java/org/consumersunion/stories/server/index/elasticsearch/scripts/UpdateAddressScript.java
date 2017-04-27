package org.consumersunion.stories.server.index.elasticsearch.scripts;

import org.consumersunion.stories.common.shared.model.Address;

import com.google.common.base.Strings;

public class UpdateAddressScript extends InlineScript {
    private static final String FORMAT = "ctx._source.%s = %s;";

    public UpdateAddressScript(Address address) {
        super(renderScript(address));
    }

    private static String renderScript(Address address) {
        StringBuilder sb = new StringBuilder();

        append(sb, "authorCity", address.getCity());
        append(sb, "authorState", address.getState());
        append(sb, "authorPostalCode", address.getPostalCode());
        append(sb, "authorAddress1", address.getAddress1());
        append(sb, "authorLocation", extractPosition(address));

        return sb.toString();
    }

    private static void append(StringBuilder sb, String property, String value) {
        String formattedValue = value == null || value.trim().isEmpty() ? null : "\"" + value + "\"";

        sb.append(String.format(FORMAT, property, formattedValue));
    }

    private static String extractPosition(Address address) {
        if (!Strings.isNullOrEmpty(address.getGeoCodeStatus())) {
            Address.GeoCodeStatus status = Address.GeoCodeStatus.valueOf(address.getGeoCodeStatus());
            if (status == Address.GeoCodeStatus.SUCCESS) {
                return address.getLatitude() + "," + address.getLongitude();
            }
        }

        return "";
    }
}
