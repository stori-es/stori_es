package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

/**
 * Encapsulates the information regarding document contributors.
 */
public class DocumentContributor implements Serializable {
    /**
     * @see #getContributor()
     */
    private int contributor;

    /**
     * @see #getRole()
     */
    private Document.DocumentContributorRole role;

    /**
     * The ID of the contributing user.
     */
    public int getContributor() {
        return contributor;
    }

    /**
     * @see #getContributor()
     */
    public void setContributor(final int contributor) {
        this.contributor = contributor;
    }

    /**
     * The role of the associated contributor. TODO: specify or reference
     * spec
     */
    public Document.DocumentContributorRole getRole() {
        return role;
    }

    /**
     * @see #getRole()
     */
    public void setRole(final Document.DocumentContributorRole role) {
        this.role = role;
    }
}
