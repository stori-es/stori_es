package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@RunWith(JukitoRunner.class)
public class DocumentPersisterTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(BlockPersistenceHelper.class);
        }
    }

    @Test
    public void retrieveLatestByStoryAndRelation_willReturnEmptyTitle_whenTitleIsNull(
            DocumentPersister documentPersister) throws SQLException {
        // Given
        Connection connection = mock(Connection.class, RETURNS_DEEP_STUBS);

        stubDocumentPreparedStatement(connection);
        stubContributorsPreparedStatement(connection);

        DocumentPersister.EntityAndRelationParams params =
                mock(DocumentPersister.EntityAndRelationParams.class);
        given(params.getRelation()).willReturn(getAnySystemEntityRelation());

        // When
        Document documentText = documentPersister.retrieveLatestDocumentByRelation(params, Document.class, connection);

        // Then
        assertThat(documentText.getTitle()).isNotNull();
    }

    private SystemEntityRelation getAnySystemEntityRelation() {
        return SystemEntityRelation.values()[0];
    }

    private void stubContributorsPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement contributorsPreparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        given(contributorsPreparedStatement.executeQuery()).willReturn(resultSet);
        given(connection.prepareStatement(DocumentPersister.CONTRIBUTOR_SELECT)).willReturn
                (contributorsPreparedStatement);
    }

    private void stubDocumentPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement documentPreparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        given(resultSet.getString(4)).willReturn(getAnySystemEntityRelation().name());
        given(resultSet.getMetaData()).willReturn(mock(ResultSetMetaData.class));
        given(resultSet.next()).willReturn(true);
        given(documentPreparedStatement.executeQuery()).willReturn(resultSet);
        given(connection.prepareStatement(DocumentPersister.DOCUMENT_SELECT))
                .willReturn(documentPreparedStatement);
    }
}
