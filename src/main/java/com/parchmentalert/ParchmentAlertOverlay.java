package com.parchmentalert;

import net.runelite.api.Client;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

@Singleton
public class ParchmentAlertOverlay extends OverlayPanel {

    private final ParchmentAlertConfig config;
    private final Client client;

    private final String lockMsg = "You need to lock items!";

    private final ParchmentAlertPlugin plugin;
    @Inject
    private ParchmentAlertOverlay(ParchmentAlertConfig config, Client client, ParchmentAlertPlugin plugin)
    {
        this.config = config;
        this.client = client;
        this.plugin = plugin;
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        if(!plugin.getIsRunning())
            return null;
        panelComponent.getChildren().clear();

        FontMetrics metrics = graphics.getFontMetrics();

        HashSet<String> unparchedNames = plugin.getUnparchedNames();
        String itemList = "";
        int longest = 0;
        if(unparchedNames != null && unparchedNames.size()>0)
        {
            for(String s : unparchedNames)
            {
                int t = metrics.stringWidth(s);
                if(t>longest)
                    longest = t;
            }

        }
        int w = Math.max(ComponentConstants.STANDARD_WIDTH, metrics.stringWidth(lockMsg) + longest + 14);
        panelComponent.setPreferredSize(new Dimension(w,0));
        panelComponent.getChildren().add((LineComponent.builder())
                .left(lockMsg)
                .right(itemList)
                .build());
        if(unparchedNames != null && unparchedNames.size()>0)
        {
            for(String s : unparchedNames)
            {
                panelComponent.getChildren().add((LineComponent.builder())
                        .right(s)
                        .build());
            }
        }

        if (config.flash()) {
            if (client.getGameCycle() % config.interval() >= config.interval()/2)
            {
                panelComponent.setBackgroundColor(config.bg());
            } else
            {
                panelComponent.setBackgroundColor(config.flashColor());
            }
        } else {
            panelComponent.setBackgroundColor(config.bg());
        }

        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        return panelComponent.render(graphics);
    }
}
