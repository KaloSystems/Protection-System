package de.kalosystems.protectionsystem.protection;

import org.bukkit.Material;

public enum ProtectionTypes {

    WORKBENCH(Material.CRAFTING_TABLE),
    CHEST(Material.CHEST),
    ANVIL(Material.ANVIL),
    BEACON(Material.BEACON);


    ProtectionTypes(Material material) {
    }
}
