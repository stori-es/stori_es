package org.consumersunion.stories.common.shared.model.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.EMAIL;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.FACEBOOK;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.FLICKR;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.GOOGLE_PLUS;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.LINKEDIN;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.PHONE;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.SKYPE;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.SOUNDCLOUD;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.TUMBLR;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.TWITTER;
import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.YOUTUBE;
import static org.junit.Assert.assertTrue;

public class MediumTypeTest {
    @Test
    public void isSocialMedium() {
        assertNotSocial(PHONE);
        assertNotSocial(EMAIL);

        assertSocial(YOUTUBE);
        assertSocial(FLICKR);
        assertSocial(SOUNDCLOUD);
        assertSocial(SKYPE);
        assertSocial(TUMBLR);
        assertSocial(GOOGLE_PLUS);
        assertSocial(LINKEDIN);
        assertSocial(TWITTER);
        assertSocial(FACEBOOK);
    }

    private void assertSocial(Contact.MediumType mediumType) {
        assertTrue(mediumType.isSocialMedium());
    }

    private void assertNotSocial(Contact.MediumType mediumType) {
        Assert.assertFalse(mediumType.isSocialMedium());
    }
}
