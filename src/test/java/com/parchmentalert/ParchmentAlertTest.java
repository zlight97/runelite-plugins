package com.parchmentalert;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ParchmentAlertTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ParchmentAlertPlugin.class);
		RuneLite.main(args);
	}
}