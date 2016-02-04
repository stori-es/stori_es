package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.post.UserPost;
import org.consumersunion.stories.common.shared.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPostConverter extends AbstractConverter<UserPost, User> {
    @Override
    public User convert(UserPost userPost) {
        User user = new User();
        user.setHandle(userPost.getHandle());
        user.setDefaultProfile(userPost.getDefaultProfileId());

        return user;
    }
}
