package de.kalosystems.protectionsystem;

import de.kalosystems.protectionsystem.events.BlockPlaceListener;
import de.kalosystems.protectionsystem.file.ProtectionFileManager;
import de.kalosystems.protectionsystem.mysql.MySQL;
import de.kalosystems.protectionsystem.protection.Protection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtectionPlugin extends JavaPlugin {

    private static ProtectionPlugin protectionPlugin;
    private ProtectionFileManager protectionFileManager;
    private Protection protection;
    private MySQL mySQL;
    private String prefix;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        protectionPlugin = this;
        protection = new Protection();
        prefix = getConfig().getString("prefix");

        if(getConfig().getString("mysql.type").equalsIgnoreCase("MYSQL")) {
            mySQL = new MySQL(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                    getConfig().getString("mysql.database"), getConfig().getString("mysql.username"),
                    getConfig().getString("mysql.password"));

            String tableName = "protections";
            String tableType = "ID INT AUTO_INCREMENT PRIMARY KEY, OWNER VARCHAR(16) NOT NULL, AUTHPERSONS TEXT, " +
                    "BLOCKED_PERSON VARCHAR(16), CHEST BOOLEAN DEFAULT FALSE, WORKBENCH BOOLEAN DEFAULT FALSE, " +
                    "ANVIL BOOLEAN DEFAULT FALSE, HOPPER BOOLEAN DEFAULT FALSE, FURNACE BOOLEAN DEFAULT FALSE, " +
                    "DOOR BOOLEAN DEFAULT FALSE, SIGN BOOLEAN DEFAULT FALSE, BEACON BOOLEAN DEFAULT FALSE, " +
                    "BREWING_STAND BOOLEAN DEFAULT FALSE, ENCHANTING_TABLE BOOLEAN DEFAULT FALSE";

            mySQL.createTable(tableName, tableType);
        }else{
            protectionFileManager = new ProtectionFileManager();
        }

        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);

    }

    @Override
    public void onDisable() {
    protectionPlugin = null;
        if(getConfig().getString("mysql.type").equalsIgnoreCase("MYSQL")) {
            getDatabase().disconnect();
        }
    }

    public MySQL getDatabase() {
        return mySQL;
    }

    public static ProtectionPlugin getProtectionPlugin() {
        return protectionPlugin;
    }

    public ProtectionFileManager getProtectionFileManager() {
        return protectionFileManager;
    }

    public Protection getProtection() {
        return protection;
    }

    public String getPrefix() {
        return prefix;
    }
}