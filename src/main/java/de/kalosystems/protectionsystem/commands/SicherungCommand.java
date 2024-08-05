package de.kalosystems.protectionsystem.commands;

import de.kalosystems.protectionsystem.ProtectionPlugin;
import de.kalosystems.protectionsystem.protection.ProtectionTypes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SicherungCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            if (player.hasPermission(ProtectionPlugin.getProtectionPlugin().getConfig().getString("use.command.basic"))) {
                if (player.getItemOnCursor().getType().equals(ProtectionTypes.valueOf(player.getItemOnCursor().getType().name()))) {
                    if (ProtectionPlugin.getProtectionPlugin().getConfig().getString("mysql.type").equalsIgnoreCase("FILE")) {
                        player.sendMessage(ProtectionPlugin.getProtectionPlugin().getConfig().getString("message.command.confirmed")
                                .replaceAll("&", "§")
                                .replaceAll("%prefix%", ProtectionPlugin.getProtectionPlugin().getPrefix())
                                .replaceAll("%block%", player.getItemOnCursor().getType().name()));
                        ProtectionPlugin.getProtectionPlugin().getProtectionFileManager().saveProtection(new Random().nextInt(), player.getName());
                    }
                } else {
                    player.sendMessage(ProtectionPlugin.getProtectionPlugin().getConfig().getString("message.command.error")
                            .replaceAll("&", "§")
                            .replaceAll("%prefix%", ProtectionPlugin.getProtectionPlugin().getPrefix())
                            .replaceAll("%block%", player.getItemOnCursor().getType().name()));
                }
            } else {
                player.sendMessage(ProtectionPlugin.getProtectionPlugin().getConfig().getString("message.permission.denied").replaceAll("&", "§")
                        .replaceAll("%prefix%", ProtectionPlugin.getProtectionPlugin().getPrefix()));
            }
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("help")){
                player.sendMessage("§8§m---§8=§6Protection§8=§8§m---\n" +
                                   "\n§7/protect §8| §7create a Protection.\n" +
                                   "§7/protect help §8| §7List of all Commands\n" +
                                   "§7/protect remove §8| §7Remove a Protection\n" +
                                   "§7/protect autoprotect §8| §7de(activate) auto Protection for placing a Block\n" +
                                   "§8§m---§8=§6Protection§8=§8§m---\n");
            }
        }
        return false;
    }
}
