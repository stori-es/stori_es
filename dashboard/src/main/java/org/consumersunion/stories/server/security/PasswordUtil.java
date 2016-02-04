package org.consumersunion.stories.server.security;

import org.consumersunion.stories.server.security.authentication.BCrypt;

public class PasswordUtil {
    public static void main(String[] args) {
        String password = args[0];
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt(12)));
    }
}
