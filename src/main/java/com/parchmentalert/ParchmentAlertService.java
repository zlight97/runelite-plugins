package com.parchmentalert;

import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ParchmentAlertService {
    private final Client client;
    private final ParchmentAlertConfig config;

    private final ItemManager itemManager;

    @Inject
    private ParchmentAlertService(Client client, ParchmentAlertConfig config, ItemManager itemManager)
    {
        this.config = config;
        this.client = client;
        this.itemManager = itemManager;
    }
}
