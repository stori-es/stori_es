package org.consumersunion.stories.common.client.widget.messages.primaryemail;

import com.google.common.base.Strings;

public class EmailUtil {
    public String hideEmail(String email) {
        int atIndex = email.indexOf("@");

        return email.substring(0, 2) + Strings.repeat("*", atIndex - 2) + email.substring(atIndex);
    }
}
