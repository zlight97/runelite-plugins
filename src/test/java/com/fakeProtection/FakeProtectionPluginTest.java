package com.fakeProtection;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FakeProtectionPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FakeProtectionPlugin.class);
		RuneLite.main(args);
	}
}