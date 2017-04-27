package org.consumersunion.stories.common.shared.service.datatransferobject;

import org.consumersunion.stories.common.client.service.datatransferobject.Coordinates;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.entity.SortField;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;

public class StorySearchParameters implements IsSerializable {
    // All these *really* should be final, but seemingly conflicts with GWT.
    private Integer start;
    private Integer length;
    private SortField sortField;
    private boolean ascending;

    private Integer accessMode;

    private Integer collectionId;
    private Integer questionnaireId;
    private Integer authorId;
    private String searchToken;
    private String northEast;
    private String southWest;
    private String localisation;
    private String distance;
    private boolean includeFullText = false;
    private boolean includeData = true;

    /**
     * Null constructor required by GWT. Users should use one of the 'intentful' constructors.
     */
    public StorySearchParameters() {
    }

    /**
     * 'Bounding box' constructor used to find stories within a given geographic box, with optional search text and
     * collection restrictions.
     */
    public StorySearchParameters(
            int start,
            int length,
            Integer collectionId,
            Integer questionnaireId,
            String searchToken,
            Coordinates northEast,
            Coordinates southWest,
            int accessMode,
            StorySortField sortField) {
        this.start = start;
        this.length = length;
        this.collectionId = collectionId;
        this.questionnaireId = questionnaireId;
        this.searchToken = searchToken;
        this.sortField = sortField;
        this.northEast = extractGeoLocation(northEast);
        this.southWest = extractGeoLocation(southWest);
        this.accessMode = accessMode;
    }

    /**
     * Basic search string constructor.
     */
    public StorySearchParameters(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            String searchText,
            int accessMode) {
        this.start = start;
        this.length = length;
        this.sortField = sortField;
        this.ascending = ascending;
        this.searchToken = searchText;
        this.accessMode = accessMode;
    }

    /**
     * Basic search string constructor with optional collection ID.
     */
    public StorySearchParameters(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            String searchText,
            Integer collectionId,
            int accessMode) {
        this.start = start;
        this.length = length;
        this.sortField = sortField;
        this.ascending = ascending;
        this.searchToken = searchText;
        this.collectionId = collectionId;
        this.accessMode = accessMode;
    }

    public static StorySearchParameters byAuthor(int start, int length, int authorId, int accessMode) {
        StorySearchParameters searchParameters = new StorySearchParameters();
        searchParameters.start = start;
        searchParameters.length = length;
        searchParameters.authorId = authorId;
        searchParameters.accessMode = accessMode;

        return searchParameters;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(int accessMode) {
        this.accessMode = accessMode;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getSearchToken() {
        return searchToken;
    }

    public void setSearchToken(String searchToken) {
        this.searchToken = searchToken;
    }

    public String getNorthEast() {
        return northEast;
    }

    public void setNorthEast(String northEast) {
        this.northEast = northEast;
    }

    public String getSouthWest() {
        return southWest;
    }

    public void setSouthWest(String southWest) {
        this.southWest = southWest;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Boolean containBBox() {
        return !Strings.isNullOrEmpty(northEast)
                && !Strings.isNullOrEmpty(southWest);
    }

    public boolean isIncludeFullText() {
        return includeFullText;
    }

    public void setIncludeFullText(boolean includeFullText) {
        this.includeFullText = includeFullText;
    }

    private String extractGeoLocation(Coordinates coordinate) {
        if (coordinate != null) {
            return coordinate.getLatitude() + "," + coordinate.getLongitude();
        }

        return "";
    }

    public boolean isIncludeData() {
        return includeData;
    }

    public void setIncludeData(boolean includeData) {
        this.includeData = includeData;
    }
}
