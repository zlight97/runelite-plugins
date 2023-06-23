package com.parchmentalert;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
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
		update();
	}

	@Override
	protected void shutDown() throws Exception
	{
		isRunning = false;
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
//		if(config.willNotify() == ParchmentAlertConfig.HighlightSetting.PVP)
		update();
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if(unparchedNames!=null)
			unparchedNames.clear();

		update();
	}

	private void update()
	{
		thread.invokeLater(() ->
		{
			if (config.willNotify() == ParchmentAlertConfig.HighlightSetting.ENABLED ||
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
		});
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
