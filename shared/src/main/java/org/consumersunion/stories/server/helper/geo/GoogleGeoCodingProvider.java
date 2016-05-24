package org.consumersunion.stories.server.helper.geo;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;

@Component
public class GoogleGeoCodingProvider implements GeoCodingService {
    private enum AddressComponentType {
        COUNTRY, ADMINISTRATIVE_AREA_LEVEL_1, LOCALITY
    }

    private static final Logger logger = Logger.getLogger(GoogleGeoCodingProvider.class.getName());
    private static final String GOOGLE_MAP_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String SENSOR_PARAM = "sensor";
    private static final String ADDRESS_PARAM = "address";
    private static final String CLIENT_PARAM = "client";
    private static final String SIGNATURE_PARAM = "signature";
    private static final int MAX_RETRIES = 3;

    private final String clientId;
    private final RestTemplate restTemplate;

    private boolean sensor = true;
    private UrlSigner urlSigner;

    @Inject
    GoogleGeoCodingProvider(
            @Named("geocoderClientId") String clientId,
            @Named("geocoderKey") String cryptoKey) {
        this.clientId = clientId;
        this.restTemplate = new RestTemplate();

        try {
            this.urlSigner = new UrlSigner(cryptoKey);
        } catch (IOException exception) {
            logger.severe(exception.getMessage());
        }
    }

    @Override
    public Localisation geoLocate(Address address) throws Exception {
        return geoLocate(address, 1);
    }

    private Localisation geoLocate(Address address, int tryCount) throws Exception {
        String extractedAddress = extractAddress(address);

        if (!Strings.isNullOrEmpty(extractedAddress.trim())) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(ADDRESS_PARAM, URLEncoder.encode(extractedAddress, "UTF-8"));
            params.put(SENSOR_PARAM, String.valueOf(sensor));

            if (isProductionMode()) {
                logger.log(Level.INFO, "Injecting business account : " + clientId);
                params.put(CLIENT_PARAM, clientId);
            }

            String requestUrl;
            if (isProductionMode()) {
                logger.log(Level.INFO, "Sigining the URL...");
                requestUrl = prepareUrl(params);
                URL url = new URL(requestUrl);
                String signature = urlSigner.signRequest(url.getPath(), url.getQuery());
                requestUrl = prepareUrl(params) + prepareUrlParam(SIGNATURE_PARAM, signature, false);
            } else {
                requestUrl = prepareUrl(params);
            }

            ResponseEntity<String> response;
            Localisation localisation = new Localisation();
            localisation.setGeoCodeProvider(Address.GeoCodeProvider.GOOGLE);

            try {
                response = restTemplate.getForEntity(requestUrl, String.class);
            } catch (Exception ignored) {
                localisation.setGeoCodeStatus(GeoCodeStatus.FAILED);
                return localisation;
            }

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.log(Level.INFO, "Status code OK - Response received...");

                JSONObject result = new JSONObject(response.getBody());
                GeoStatus status = GeoStatus.valueOf(result.getString("status"));

                if (status == GeoStatus.OK) {
                    localisation.setGeoCodeStatus(GeoCodeStatus.SUCCESS);
                    JSONObject rawResponse = result.getJSONArray("results").getJSONObject(0);
                    extractLocation(localisation, rawResponse);
                    updateAddress(address, rawResponse);
                } else if (status == GeoStatus.OVER_QUERY_LIMIT) {
                    if (tryCount > MAX_RETRIES) {
                        localisation.setGeoCodeStatus(GeoCodeStatus.SKIPPED);
                    } else {
                        sleep(2);
                        return geoLocate(address, tryCount + 1);
                    }
                } else {
                    localisation.setGeoCodeStatus(GeoCodeStatus.FAILED);
                }
            } else {
                logger.log(Level.INFO, "Status code Failed...");

                localisation.setGeoCodeStatus(GeoCodeStatus.FAILED);
            }

            return localisation;
        }

        return null;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }

    private void updateAddress(Address address, JSONObject rawResponse) throws JSONException {
        JSONArray components = rawResponse.getJSONArray("address_components");
        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            JSONArray types = component.getJSONArray("types");

            String value = component.getString("short_name");
            for (int j = 0; j < types.length(); j++) {
                try {
                    AddressComponentType addressComponentType = AddressComponentType.valueOf(
                            types.getString(j).toUpperCase());
                    switch (addressComponentType) {
                        case COUNTRY:
                            address.setCountry(value);
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_1:
                            address.setState(value);
                            break;
                        case LOCALITY:
                            address.setCity(value);
                            break;
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    private String prepareUrl(Map<String, String> params) throws Exception {
        StringBuilder completeUrl = new StringBuilder();
        completeUrl.append(GOOGLE_MAP_URL);

        completeUrl.append(prepareUrlParam(ADDRESS_PARAM, params.get(ADDRESS_PARAM), true));
        completeUrl.append(prepareUrlParam(SENSOR_PARAM, params.get(SENSOR_PARAM), false));

        if (isProductionMode()) {
            completeUrl.append(prepareUrlParam(CLIENT_PARAM, params.get(CLIENT_PARAM), false));
        }

        return completeUrl.toString();
    }

    private String prepareUrlParam(String paramName, String value, Boolean isFirst) {
        StringBuilder paramToken = new StringBuilder();
        paramToken.append(isFirst ? "?" : "&");
        paramToken.append(paramName + "=" + value);

        return paramToken.toString();
    }

    private Boolean isProductionMode() {
        return !Strings.isNullOrEmpty(System.getProperty("PARAM1"));
    }

    private void extractLocation(Localisation localisation, JSONObject rawResponse) throws Exception {
        JSONObject geometry = rawResponse.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        localisation.setLongitude(location.getDouble("lng"));
        localisation.setLatitude(location.getDouble("lat"));
    }

    private String extractAddress(Address address) {
        StringBuilder extractedAddress = new StringBuilder();

        if (!Strings.isNullOrEmpty(address.getAddress1())) {
            extractedAddress.append(address.getAddress1());
        }

        if (!Strings.isNullOrEmpty(address.getAddress2())) {
            extractedAddress.append(", ");
            extractedAddress.append(address.getAddress2());
        }

        if (!Strings.isNullOrEmpty(address.getCity())) {
            extractedAddress.append(", ");
            extractedAddress.append(address.getCity());
        }

        if (!Strings.isNullOrEmpty(address.getState())) {
            extractedAddress.append(", ");
            extractedAddress.append(address.getState());
        }

        if (!Strings.isNullOrEmpty(address.getPostalCode())) {
            extractedAddress.append(" ");
            extractedAddress.append(address.getPostalCode());
        }

        if (!Strings.isNullOrEmpty(address.getCountry())) {
            extractedAddress.append(", ");
            extractedAddress.append(address.getCountry());
        }

        return extractedAddress.toString().trim();
    }
}
