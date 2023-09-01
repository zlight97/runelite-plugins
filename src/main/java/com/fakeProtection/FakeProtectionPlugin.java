package com.fakeProtection;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.SkullIcon;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import static net.runelite.api.widgets.WidgetInfo.*;
import static net.runelite.client.config.RuneScapeProfileType.getCurrent;

@Slf4j
@PluginDescriptor(
	name = "DMM Fake Protection"
)
public class FakeProtectionPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private FakeProtectionConfig config;

	private RuneScapeProfileType profileType;

	private int tickCounter = 0;

	private int startTime;

	private int displayedTime;

	public final int TIMER_WIDGET = 5898291;
	public final int LEVEL_WIDGET = 5898289;

	@Override
	protected void startUp() throws Exception
	{
		tickCounter = 0;
		profileType = getCurrent(client);
		displayedTime = config.timeDisplayed();
		startTime = displayedTime;
	}

	@Override
	protected void shutDown() throws Exception
	{
		//These will help it appear normal faster, but the game will overwrite the sprite within a few ticks normally
		Widget wi = client.getWidget(LEVEL_WIDGET);//This is the 3-50 etc widget
		if(wi != null) {
			wi.setHidden(false);
		}
		wi = client.getWidget(TIMER_WIDGET); //this is the timer widget
		if(wi != null) {
			wi.setHidden(true);
		}
	}

	@Subscribe
	protected void onBeforeRender(BeforeRender event)
	{
		switch(profileType) {
			case DEADMAN:
			case DEADMAN_REBORN:
				Update();
			default:
				break;
		}
	}

	@Subscribe
	protected void onGameTick(GameTick tick)
	{
		if(tickCounter++ > 100)
		{
			tickCounter = 0;
			if(config.decreaseTime())
			{
				if(displayedTime-- <= config.resetTime())
					displayedTime = config.timeDisplayed();
			}
			else
				displayedTime = config.timeDisplayed();

		}
	}

	@Subscribe
	protected void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		profileType = getCurrent(client);
	}

	@Subscribe
	protected void onConfigChanged(ConfigChanged configChanged)	{
		if(startTime!=config.timeDisplayed()) {
			tickCounter = 0;
			displayedTime = config.timeDisplayed();
			startTime = displayedTime;
		}
	}

	@Provides
	FakeProtectionConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FakeProtectionConfig.class);
	}

	private void Update()
	{
		Widget wi = client.getWidget(PVP_WILDERNESS_LEVEL);
		if(wi != null) {
			wi.setText("Protection");
			wi.deleteAllChildren();
		}
		wi = client.getWidget(TIMER_WIDGET); //this is the timer widget
		if(wi != null) {
			SkullIcon currentIcon = client.getLocalPlayer().getSkullIcon();
			if(config.skullTimer() && currentIcon!=null && currentIcon!=SkullIcon.SKULL_FIGHT_PIT) {
				String curTime = wi.getText();
				if(curTime.length() > 4)
					wi.setText(curTime.substring(curTime.length() - 5));
			}
			else {
				wi.setText(Integer.toString(displayedTime) + "min");
			}
			wi.setHidden(false);
		}

		wi = client.getWidget(LEVEL_WIDGET);//This is the 3-50 etc widget
		if(wi != null) {
			wi.setHidden(true);
		}

	}

}
