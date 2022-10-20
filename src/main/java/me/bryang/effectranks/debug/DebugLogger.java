package me.bryang.effectranks.debug;

import me.bryang.effectranks.EffectRanks;
import me.bryang.effectranks.PluginService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugLogger {

    private final PluginService pluginService;
    private final EffectRanks plugin;

    private File file;

    public DebugLogger(PluginService pluginService) {
        this.pluginService = pluginService;
        this.plugin = pluginService.getPlugin();
        setup();
        log("Loading plugin..");
    }

    public void log(String string, int number) {
        switch (number) {
            case 0:
                this.getLogger(string, "WARNING");
                break;

            case 1:
                plugin.getLogger().info("A error ocurred. Please check debug");
                this.getLogger(string, "ERROR");
                break;


            case 2:
                plugin.getLogger().info("Plugin successfully loaded!");
                this.getLogger(string, "SUCCESSFULL");
                break;
        }
    }

    public void setup() {
        file = new File(plugin.getDataFolder(), "logs.log");

        if (file.exists() && file.mkdirs()) {
            plugin.getLogger().info("Old log file removed!");
            if (!file.delete()) {
                throw new RuntimeException("Failed to delete old log file!");
            }
        }

        try {
            if (!file.createNewFile()) {
                throw new RuntimeException("Failed to create log file!");
            }
            plugin.getLogger().info("Logs created!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void log(String string) {
        this.getLogger(string, "LOG");
    }


    private void getLogger(String string, String mode) {
        Date actualDate = new Date();
        try {
            FileWriter logWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(logWriter);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            bufferedWriter.write("[" + format.format(actualDate) + " " + mode + "]: " + string);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
