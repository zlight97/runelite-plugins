package com.parchmentalert;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class ParchmentAlertService {
    private final Client client;
    private final ParchmentAlertConfig config;

    private final ItemManager itemManager;

    private final int[] UnparchmentedItems = {
            ItemID.DRAGON_DEFENDER, ItemID.DRAGON_DEFENDER_T,
            ItemID.FIRE_CAPE, ItemID.INFERNAL_CAPE, ItemID.AVAS_ACCUMULATOR, ItemID.MASORI_ASSEMBLER,
            ItemID.IMBUED_GUTHIX_CAPE, ItemID.IMBUED_SARADOMIN_CAPE, ItemID.IMBUED_ZAMORAK_CAPE,
            ItemID.FIRE_MAX_CAPE, ItemID.INFERNAL_MAX_CAPE, ItemID.ASSEMBLER_MAX_CAPE, ItemID.MASORI_ASSEMBLER_MAX_CAPE,
            ItemID.IMBUED_ZAMORAK_MAX_CAPE, ItemID.IMBUED_GUTHIX_MAX_CAPE, ItemID.IMBUED_SARADOMIN_MAX_CAPE, ItemID.BRONZE_DEFENDER,
            ItemID.IRON_DEFENDER, ItemID.STEEL_DEFENDER, ItemID.BLACK_DEFENDER, ItemID.MITHRIL_DEFENDER, ItemID.ADAMANT_DEFENDER,
            ItemID.RUNE_DEFENDER, ItemID.RUNE_DEFENDER_T, ItemID.DRAGON_DEFENDER, ItemID.DRAGON_DEFENDER_T, ItemID.AVERNIC_DEFENDER,
            ItemID.VOID_KNIGHT_GLOVES, ItemID.VOID_KNIGHT_MACE, ItemID.VOID_KNIGHT_TOP, ItemID.VOID_KNIGHT_ROBE, ItemID.VOID_KNIGHT_GLOVES_OR,
            ItemID.VOID_KNIGHT_TOP_OR, ItemID.VOID_KNIGHT_ROBE_OR, ItemID.VOID_MELEE_HELM, ItemID.VOID_MELEE_HELM_OR, ItemID.VOID_RANGER_HELM,
            ItemID.VOID_RANGER_HELM_OR,ItemID.VOID_MAGE_HELM, ItemID.VOID_MAGE_HELM_OR, ItemID.GUTHIX_HALO, ItemID.SARADOMIN_HALO, ItemID.ZAMORAK_HALO,
            ItemID.BANDOS_HALO, ItemID.SEREN_HALO, ItemID.ANCIENT_HALO, ItemID.BRASSICA_HALO, ItemID.FIGHTER_HAT, ItemID.FIGHTER_TORSO, ItemID.FIGHTER_TORSO_OR,
            ItemID.HEALER_HAT, ItemID.RANGER_HAT, ItemID.RUNNER_HAT, ItemID.PENANCE_SKIRT, ItemID.DECORATIVE_ARMOUR, ItemID.DECORATIVE_ARMOUR_4070,
            ItemID.DECORATIVE_ARMOUR_4504, ItemID.DECORATIVE_ARMOUR_4505, ItemID.DECORATIVE_ARMOUR_4510, ItemID.DECORATIVE_ARMOUR_4509, ItemID.DECORATIVE_ARMOUR_4070,
            ItemID.DECORATIVE_ARMOUR_11893, ItemID.DECORATIVE_ARMOUR_11894, ItemID.DECORATIVE_ARMOUR_11896, ItemID.DECORATIVE_ARMOUR_11895, ItemID.DECORATIVE_ARMOUR_11898,
            ItemID.DECORATIVE_ARMOUR_11897, ItemID.DECORATIVE_ARMOUR_11899, ItemID.DECORATIVE_ARMOUR_11900, ItemID.DECORATIVE_ARMOUR_11901, ItemID.ELITE_VOID_ROBE,
            ItemID.ELITE_VOID_ROBE_OR, ItemID.ELITE_VOID_TOP, ItemID.ELITE_VOID_TOP_OR
    };



    @Inject
    private ParchmentAlertService(Client client, ParchmentAlertConfig config, ItemManager itemManager)
    {
        this.config = config;
        this.client = client;
        this.itemManager = itemManager;
    }

    public ArrayList<Integer> getItems()
    {
        ArrayList<Integer> missingParchment = new ArrayList<>();
        final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
        final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

        for( int i = 0; i<UnparchmentedItems.length; i++ )
        {
            if((equipment != null && equipment.contains(UnparchmentedItems[i])) || (inventory != null && inventory.contains(UnparchmentedItems[i])))
            {
                missingParchment.add(UnparchmentedItems[i]);
            }
        }
        if(config.extraItems() == null || config.extraItems().equals(""))
            return missingParchment;

        String[] idStrs = config.extraItems().split(",");
        ArrayList<Integer> extraItems = new ArrayList<>();
        for(String s : idStrs)
        {
            try
            {
                Integer i = Integer.parseInt(s);
                extraItems.add(i);

            }
            catch(NumberFormatException e)
            {
            }
        }
        for( int i = 0; i<extraItems.size(); i++ )
        {
            if((equipment != null && equipment.contains(extraItems.get(i).intValue())) || (inventory != null && inventory.contains(extraItems.get(i).intValue())))
            {
                missingParchment.add(extraItems.get(i));
            }
        }

        return missingParchment;
    }
}
