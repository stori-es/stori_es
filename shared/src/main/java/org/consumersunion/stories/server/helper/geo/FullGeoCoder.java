package org.consumersunion.stories.server.helper.geo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component("geoCoder")
public class FullGeoCoder {
    private final static Logger logger = Logger.getLogger(FullGeoCoder.class.getName());

    private static final String query = "SELECT a.entity, a.address1, a.address2, a.city, a.state, " +
            "a.country, a.postalCode FROM address a " +
            "WHERE a.geoCodeStatus is null or a.geoCodeStatus IN ('FAILED', 'SKIPPED') ORDER BY geoCodeStatus DESC";
    private static final String update = "UPDATE address set geoCodeDate = ?, geoCodeProvider = ?, geoCodeStatus = ?," +
            " latitude = ?, longitude = ? WHERE entity = ?";

    @Inject
    private GeoCodingService geoCodingService;

    public void geoCodeAll(int callQuota) throws Exception {
        Connection connection = null;

        try {
            connection = PersistenceUtil.getConnection();
            PreparedStatement addressesStatement = connection.prepareStatement(query);

            try {
                for (ResultSet result = addressesStatement.executeQuery(); result.next() && callQuota > 0; ) {
                    logger.log(Level.INFO, "## Remaining quota is : " + callQuota);

                    Address address = addressExtractor.extractData(result);
                    Localisation localisation = geoCodingService.geoLocate(address);

                    if (localisation != null) {
                        localisation.updateAddress(address);
                        updateAddress(connection, address);
                    } else {
                        logger.log(Level.INFO, "No location found for address with entity : " + address.getEntity());
                    }

                    callQuota = callQuota - 1;
                }
            } finally {
                addressesStatement.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void updateAddress(Connection connection, Address address) throws Exception {
        try {
            connection.setSavepoint();

            PreparedStatement updateStatement = connection.prepareStatement(update);
            updateStatement.setTimestamp(1, new Timestamp(address.getGeoCodeDate().getTime()));
            updateStatement.setString(2, address.getGeoCodeProvider());
            updateStatement.setString(3, address.getGeoCodeStatus());

            if (address.getLatitude() == null) {
                updateStatement.setNull(4, Types.BIGINT);
            } else {
                updateStatement.setBigDecimal(4, address.getLatitude());
            }

            if (address.getLongitude() == null) {
                updateStatement.setNull(5, Types.BIGINT);
            } else {
                updateStatement.setBigDecimal(5, address.getLongitude());
            }

            updateStatement.setInt(6, address.getEntity());
            updateStatement.executeUpdate();

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }

    private final ResultSetExtractor<Address> addressExtractor = new ResultSetExtractor<Address>() {
        @Override
        public Address extractData(ResultSet resultSet) throws SQLException {
            Address address = new Address();
            address.setEntity(resultSet.getInt(1));
            address.setAddress1(resultSet.getString(2));
            address.setAddress2(resultSet.getString(3));
            address.setCity(resultSet.getString(4));
            address.setState(resultSet.getString(5));
            address.setCountry(resultSet.getString(6));
            address.setPostalCode(resultSet.getString(7));

            return address;
        }
    };
}
