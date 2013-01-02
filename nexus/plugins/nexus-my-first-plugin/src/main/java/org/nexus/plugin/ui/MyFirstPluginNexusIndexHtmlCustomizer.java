package org.nexus.plugin.ui;

import java.util.Map;

import javax.inject.Named;

import org.sonatype.nexus.plugins.rest.AbstractNexusIndexHtmlCustomizer;
import org.sonatype.nexus.plugins.rest.NexusIndexHtmlCustomizer;


@Named( "MyFirstPluginNexusIndexHtmlCustomizer" )
public class MyFirstPluginNexusIndexHtmlCustomizer
    extends AbstractNexusIndexHtmlCustomizer
    implements NexusIndexHtmlCustomizer
{
	@Override
	public String getPostBodyContribution(Map<String, Object> context) {
		// TODO Auto-generated method stub
		return super.getPostBodyContribution(context);
	}
}