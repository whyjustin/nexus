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
package org.sonatype.nexus.integrationtests.nexus606;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.sonatype.nexus.integrationtests.AbstractNexusIntegrationTest;
import org.sonatype.nexus.integrationtests.RequestFacade;
import org.sonatype.nexus.integrationtests.TestContainer;
import org.sonatype.nexus.rest.model.ConfigurationsListResource;
import org.sonatype.nexus.rest.model.ConfigurationsListResourceResponse;
import org.sonatype.nexus.rest.model.LogsListResource;
import org.sonatype.nexus.rest.model.LogsListResourceResponse;
import org.sonatype.nexus.test.utils.FileTestingUtils;
import org.sonatype.nexus.test.utils.NexusConfigUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests downloading of log and config files.
 */
public class Nexus606DownloadLogsAndConfigFilesIT
    extends AbstractNexusIntegrationTest
{

    @BeforeClass
    public void setSecureTest()
    {
        TestContainer.getInstance().getTestContext().setSecureTest( true );
    }

    @Test
    public void getLogsTest()
        throws Exception
    {

        Response response = RequestFacade.sendMessage( "service/local/logs", Method.GET );
        String responseText = response.getEntity().getText();

        Assert.assertEquals( response.getStatus().getCode(), 200, "Status: \n" + responseText );

        LogsListResourceResponse logListResponse =
            (LogsListResourceResponse) this.getXMLXStream().fromXML( responseText );
        List<LogsListResource> logList = logListResponse.getData();
        Assert.assertTrue( logList.size() > 0, "Log List should contain at least 1 log." );

        for ( Iterator<LogsListResource> iter = logList.iterator(); iter.hasNext(); )
        {
            LogsListResource logResource = iter.next();

            // check the contents of each log now...
            this.downloadAndConfirmLog( logResource.getResourceURI(), logResource.getName() );
        }
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void getConfigsTest()
        throws IOException
    {

        Response response = RequestFacade.sendMessage( "service/local/configs", Method.GET );
        String responseText = response.getEntity().getText();

        Assert.assertEquals( response.getStatus().getCode(), 200, "Status: \n" + responseText );

        ConfigurationsListResourceResponse logListResponse =
            (ConfigurationsListResourceResponse) this.getXMLXStream().fromXML( responseText );
        List<ConfigurationsListResource> configList = logListResponse.getData();
        Assert.assertTrue( configList.size() >= 2, "Config List should contain at least 2 config file: " + configList );

        ConfigurationsListResource nexusXmlConfigResource = getConfigFromList( configList, "nexus.xml" );
        Assert.assertNotNull( nexusXmlConfigResource, "nexus.xml" );

        ConfigurationsListResource securityXmlConfigResource = this.getConfigFromList( configList, "security.xml" );
        Assert.assertNotNull( securityXmlConfigResource, "security.xml" );

        // check the config now...
        response = RequestFacade.sendMessage( new URL( nexusXmlConfigResource.getResourceURI() ), Method.GET, null );
        Assert.assertEquals( response.getStatus().getCode(), 200, "Status: " );

        String sha1Expected = FileTestingUtils.createSHA1FromStream( response.getEntity().getStream() );
        String sha1Actual = FileTestingUtils.createSHA1FromFile( NexusConfigUtil.getNexusFile() );

        Assert.assertEquals( sha1Actual, sha1Expected, "SHA1 of config files do not match: " );
    }

    private void downloadAndConfirmLog( String logURI, String name )
        throws Exception
    {
        Response response = RequestFacade.sendMessage( new URL( logURI ), Method.GET, null );
        Assert.assertEquals( response.getStatus().getCode(), 200, "Request URI: " + logURI + " Status: " );
        InputStream stream = response.getEntity().getStream();
        if ( stream == null )
        {
            Assert.fail( "Stream was null: " + response.getEntity().getText() );
        }

        // get the first 10000 chars from the downloaded log
        InputStreamReader reader = new InputStreamReader( stream );
        BufferedReader bReader = new BufferedReader( reader );

        StringBuilder downloadedLog = new StringBuilder();

        int lineCount = 10000;
        while ( bReader.ready() && lineCount-- > 0 )
        {
            downloadedLog.append( (char) bReader.read() );
        }

        Assert.assertTrue( downloadedLog.length() > 0 );

        final File nexusLog = getNexusLogFile();

        if ( nexusLog != null )
        {
            String logOnDisk = FileUtils.fileRead( nexusLog );
            Assert.assertTrue(
                logOnDisk.contains( downloadedLog ),
                "Downloaded log should be similar to log file from disk.\nNOTE: its possible the file could have rolled over.\nTrying to match:\n"
                    + downloadedLog );
        }
    }

    private ConfigurationsListResource getConfigFromList( List<ConfigurationsListResource> configList, String name )
    {
        for ( ConfigurationsListResource configurationsListResource : configList )
        {
            if ( configurationsListResource.getName().equals( name ) )
            {
                return configurationsListResource;
            }
        }
        return null;
    }

}
