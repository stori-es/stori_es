package org.consumersunion.stories.dashboard.client.util;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;

import com.google.common.collect.Lists;

public abstract class OrganizationClientUtil {
    private static final int LENGTH = 100;
    private static final List<OrganizationSummary> organizations = new ArrayList<OrganizationSummary>();

    public abstract void processAllOrganizations(List<OrganizationSummary> organizations);

    public static void getAllAdminOrganizations(
            RpcOrganizationServiceAsync organizationService,
            final OrganizationClientUtil processor) {
        final List<Object> data = Lists.<Object>newArrayList(0, true);
        organizations.clear();

        ResponseHandler<PagedDataResponse<OrganizationSummary>> callback =
                new ResponseHandlerLoader<PagedDataResponse<OrganizationSummary>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<OrganizationSummary> result) {
                        organizations.addAll(result.getData());
                        if (organizations.size() < LENGTH) {
                            processor.processAllOrganizations(organizations);
                            data.set(1, false);
                        }
                    }
                };
        organizationService.getAdminOrganizations((Integer) data.get(0), LENGTH, StorySortField.ID, true, callback);
    }
}
