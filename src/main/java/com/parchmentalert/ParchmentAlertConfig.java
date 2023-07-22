package com.parchmentalert;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("Trouver Parchment Alert")
public interface ParchmentAlertConfig extends Config
{
	enum HighlightSetting
	{
		DISABLED,
		ENABLED,
		PVP,
		DEEP_WILD;
	}

	@ConfigItem(
			keyName = "willNotify",
			name = "Notify when carrying an unlocked item",
			description = "Displays a popup when you are holding an unlocked item",
			position = 1
	)
	default HighlightSetting willNotify()
	{
		return HighlightSetting.DEEP_WILD;
	}

	@ConfigItem(
			keyName = "showNames",
			name = "Show Item Names",
			description = "The box will list what items are unlocked",
			position = 3
	)
	default boolean showNames()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "bg",
			name = "Background Color",
			description = "The color of the background.",
			position = 4
	)
	default Color bg() { return new Color(255, 0, 0, 150); }

	@ConfigItem(
		keyName = "flash",
		name = "Flash the reminder box",
		description = "The reminder box will flash to draw your attention to it",
		position = 5
	)
	default boolean flash()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			keyName = "flashColor",
			name = "Flash Color",
			description = "The Color to flash to.",
			position = 6
	)
	default Color flashColor() { return new Color(70, 61, 50, 150); }

	@Range(
			max = 80,
			min = 2
	)
	@ConfigItem(
			keyName = "interval",
			name = "Flash Interval",
			description = "Time between flash intervals",
			position = 7
	)
	default int interval() { return 40; }

	@Range(
			max = 56,
			min = 0
	)
	@ConfigItem(
			keyName = "wildernessLevel",
			name = "Deep Wild Level",
			description = "What level wilderness should the box appear in, when on Deep Wild mode. (0 includes safe zones like outside ferox)",
			position = 2
	)
	default int wildernessLevel() { return 20; }

	@ConfigItem(
			keyName = "extraItems",
			name = "Extra item IDs",
			description = "Add an item to be notified for using a comma seperated list and item IDs",
			position = 8
	)
	default String extraItems() { return ""; }

}

