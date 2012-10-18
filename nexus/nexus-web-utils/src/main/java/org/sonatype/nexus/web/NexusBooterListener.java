/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.inject.Injector;
import org.sonatype.nexus.Nexus;
import org.sonatype.nexus.configuration.application.NexusConfiguration;

import static com.google.common.base.Preconditions.checkState;

/**
 * This J2EE ServletContextListener gets the Plexus from servlet context, and uses it to lookup, hence "boot" the Nexus
 * component. Finally, it will place Nexus instance into servlet context, to make it available for other filters and
 * servlets. Same will happen with NexusConfiguration instance.
 * 
 * @author cstamas
 */
public class NexusBooterListener
    implements ServletContextListener
{
    public void contextInitialized( ServletContextEvent event )
    {
        ServletContext context = event.getServletContext();

        try
        {
            Injector injector = (Injector) context.getAttribute(Injector.class.getName());
            checkState(injector != null, "ServletContext is missing Injector");
            final Nexus nexus = injector.getInstance(Nexus.class);

            nexus.start();

            context.setAttribute(Nexus.class.getName(), nexus);

            NexusConfiguration nexusConfiguration = injector.getInstance(NexusConfiguration.class);

            context.setAttribute(NexusConfiguration.class.getName(), nexusConfiguration);
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Could not initialize Nexus", e );
        }
    }

    public void contextDestroyed( ServletContextEvent event )
    {
        ServletContext context = event.getServletContext();
        try {
            Nexus nexus = (Nexus) context.getAttribute(Nexus.class.getName());
            nexus.stop();
        }
        catch (Exception e) {
            throw new IllegalStateException( "Could not shutdown Nexus", e);
        }

        context.removeAttribute(NexusConfiguration.class.getName());
        context.removeAttribute(Nexus.class.getName());
    }
}
