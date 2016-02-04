package org.consumersunion.stories.server.velocity;

import javax.inject.Inject;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

@Component
public class VelocityWrapperFactoryImpl implements VelocityWrapperFactory {
    private final VelocityEngine velocityEngine;

    @Inject
    VelocityWrapperFactoryImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public VelocityWrapper create(String templateLocation) {
        return new VelocityWrapper(velocityEngine, templateLocation);
    }
}
