package de.kalosystems.protectionsystem.protection;

import de.kalosystems.protectionsystem.ProtectionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Protection {

    public Player setProtectionOwner(Player player){
        return player;
    }

    public Player getProtectionOwner(int id){
        String owner = "";
        if (ProtectionPlugin.getProtectionPlugin().getConfig().getString("mysql.type").equalsIgnoreCase("MYSQL")) {
            try {
                ResultSet rs = ProtectionPlugin.getProtectionPlugin().getDatabase().getResult("SELECT * FROM protections WHERE ID= '" + id + "'");
                if (rs.next() && rs.getString("OWNER") != null) {
                    owner = rs.getString("OWNER");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else {
            owner = ProtectionPlugin.getProtectionPlugin().getProtectionFileManager().getProtectionOwner(id);
        }
        return Bukkit.getPlayer(owner);
    }

    public List<Integer> getProtectionsByOwner(String owner) {
        List<Integer> protectionIds = new ArrayList<>();
        try {
            ResultSet rs = ProtectionPlugin.getProtectionPlugin().getDatabase().getResult("SELECT ID FROM protections WHERE OWNER = '" + owner + "'");

            while (rs.next()) {
                protectionIds.add(rs.getInt("ID"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return protectionIds;
    }

    public int getTotalProtections() {
        int totalProtections = 0;
        if (ProtectionPlugin.getProtectionPlugin().getConfig().getString("mysql.type").equalsIgnoreCase("MYSQL")) {
            try {
                ResultSet rs = ProtectionPlugin.getProtectionPlugin().getDatabase().getResult("SELECT COUNT(*) AS total FROM protections");
                if (rs.next()) {
                    totalProtections = rs.getInt("total");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else {
            totalProtections = ProtectionPlugin.getProtectionPlugin().getProtectionFileManager().getTotalProtections();
        }
        return totalProtections;
    }

    public void backupToDatabase() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String backupTableName = "protection_backup_" + dateFormat.format(now);

        try {
            // Create backup table
            String createBackupTableSQL = "CREATE TABLE " + backupTableName + " AS SELECT * FROM protections";
            PreparedStatement createStatement = ProtectionPlugin.getProtectionPlugin().getDatabase().getConnection().prepareStatement(createBackupTableSQL);
            createStatement.executeUpdate();

            System.out.println("Backup to database table '" + backupTableName + "' successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreFromBackup(String backupTableName) {
        try {
            String dropProtectionsTableSQL = "DROP TABLE IF EXISTS protections";
            PreparedStatement dropStatement = ProtectionPlugin.getProtectionPlugin().getDatabase().getConnection().prepareStatement(dropProtectionsTableSQL);
            dropStatement.executeUpdate();

            String restoreBackupTableSQL = "CREATE TABLE protections AS SELECT * FROM " + backupTableName;
            PreparedStatement restoreStatement = ProtectionPlugin.getProtectionPlugin().getDatabase().getConnection().prepareStatement(restoreBackupTableSQL);
            restoreStatement.executeUpdate();

            System.out.println("Restored from database table '" + backupTableName + "'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
