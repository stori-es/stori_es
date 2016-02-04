package org.consumersunion.stories.common.shared.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Contact implements Serializable, Comparable<Contact> {
    public enum MediumType {
        PHONE("Phone", ""),
        EMAIL("Email", ""),
        YOUTUBE("Youtube", "http://www.youtube.com/user/##"),
        INSTAGRAM("Instagram", "http://instagram.com/##"),
        FLICKR("Flickr", "http://www.flickr.com/people/##"),
        SOUNDCLOUD("SoundCloud", "https://soundcloud.com/##"),
        SKYPE("Skype", "skype:##"),
        TUMBLR("Tumblr", "http://##.tumblr.com/"),
        PINTEREST("Pinterest", "http://pinterest.com/##"),
        GOOGLE_PLUS("Google+", "https://plus.google.com/u/0/##"),
        LINKEDIN("LinkedIn", "http://www.linkedin.com/in/##"),
        TWITTER("Twitter", "https://twitter.com/##"),
        FACEBOOK("Facebook", "https://www.facebook.com/##");

        public static final String[] SOCIAL_LABELS = new String[]{
                YOUTUBE.name(), INSTAGRAM.name(), FLICKR.name(),
                SOUNDCLOUD.name(), SKYPE.name(), TUMBLR.name(),
                PINTEREST.name(), GOOGLE_PLUS.name(), LINKEDIN.name(),
                TWITTER.name(), FACEBOOK.name()
        };

        private static final String[] SOCIAL_VALUES;

        static {
            Arrays.sort(SOCIAL_LABELS);

            List<String> values = Lists.newArrayList();
            for (String key : SOCIAL_LABELS) {
                values.add(valueOf(key).label);
            }

            SOCIAL_VALUES = values.toArray(new String[values.size()]);
        }

        private final String label;
        private final String url;

        MediumType(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public static String[] getSocialLabel() {
            return SOCIAL_LABELS;
        }

        public static String[] getSocialValue() {
            return SOCIAL_VALUES;
        }

        public boolean isSocialMedium() {
            int idx = Arrays.binarySearch(getSocialLabel(), name());

            return idx >= 0;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return asString();
        }

        public String asString() {
            return label;
        }
    }

    public static final String TYPE_MOBILE = "Mobile";
    public static final String TYPE_HOME = "Home";
    public static final String TYPE_WORK = "Work";
    public static final String TYPE_OTHER = "Other";
    public static final String SOCIAL = "Social";

    private int entityId;
    private String medium;
    private String type;
    private String value;
    private ContactStatus status;

    public Contact() {
        status = ContactStatus.UNVERIFIED;
    }

    public Contact(int entityId, String medium, String type, String value) {
        this(entityId, medium, type, value, ContactStatus.UNVERIFIED);
    }

    public Contact(int entityId, String medium, String type, String value, ContactStatus status) {
        setEntityId(entityId);
        setMedium(medium);
        setType(type);
        setValue(value);
        this.status = status;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public static Map<String, List<Contact>> listToMediumMap(List<Contact> contacts) {
        Map<String, List<Contact>> map = Maps.newHashMap();
        if (contacts == null) {
            return null;
        }
        for (Contact contact : contacts) {
            String medium;
            if (contact.getType().equalsIgnoreCase(SOCIAL)) {
                medium = SOCIAL;
            } else {
                medium = contact.getMedium();
            }

            if (!map.containsKey(medium)) {
                final List<Contact> list = new ArrayList<Contact>();
                map.put(medium, list);
            }
            map.get(medium).add(contact);
        }

        return map;
    }

    @Override
    public int compareTo(Contact o) {
        if (this.getEntityId() == o.getEntityId()) {
            if (this.medium.equals(o.medium)) {
                if (this.type.equals(o.type)) {
                    return this.value.compareTo(o.value);
                } else {
                    if (Contact.TYPE_OTHER.equals(this.type)) {
                        return 1;
                    }
                    if (Contact.TYPE_OTHER.equals(o.type)) {
                        return -1;
                    }
                    if (Contact.TYPE_MOBILE.equals(this.type)) {
                        return -1;
                    }
                    if (Contact.TYPE_MOBILE.equals(o.type)) {
                        return 1;
                    }
                    return this.type.compareTo(o.type);
                }
            } else {
                return this.medium.compareTo(o.medium);
            }
        } else {
            return this.entityId > o.entityId ? 1 : -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        return entityId == contact.entityId &&
                Objects.equals(medium, contact.medium) &&
                Objects.equals(type, contact.type) &&
                Objects.equals(value, contact.value) &&
                status == contact.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, medium, type, value, status);
    }
}
