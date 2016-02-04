package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.consumersunion.stories.common.shared.model.entity.Entity;

/**
 * This object was created to keep the credential information separate so that
 * we naturally avoid sending it across the wire and/or exposing the
 * information. Notice that it does not sub-class {@link User}; this is because
 * if we did, then we would inherit {@link User}'s serializability. We instead
 * maintain a reference to the {@link User}.
 * <p/>
 * Notice that there are two sets of password functions, one for clear text and
 * one for hashed passwords. The clear text password is used to set and update
 * the password, while the hashed password is used to verify authentication
 * requests. The clear text password will never be set through a back-end
 * retrieval and should only ever be set by the user themself. Similarly, the
 * hashed password is never set manually.
 */
public class CredentialedUser extends Entity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6287298950898334703L;
    /**
     * @see #getUser()
     */
    private User user;
    /**
     * @see #getPasswordClearText()
     */
    private String passwordClearText;
    /**
     * @see #getPasswordHash()
     */
    private String passwordHash;
    /**
     * @see #getResetQuestion()
     */
    private String resetQuestion;
    /**
     * @see #getResetAnswer()
     */
    private String resetAnswer;

    public CredentialedUser() {
        super();

        this.user = new User();
    }

    /**
     * Retrieve the associated {@link User}. See SYSTWO-239
     */
    public User getUser() {
        return user;
    }

    /**
     * @see #getUser()
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the clear text password. This is generally only set when creating
     * a new user or updating the password. It really shouldn't be possible to
     * set the value with anything meaningful at any other time.
     */
    public String getPasswordClearText() {
        return passwordClearText;
    }

    /**
     * @see #getPasswordClearText()
     */
    public void setPasswordClearText(String passwordClearText) {
        this.passwordClearText = passwordClearText;
    }

    /**
     * Returns the hashed password. This is used to validate user logins.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @see #getPasswordHash()
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * The user's reset question.
     */
    public String getResetQuestion() {
        return resetQuestion;
    }

    /**
     * @see #getResetQuestion()
     */
    public void setResetQuestion(String resetQuestion) {
        this.resetQuestion = resetQuestion;
    }

    /**
     * The answer to the reset question. A user must generally answer this
     * correctly (though the logic may allow low value edits like transposition
     * or likely typos).
     */
    public String getResetAnswer() {
        return resetAnswer;
    }

    /**
     * @see #getResetAnswer()
     */
    public void setResetAnswer(String resetAnswer) {
        this.resetAnswer = resetAnswer;
    }

    // the Entity and SystemEntity methods get passed through
    @Override
    public Integer getProfile() {
        return getUser().getProfile();
    }

    @Override
    public void setProfile(Integer profile) {
        getUser().setProfile(profile);
    }

    @Override
    public String getPermalink() {
        return getUser().getPermalink();
    }

    @Override
    public void setPermalink(String permalink) {
        getUser().setPermalink(permalink);
    }

    @Override
    public int getId() {
        return getUser().getId();
    }

    @Override
    public void setId(int id) {
        getUser().setId(id);
    }

    @Override
    public int getVersion() {
        return user.getVersion();
    }

    @Override
    public void setVersion(int version) {
        getUser().setVersion(version);
    }

    @Override
    public void setPublic(boolean isPublic) {
        getUser().setPublic(isPublic);
    }

    @Override
    public boolean isPublic() {
        return user.isPublic();
    }

    @Override
    public Date getCreated() {
        return getUser().getCreated();
    }

    @Override
    public void setCreated(Date created) {
        getUser().setCreated(created);
    }

    @Override
    public Date getUpdated() {
        return user.getUpdated();
    }

    @Override
    public void setUpdated(Date updated) {
        getUser().setUpdated(updated);
    }

    @Override
    public boolean isNew() {
        return getUser().isNew();
    }

    @Override
    public long getCreatedTimeStamp() {
        return getUser().getCreatedTimeStamp();
    }

    @Override
    public void setCreatedTimeStamp(long createdTimeStamp) {
        user.setCreatedTimeStamp(createdTimeStamp);
    }
}
