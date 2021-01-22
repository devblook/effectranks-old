package bryangaming.code.utils;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import java.util.logging.*;
import org.bukkit.configuration.*;
import java.io.*;

public final class Configuration extends YamlConfiguration {

    private final String fileName;
    private String fileStatus;

    private final Plugin plugin;
    private final File folder;

    public Configuration(Plugin plugin, String fileName, String fileExtension,
                         File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public Configuration(Plugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
        this.fileStatus = fileName;
    }

    public Configuration(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    private void createFile() {
        try {
            File file = new File(folder, fileName);

            if (file.exists()) {
                load(file);
                save(file);
                return;
            }

            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            } else {
                save(file);
            }

            load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Creation of Configuration '" + fileName + "' failed.", e);
        }
    }

    public void save() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);
        try {
            save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Save of the file '" + fileName + "' failed.", e);
        }
    }

    public void reload() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + fileName + "' failed.", e);
        }
    }

}