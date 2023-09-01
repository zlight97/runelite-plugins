package com.fakeProtection;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("DMM Fake Protection")
public interface FakeProtectionConfig extends Config
{
	@ConfigItem(
		keyName = "skullTimer",
		name = "Show skull timer",
		description = "This replaces the fake protection timer",
		position = 2
	)
	default boolean skullTimer()
	{
		return true;
	}
	@Range (
		min = 0,
		max = 60
	)
	@ConfigItem(
			keyName = "timeDisplayed",
			name = "Start time",
			description = "0-60",
			position = 1
	)
	default int timeDisplayed()
	{
		return 60;
	}

	@ConfigItem(
			keyName = "decreaseTime",
			name = "Count down",
			description = "Every minute the fake time goes down by 1",
			position = 3
	)
	default boolean decreaseTime()
	{
		return true;
	}

	@Range(
			min = 0,
			max = 60
	)
	@ConfigItem(
			keyName = "resetTime",
			name = "Reset time",
			description = "If > than default, time will not change",
			position = 4
	)
	default int resetTime()
	{
		return 0;
	}
}
