package org.consumersunion.stories.common.client.service.response;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;

public class AddressResponse extends DataResponse<Address> {

    @Override
    public void setData(final List<Address> address) {
        this.data = address;
        if (this.data != null && this.data.size() > 0) {
            Collections.sort(data);
        }
    }
}
