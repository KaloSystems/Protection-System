package de.kalosystems.protectionsystem.events;

import de.kalosystems.protectionsystem.ProtectionPlugin;
import de.kalosystems.protectionsystem.file.ProtectionFileManager;
import de.kalosystems.protectionsystem.protection.ProtectionTypes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Random;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void handle(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if (ProtectionTypes.values().equals(ProtectionTypes.valueOf(event.getBlockPlaced().getType().toString()))) {
            player.sendMessage(ProtectionPlugin.getProtectionPlugin().getPrefix() + "§bDie Sicherung für §3§l" + event.getBlockPlaced().getType() + " §bwurde erfolgreich erstellt§8.");
            if(ProtectionPlugin.getProtectionPlugin().getConfig().getString("mysql.type").equalsIgnoreCase("FILE")) {
                ProtectionPlugin.getProtectionPlugin().getProtectionFileManager().saveProtection(new Random().nextInt(), player.getName());
            }
        }
    }
}
