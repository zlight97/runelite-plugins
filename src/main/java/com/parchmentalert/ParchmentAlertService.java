package com.parchmentalert;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.api.gameval.ItemID;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;

@Singleton
public class ParchmentAlertService {
    private final Client client;
    private final ParchmentAlertConfig config;

    private final ItemManager itemManager;

    private final int[] UnparchmentedItems = {
            ItemID.SKILLCAPE_MAX_INFERNALCAPE,
            ItemID.SKILLCAPE_MAX_FIRECAPE,
            ItemID.SKILLCAPE_MAX_ASSEMBLER,
            ItemID.BRONZE_PARRYINGDAGGER,
            ItemID.IRON_PARRYINGDAGGER,
            ItemID.STEEL_PARRYINGDAGGER,
            ItemID.BLACK_PARRYINGDAGGER,
            ItemID.MITHRIL_PARRYINGDAGGER,
            ItemID.ADAMANT_PARRYINGDAGGER,
            ItemID.RUNE_PARRYINGDAGGER,
            ItemID.DRAGON_PARRYINGDAGGER,
            ItemID.CASTLEWARS_SWORD_3,
            ItemID.CASTLEWARS_ARMOUR_BODY_3,
            ItemID.CASTLEWARS_ARMOUR_LEGS_3,
            ItemID.CASTLEWARS_MED_HELM_3,
            ItemID.CASTLEWARS_SHIELD_3,
            ItemID.CASTLEWARS_ARMOUR_SKIRT_3,
            ItemID.CASTLEWARS_MAGE_TOP,
            ItemID.CASTLEWARS_MAGE_LEGS,
            ItemID.CASTLEWARS_MAGE_HAT,
            ItemID.CASTLEWARS_RANGE_TOP,
            ItemID.CASTLEWARS_RANGE_LEGS,
            ItemID.CASTLEWARS_RANGE_QUIVER,
            ItemID.CASTLEWARS_SARADOMIN_HALO,
            ItemID.CASTLEWARS_ZAMORAK_HALO,
            ItemID.CASTLEWARS_GUTHIX_HALO,
            ItemID.BARBASSAULT_PENANCE_HEALER_HAT,
            ItemID.BARBASSAULT_PENANCE_FIGHTER_HAT,
            ItemID.BARBASSAULT_PENANCE_RANGER_HAT,
            ItemID.BARBASSAULT_PENANCE_FIGHTER_TORSO,
            ItemID.BARBASSAULT_PENANCE_RANGER_LEGS,
            ItemID.PEST_VOID_KNIGHT_TOP,
            ItemID.ELITE_VOID_KNIGHT_TOP,
            ItemID.PEST_VOID_KNIGHT_ROBES,
            ItemID.ELITE_VOID_KNIGHT_ROBES,
            ItemID.PEST_VOID_KNIGHT_MACE,
            ItemID.PEST_VOID_KNIGHT_GLOVES,
            ItemID.GAME_PEST_MAGE_HELM,
            ItemID.GAME_PEST_ARCHER_HELM,
            ItemID.GAME_PEST_MELEE_HELM,
            ItemID.INFERNAL_DEFENDER,
            ItemID.ARMADYL_HALO,
            ItemID.BANDOS_HALO,
            ItemID.SEREN_HALO,
            ItemID.ZAROS_HALO,
            ItemID.BRASSICA_HALO,
            ItemID.AVAS_ASSEMBLER,
            ItemID.TZHAAR_CAPE_FIRE,
            ItemID.INFERNAL_CAPE,
            ItemID.SKILLCAPE_MAX_SARADOMIN2,
            ItemID.SKILLCAPE_MAX_ZAMORAK2,
            ItemID.SKILLCAPE_MAX_GUTHIX2,
            ItemID.MA2_SARADOMIN_CAPE,
            ItemID.MA2_GUTHIX_CAPE,
            ItemID.MA2_ZAMORAK_CAPE,
            ItemID.BH_RUNE_POUCH,
            ItemID.BARBASSAULT_PENANCE_RUNNER_HAT,
            ItemID.CASTLEWARS_BOOTS_3,
            ItemID.CASTLEWARS_FULL_HELM_3,
            ItemID.BARRONITE_MACE,
            ItemID.PVPA_CENTURION_CUIRASS,
            ItemID.PVPA_ARENA_WRISTBANDS,
            ItemID.PVPA_ARENA_WRISTBANDS_CHARGED,
            ItemID.PVPA_ARENA_WRISTBANDS_I,
            ItemID.PVPA_ARENA_WRISTBANDS_I_CHARGED,
            ItemID.PVPA_SAIKAS_HOOD,
            ItemID.PVPA_SAIKAS_VEIL,
            ItemID.PVPA_SAIKAS_SHROUD,
            ItemID.PVPA_KORIFFS_HEADBAND,
            ItemID.PVPA_KORIFFS_COWL,
            ItemID.PVPA_KORIFFS_COIF,
            ItemID.PVPA_MAOMAS_MED_HELM,
            ItemID.PVPA_MAOMAS_FULL_HELM,
            ItemID.PVPA_MAOMAS_GREAT_HELM,
            ItemID.PVPA_CALAMITY_CHEST,
            ItemID.PVPA_SUPERIOR_CALAMITY_CHEST,
            ItemID.PVPA_ELITE_CALAMITY_CHEST,
            ItemID.PVPA_CALAMITY_BREECHES,
            ItemID.PVPA_SUPERIOR_CALAMITY_BREECHES,
            ItemID.PVPA_ELITE_CALAMITY_BREECHES,
            ItemID.LEAGUE_3_VOID_KNIGHT_TOP,
            ItemID.LEAGUE_3_VOID_KNIGHT_ROBES,
            ItemID.LEAGUE_3_VOID_KNIGHT_GLOVES,
            ItemID.LEAGUE_3_VOID_KNIGHT_TOP_ELITE,
            ItemID.LEAGUE_3_VOID_KNIGHT_ROBES_ELITE,
            ItemID.LEAGUE_3_VOID_MAGE_HELM,
            ItemID.LEAGUE_3_VOID_RANGE_HELM,
            ItemID.LEAGUE_3_VOID_MELEE_HELM,
            ItemID.DRAGON_PARRYINGDAGGER_T,
            ItemID.RUNE_PARRYINGDAGGER_T,
            ItemID.SKILLCAPE_MAX_ASSEMBLER_MASORI,
            ItemID.AVAS_ASSEMBLER_MASORI,
            ItemID.DIVINE_RUNE_POUCH,
            ItemID.INFERNAL_DEFENDER_GHOMMAL_5,
            ItemID.INFERNAL_DEFENDER_GHOMMAL_6,
            ItemID.ANCIENT_SCEPTRE,
            ItemID.BH_BARBASSAULT_PENANCE_FIGHTER_TORSO_CORRUPTED,
            ItemID.ANCIENT_SCEPTRE_BLOOD,
            ItemID.ANCIENT_SCEPTRE_ICE,
            ItemID.ANCIENT_SCEPTRE_SMOKE,
            ItemID.ANCIENT_SCEPTRE_SHADOW,
            ItemID.SKILLCAPE_MAX_DIZANAS,
            ItemID.DIZANAS_QUIVER_UNCHARGED,
            ItemID.DIZANAS_QUIVER_CHARGED,
            ItemID.DIZANAS_QUIVER_INFINITE,
            ItemID.DIVINE_RUNE_POUCH,
            ItemID.DIZANAS_QUIVER_UNCHARGED,
            ItemID.DIZANAS_QUIVER_CHARGED,
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
        HashSet<Integer> skippedItems = new HashSet<>();
        HashSet<String> skippedNames = new HashSet<>();
        if(config.ignoredItems() != null && !config.ignoredItems().isEmpty()) {
            String[] skipIds = config.ignoredItems().split(",");
            for (String s : skipIds) {
                try {
                    Integer i = Integer.parseInt(s.strip());
                    skippedItems.add(i);
                } catch (NumberFormatException ignored) {
                    skippedNames.add(s.strip().toLowerCase());
                }
            }
        }

        for (int unparchmentedItem : UnparchmentedItems) {
            if (skippedItems.contains(unparchmentedItem))
                continue;
            if (skippedNames.contains(client.getItemDefinition(unparchmentedItem).getName().toLowerCase()))
                continue;
            if ((equipment != null && equipment.contains(unparchmentedItem)) || (inventory != null && inventory.contains(unparchmentedItem))) {
                missingParchment.add(unparchmentedItem);
            }
        }
        if(config.extraItems() == null || config.extraItems().isEmpty())
            return missingParchment;

        String[] idStrs = config.extraItems().split(",");
        ArrayList<Integer> extraItems = new ArrayList<>();
        for(String s : idStrs)
        {
            try
            {
                Integer i = Integer.parseInt(s.strip());
                extraItems.add(i);

            }
            catch(NumberFormatException ignored)
            {
            }
        }
        for (Integer extraItem : extraItems) {
            if ((equipment != null && equipment.contains(extraItem.intValue())) || (inventory != null && inventory.contains(extraItem.intValue()))) {
                missingParchment.add(extraItem);
            }
        }

        return missingParchment;
    }
}
