package de.kalosystems.protectionsystem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void handle(BlockBreakEvent event){
        Player player = event.getPlayer();


    }
}
