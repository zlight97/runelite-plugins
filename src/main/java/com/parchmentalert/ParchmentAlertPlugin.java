package com.parchmentalert;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.runelite.api.widgets.WidgetInfo.PVP_WILDERNESS_LEVEL;

@Slf4j
@PluginDescriptor(
	name = "Trouver Parchment Alert"
)
public class ParchmentAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ParchmentAlertConfig config;

	@Inject
	private ParchmentAlertService service;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ParchmentAlertOverlay overlay;

	private boolean isRunning;

	@Inject
	ClientThread thread;

	private HashSet<String> unparchedNames;


	@Override
	protected void startUp() throws Exception
	{
		isRunning = true;

		thread.invokeLater(() ->
		{
			update();
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		isRunning = false;
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		update();
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if(unparchedNames!=null)
			unparchedNames.clear();

		thread.invokeLater(() ->
		{
			update();
		});
	}

	private void update()
	{
		int wildyLevel = 0;
		if (config.willNotify() == ParchmentAlertConfig.HighlightSetting.DEEP_WILD)
		{
			Widget wi = client.getWidget(PVP_WILDERNESS_LEVEL);

			if(wi != null)
				if(wi.isHidden())
				{
					wildyLevel = -1;
				}
				else {
					String wildyText = wi.getText();
					if(!(wildyText == null || wildyText.equals("")) && wildyText.matches(".*\\d.*")) //these are to prevent timing issues, as well as the -- outside ferox
					{
						Matcher m = Pattern.compile("(\\d+)").matcher(wildyText);
//						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", " Wildy text: " + wildyText, null); //commented for debug in future
						m.find();
						wildyLevel = Integer.valueOf(m.group());
					}
				}
		}
		if (config.willNotify() == ParchmentAlertConfig.HighlightSetting.ENABLED ||
				(config.willNotify() == ParchmentAlertConfig.HighlightSetting.DEEP_WILD && wildyLevel >= config.wildernessLevel()) ||
				(config.willNotify() == ParchmentAlertConfig.HighlightSetting.PVP &&
				(client.getVarbitValue(Varbits.IN_WILDERNESS) == 1 || client.getVarbitValue(Varbits.PVP_SPEC_ORB) == 1))) {
			ArrayList<Integer> unparchedItems = service.getItems();
			HashSet<String> unparchedNames = new HashSet<>();
			unparchedNames.clear();
			if (unparchedItems.size() == 0) {
				overlayManager.remove(overlay);
				return;
			}
			for (Integer i : unparchedItems) {
				ItemComposition ip = client.getItemDefinition(i.intValue());
				unparchedNames.add(ip.getName());
			}
			if (config.showNames()) {
				this.unparchedNames = unparchedNames;
			}
			overlayManager.add(overlay);


		} else {
			overlayManager.remove(overlay);
		}
	}

	public HashSet<String> getUnparchedNames()
	{
		return unparchedNames;
	}

	public boolean getIsRunning()
	{
		return isRunning && config.willNotify() != ParchmentAlertConfig.HighlightSetting.DISABLED;
	}


	@Provides
	ParchmentAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ParchmentAlertConfig.class);
	}
}
