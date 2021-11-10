package me.bryang.effectranks.modules;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.utils.FileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final PluginService pluginService;

    private final FileManager configFile;

    private final Map<String, Long> playerDoubleMap = new ConcurrentHashMap<>();

    public CooldownManager(PluginService pluginService){
       this.pluginService = pluginService;

       this.configFile = pluginService.getFiles().getConfig();
       setup();
    }

    public void setup(){

        FileManager playersFile = pluginService.getFiles().getPlayers();
        ConfigurationSection playersCooldown = playersFile.getConfigurationSection("players");

        if (playersCooldown == null){
            return;
        }

        if (playersCooldown.getKeys(false).isEmpty()) {
            return;
        }

        for (String players : playersCooldown.getKeys(false)){
            playerDoubleMap.put(players, playersFile.getLong("players." + players));
        }

        createCooldown();
    }

    public Map<String, Long> getPlayerCooldown(){
        return playerDoubleMap;
    }

    public void createCooldown() {
        FileManager playersFile = pluginService.getFiles().getPlayers();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerDoubleMap.isEmpty()){
                    cancel();
                }

                for (String players : playerDoubleMap.keySet()){
                    long time = System.currentTimeMillis() / 1000;

                    if (time >= playersFile.getLong("players." + players)) {
                        playerDoubleMap.remove(players);
                    }

                }
            }
        }.runTaskTimer(pluginService.getPlugin(), 20, 20);
    }

    public String getTextRankCooldown(Player player) {

        long currentTime = System.currentTimeMillis() / 1000;
        long cooldownTime = pluginService.getPlayerMethods().getCooldownMethod().getPlayerCooldown().get(player.getUniqueId().toString());

        long allTime = cooldownTime - currentTime;
        long allTimeNotCommprimed = allTime;


        int minutes = 60;
        int hour = minutes * 60;
        int day = hour * 24;
        int week = day * 7;
        int months = week * 4 + (day * 2);
        int year = months * 12;

        StringBuilder stringBuilder = new StringBuilder();

        if (allTime >= year) {
            allTime = allTime / year;

            if (allTime >= 2) {
                return allTime + " " + configFile.getString("time.years");
            }
            return allTime + " " + configFile.getString("time.year");
        }

        if (allTime >= months) {
            allTime = allTime / months;

            if (allTime >= 2) {
                return allTime + " " + configFile.getString("time.months");
            }
            return allTime + " " + configFile.getString("time.month");
        }

        if (allTime >= week) {
            allTime = allTime / week;

            if (allTime >= 2) {
                return allTime + " " + configFile.getString("time.weeks");
            }
            return allTime + " " + configFile.getString("time.week");
        }

        if (allTime >= day) {
            allTime = allTime / day;

            if (allTime >= 2) {
                return allTime + " " + configFile.getString("time.days");
            }
            return allTime + " " + configFile.getString("time.day");
        }

        if (allTime >= hour) {
            allTime = allTime / hour;

            if (allTime >= 2) {

                long secondsMin = allTimeNotCommprimed - (allTime * hour);
                stringBuilder.append(" ").append(secondsMin);

                if (allTimeNotCommprimed > allTime * hour) {

                    long minutesHour = allTimeNotCommprimed - (allTime * hour);
                    stringBuilder.append(" ").append(secondsMin).append(" ");;

                    if (minutesHour >= 2) {
                        stringBuilder.append(configFile.getString("time.minutes"));
                    } else {
                        stringBuilder.append(configFile.getString("time.minute"));
                    }
                }
                return allTime + " " + configFile.getString("time.hours") + stringBuilder.toString();
            }

            if (allTimeNotCommprimed > allTime * hour) {

                long minutesHour = allTimeNotCommprimed - (allTime * hour);
                stringBuilder.append(" ").append(minutesHour).append(" ");

                if (minutesHour >= 2) {
                    stringBuilder.append(configFile.getString("time.minutes"));
                } else {
                    stringBuilder.append(configFile.getString("time.minute"));
                }
            }

            return allTime + " " + configFile.getString("time.hour") + stringBuilder.toString();
        }

        if (allTime >= minutes){
            allTime = allTime / minutes;

            if (allTime >= 2) {
                if (allTimeNotCommprimed > allTime * minutes) {

                    long secondsMin = allTimeNotCommprimed - (allTime * minutes);
                    stringBuilder.append(" ").append(secondsMin).append(" ");

                    if (secondsMin >= 2) {
                        stringBuilder.append(configFile.getString("time.seconds"));
                    } else {
                        stringBuilder.append(configFile.getString("time.second"));
                    }
                }
                return allTime + " " + configFile.getString("time.minutes") + stringBuilder.toString();
            }

            if (allTimeNotCommprimed > allTime * minutes) {

                long secondsMin = allTimeNotCommprimed - (allTime * minutes);
                stringBuilder.append(" ").append(secondsMin).append(" ");;

                if (secondsMin >= 2) {
                    stringBuilder.append(configFile.getString("time.seconds"));
                } else {
                    stringBuilder.append(configFile.getString("time.second"));
                }
            }

            return allTime + " " + configFile.getString("time.minute") + stringBuilder.toString();
        }

        if (allTime >= 2) {
            return allTime + " " + configFile.getString("time.seconds");
        }

        return allTime + " " + configFile.getString("time.second");
    }

    public boolean playerIsInCooldown(Player player){
        return pluginService.getPlayerMethods().getCooldownMethod().getPlayerCooldown().get(player.getUniqueId().toString()) != null;
    }

    public void putCooldown(Player player, long time){
        CooldownManager cooldownManager = pluginService.getPlayerMethods().getCooldownMethod();

        if (cooldownManager.getPlayerCooldown().isEmpty()){
            cooldownManager.createCooldown();
        }

        cooldownManager.getPlayerCooldown().put(player.getUniqueId().toString(), time);
        pluginService.getFiles().getPlayers().set("players." + player.getUniqueId().toString(), time);
        pluginService.getFiles().getPlayers().save();
    }

    public int getRankCooldown(Player player) {

        RankManager rankManager = pluginService.getPlayerMethods().getLoopMethod();

        String timePath;

        if (!rankManager.getPlayerRank(player).equalsIgnoreCase("default")) {
            timePath = configFile.getString("groups." + rankManager.getPlayerRank(player) + ".cooldown");
        } else {
            timePath = configFile.getString("default.cooldown");
        }

        String[] times = timePath.trim()
                .replaceAll("[A-Za-z]", " ").split(" ");

        int minutes = 60;
        int hour = minutes * 60;
        int day = hour * 24;
        int week = day * 7;
        int months = week * 4 + (day * 2);
        int year = months * 12;

        int time = 0;
        int textSize = 0;

        for (String timeText : times){

            int timeNumber = Integer.parseInt(timeText);
            int timeSize = String.valueOf(timeNumber).length() + textSize;

            if (timePath.substring(timeSize).startsWith("y")) {
                time = timeNumber * year;
            }

            if (timePath.substring(timeSize).startsWith("mn")) {
                time = timeNumber * months;
            }

            if (timePath.substring(timeSize).startsWith("w")) {
                time = timeNumber * week;
            }

            if (timePath.substring(timeSize).startsWith("d")) {
                time = timeNumber * day;
            }

            if (timePath.substring(timeSize).startsWith("h")) {
                time = timeNumber * hour;
            }

            if (timePath.substring(timeSize).startsWith("m")) {
                time = timeNumber * minutes;
            }

            if (timePath.substring(timeSize).startsWith("s")){
                time = timeNumber;
            }

            textSize = textSize + timeSize;
        }

        return time;
    }
}
