package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.UserPut;
import org.consumersunion.stories.common.shared.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPutMerger extends AbstractMerger<User, UserPut> {
    @Override
    public void merge(User entity, UserPut dto) {
        entity.setHandle(dto.getHandle());
    }
}
