package de.kalosystems.protectionsystem.file;

import de.kalosystems.protectionsystem.ProtectionPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProtectionFileManager {

    private File file;
    private FileConfiguration config;

    public ProtectionFileManager() {
        file = new File(ProtectionPlugin.getProtectionPlugin().getDataFolder() + "/protection.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void backupToFile() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String backupFileName = "protection_backup_" + dateFormat.format(now) + ".yml";
        File backupFile = new File(ProtectionPlugin.getProtectionPlugin().getDataFolder() + "/backups/", backupFileName);

        try {
            config.save(backupFile);
            System.out.println("Backup to file '" + backupFileName + "' successful.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void restoreFromFile(String backupFileName) {
        File backupFile = new File("plugins/YourPlugin/backups", backupFileName);

        if (!backupFile.exists()) {
            System.out.println("Backup file '" + backupFileName + "' not found.");
            return;
        }

        FileConfiguration backupConfig = YamlConfiguration.loadConfiguration(backupFile);
        config.set("protections", backupConfig.getConfigurationSection("protections"));

        saveConfig();
        System.out.println("Restored from backup file '" + backupFileName + "'.");
    }

    public String getProtectionOwner(int id) {
        return config.getString("protections." + id + ".owner");
    }

    public List<Integer> getProtectionsByOwner(String owner) {
        List<Integer> ids = new ArrayList<>();
        if (config.contains("protections")) {
            for (String key : config.getConfigurationSection("protections").getKeys(false)) {
                if (owner.equals(config.getString("protections." + key + ".owner"))) {
                    ids.add(Integer.parseInt(key));
                }
            }
        }
        return ids;
    }

    public void saveProtection(int id, String owner) {
        config.set("protections." + id + ".owner", owner);
        saveConfig();
    }

    public int getTotalProtections() {
        if (config.contains("protections")) {
            return config.getConfigurationSection("protections").getKeys(false).size();
        }
        return 0;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
