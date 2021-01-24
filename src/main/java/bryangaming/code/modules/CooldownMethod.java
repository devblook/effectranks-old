package bryangaming.code.modules;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownMethod {

    private final Manager manager;

    private final Configuration config;

    private final Map<String, Long> playerDoubleMap = new ConcurrentHashMap<>();

    public CooldownMethod(Manager manager){
       this.manager = manager;

       this.config = manager.getFiles().getConfig();
       setup();
    }

    public void setup(){

        Configuration playersFile = manager.getFiles().getPlayers();
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
        Configuration playersFile = manager.getFiles().getPlayers();

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
        }.runTaskTimer(manager.getPlugin(), 20, 20);
    }

    public String getTextRankCooldown(Player player) {

        long currentTime = System.currentTimeMillis() / 1000;
        long cooldownTime = manager.getPlayerMethods().getCooldownMethod().getPlayerCooldown().get(player.getUniqueId().toString());

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
                return allTime + " " + config.getString("time.years");
            }
            return allTime + " " + config.getString("time.year");
        }

        if (allTime >= months) {
            allTime = allTime / months;

            if (allTime >= 2) {
                return allTime + " " + config.getString("time.months");
            }
            return allTime + " " + config.getString("time.month");
        }

        if (allTime >= week) {
            allTime = allTime / week;

            if (allTime >= 2) {
                return allTime + " " + config.getString("time.weeks");
            }
            return allTime + " " + config.getString("time.week");
        }

        if (allTime >= day) {
            allTime = allTime / day;

            if (allTime >= 2) {
                return allTime + " " + config.getString("time.days");
            }
            return allTime + " " + config.getString("time.day");
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
                        stringBuilder.append(config.getString("time.minutes"));
                    } else {
                        stringBuilder.append(config.getString("time.minute"));
                    }
                }
                return allTime + " " + config.getString("time.hours") + stringBuilder.toString();
            }

            if (allTimeNotCommprimed > allTime * hour) {

                long minutesHour = allTimeNotCommprimed - (allTime * hour);
                stringBuilder.append(" ").append(minutesHour).append(" ");

                if (minutesHour >= 2) {
                    stringBuilder.append(config.getString("time.minutes"));
                } else {
                    stringBuilder.append(config.getString("time.minute"));
                }
            }

            return allTime + " " + config.getString("time.hour") + stringBuilder.toString();
        }

        if (allTime >= minutes){
            allTime = allTime / minutes;

            if (allTime >= 2) {
                if (allTimeNotCommprimed > allTime * minutes) {

                    long secondsMin = allTimeNotCommprimed - (allTime * minutes);
                    stringBuilder.append(" ").append(secondsMin).append(" ");

                    if (secondsMin >= 2) {
                        stringBuilder.append(config.getString("time.seconds"));
                    } else {
                        stringBuilder.append(config.getString("time.second"));
                    }
                }
                return allTime + " " + config.getString("time.minutes") + stringBuilder.toString();
            }

            if (allTimeNotCommprimed > allTime * minutes) {

                long secondsMin = allTimeNotCommprimed - (allTime * minutes);
                stringBuilder.append(" ").append(secondsMin).append(" ");;

                if (secondsMin >= 2) {
                    stringBuilder.append(config.getString("time.seconds"));
                } else {
                    stringBuilder.append(config.getString("time.second"));
                }
            }

            return allTime + " " + config.getString("time.minute") + stringBuilder.toString();
        }

        if (allTime >= 2) {
            return allTime + " " + config.getString("time.seconds");
        }

        return allTime + " " + config.getString("time.second");
    }

    public boolean playerIsInCooldown(Player player){
        return manager.getPlayerMethods().getCooldownMethod().getPlayerCooldown().get(player.getUniqueId().toString()) != null;
    }

    public void putCooldown(Player player, long time){
        CooldownMethod cooldownMethod = manager.getPlayerMethods().getCooldownMethod();

        if (cooldownMethod.getPlayerCooldown().isEmpty()){
            cooldownMethod.createCooldown();
        }

        cooldownMethod.getPlayerCooldown().put(player.getUniqueId().toString(), time);
        manager.getFiles().getPlayers().set("players." + player.getUniqueId().toString(), time);
        manager.getFiles().getPlayers().save();
    }

    public int getRankCooldown(Player player) {

        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();

        String timePath;

        if (!rankMethod.getPlayerRank(player).equalsIgnoreCase("default")) {
            timePath = config.getString("groups." + rankMethod.getPlayerRank(player) + ".cooldown");
        } else {
            timePath = config.getString("default.cooldown");
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
