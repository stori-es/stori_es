package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.OrganizationPut;
import org.consumersunion.stories.common.shared.model.Organization;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class OrganizationPutMergerTest {
    private static final String A_NAME = "A_NAME";
    private static final String A_SHORT_NAME = "A_SHORT_NAME";

    private OrganizationPutMerger merger;

    @Before
    public void setUp() {
        merger = new OrganizationPutMerger();
    }

    @Test
    public void merge_mergesData() throws Exception {
        Organization organization = new Organization();
        OrganizationPut dto = OrganizationPut.builder()
                .setName(A_NAME)
                .setShortName(A_SHORT_NAME)
                .build();

        merger.merge(organization, dto);

        assertThat(organization.getName()).isEqualTo(A_NAME);
        assertThat(organization.getShortName()).isEqualTo(A_SHORT_NAME);
    }

    @Test
    public void merge_noDataProvided_doesNotUpdate() throws Exception {
        Organization organization = new Organization();
        organization = spy(organization);
        OrganizationPut dto = OrganizationPut.builder()
                .setName(A_NAME)
                .build();

        merger.merge(organization, dto);

        assertThat(organization.getName()).isEqualTo(A_NAME);
        verify(organization, never()).setShortName(anyString());
    }
}
