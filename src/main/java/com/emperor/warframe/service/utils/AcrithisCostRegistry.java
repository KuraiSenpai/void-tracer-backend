package com.emperor.warframe.service.utils;

import java.util.HashMap;
import java.util.Map;

public class AcrithisCostRegistry {
    private static final Map<String, Integer> ITEM_COSTS = new HashMap<>();

    static {
        // 10 Pathos Clamps
        ITEM_COSTS.put("Forma Blueprint", 10);
        ITEM_COSTS.put("10,000 Kuva", 10);

        // 15 Pathos Clamps (Rivens)
        ITEM_COSTS.put("Melee Riven Mod", 15);
        ITEM_COSTS.put("Pistol Riven Mod", 15);
        ITEM_COSTS.put("Rifle Riven Mod", 15);
        ITEM_COSTS.put("Kitgun Riven Mod", 15);
        ITEM_COSTS.put("Zaw Riven Mod", 15);
        ITEM_COSTS.put("Shotgun Riven Mod", 15);

        // 20 Pathos Clamps
        ITEM_COSTS.put("Orokin Reactor Blueprint", 20);
        ITEM_COSTS.put("Orokin Catalyst Blueprint", 20);
        ITEM_COSTS.put("Exilus Warframe Adapter", 20);
        ITEM_COSTS.put("Exilus Weapon Adapter", 20);
        ITEM_COSTS.put("Companion Weapon Riven Mod", 20);

        // 25 Pathos Clamps
        ITEM_COSTS.put("Primary Arcane Adapter", 25);
        ITEM_COSTS.put("Secondary Arcane Adapter", 25);
    }

    public static int getCost(String itemName) {
        if (itemName == null) {
            return 20;
        }
        return ITEM_COSTS.getOrDefault(itemName, 20);
    }
}
