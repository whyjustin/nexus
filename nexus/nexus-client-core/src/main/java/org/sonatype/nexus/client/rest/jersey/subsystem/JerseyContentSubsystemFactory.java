/*
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
package org.sonatype.nexus.client.rest.jersey.subsystem;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.client.core.Condition;
import org.sonatype.nexus.client.core.condition.NexusStatusConditions;
import org.sonatype.nexus.client.core.spi.SubsystemFactory;
import org.sonatype.nexus.client.core.subsystem.content.Content;
import org.sonatype.nexus.client.internal.rest.jersey.subsystem.JerseyContent;
import org.sonatype.nexus.client.rest.jersey.JerseyNexusClient;

/**
 * @since 2.1
 */
@Named
@Singleton
public class JerseyContentSubsystemFactory
    implements SubsystemFactory<Content, JerseyNexusClient>
{

    @Override
    public Condition availableWhen()
    {
        return NexusStatusConditions.anyModern();
    }

    @Override
    public Class<Content> getType()
    {
        return Content.class;
    }

    @Override
    public Content create( final JerseyNexusClient nexusClient )
    {
        return new JerseyContent( nexusClient );
    }
}
