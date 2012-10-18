package org.sonatype.nexus.email;

import org.sonatype.nexus.proxy.events.EventInspector;
import org.sonatype.nexus.proxy.events.NexusStoppedEvent;
import org.sonatype.plexus.appevents.Event;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @since 2.3
 */
@Named
@Singleton
public class NexusEmailerLifecycle
    implements EventInspector
{
    private final NexusEmailer emailer;

    @Inject
    public NexusEmailerLifecycle(final NexusEmailer emailer) {
        this.emailer = emailer;
    }

    @Override
    public boolean accepts(final Event<?> event) {
        return true;
    }

    @Override
    public void inspect(final Event<?> event) {
        if (event instanceof NexusStoppedEvent) {
            emailer.stop();
        }
    }
}
