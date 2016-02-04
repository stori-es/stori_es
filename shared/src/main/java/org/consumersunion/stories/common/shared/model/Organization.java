package org.consumersunion.stories.common.shared.model;

import org.consumersunion.stories.common.shared.model.entity.Entity;

/**
 * A kind of {@link Entity} representing an organization or group. Every
 * {@link Organization} must have at least one description Document of type ???.
 *
 * @author Zane Rockenbaugh
 */
public class Organization extends Entity {
    /**
     * @see #getName()
     */
    private String name;

    /**
     * @see #getShortName()
     */
    private String shortName;

    private Integer defaultTheme;

    private String crmApiLogin;

    private String crmApiKey;

    private String crmEndpoint;

    /**
     * Constructor for a new Organization.
     */
    public Organization() {
        super();
    }

    /**
     * Constructor for an existing Organization.
     */
    public Organization(int id, int version) {
        super(id, version);
    }

    /**
     * The organization name.
     */
    public String getName() {
        return name;
    }

    /**
     * @see #getName()
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(Integer theme) {
        this.defaultTheme = theme;
    }

    public String getCrmApiLogin() {
        return crmApiLogin;
    }

    public void setCrmApiLogin(String crmApiLogin) {
        this.crmApiLogin = crmApiLogin;
    }

    public String getCrmApiKey() {
        return crmApiKey;
    }

    public void setCrmApiKey(String crmApiKey) {
        this.crmApiKey = crmApiKey;
    }

    public String getCrmEndpoint() {
        return crmEndpoint;
    }

    public void setCrmEndpoint(String crmEndpoint) {
        this.crmEndpoint = crmEndpoint;
    }
}
