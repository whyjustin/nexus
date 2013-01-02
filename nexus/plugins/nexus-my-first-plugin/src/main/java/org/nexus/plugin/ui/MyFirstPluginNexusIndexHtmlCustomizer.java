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
	public String getPostHeadContribution(Map<String, Object> context) {
		return "<script src=\"static/js/nexus-indexer-my-first-plugin.js\" type=\"text/javascript\" charset=\"utf-8\"></script>";
	}
}