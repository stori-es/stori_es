package org.consumersunion.stories.common.shared.service.datatransferobject;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StoryPosition implements IsSerializable {
    private Integer storyId;
    private Float latitude;
    private Float longitude;

    public StoryPosition() {
    }

    public StoryPosition(Integer storyId, String solrPosition) {
        String[] position = solrPosition.split(",");
        this.latitude = Float.parseFloat(position[0]);
        this.longitude = Float.parseFloat(position[1]);
        this.storyId = storyId;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
