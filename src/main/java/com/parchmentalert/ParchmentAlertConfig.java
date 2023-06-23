package com.parchmentalert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface ParchmentAlertConfig extends Config
{
	enum HighlightSetting
	{
		DISABLED,
		ENABLED,
		PVP;
	}

	@ConfigItem(
			keyName = "willNotify",
			name = "Notify when carrying an unlocked item",
			description = "Displays a popup when you are holding an unlocked item",
			position = 1
	)
	default HighlightSetting willNotify()
	{
		return HighlightSetting.PVP;
	}
	@ConfigItem(
		keyName = "flash",
		name = "Flash the reminder box",
		description = "The reminder box will flash to draw your attention to it",
		position = 2
	)
	default boolean flash()
	{
		return false;
	}
}
