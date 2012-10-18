/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandWorldSet implements CommandExecutor{

    //fw_world_set name value [worldname]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        World lWorld = null;
        if (aStrings.length > 1) {
            if (aCommandSender instanceof Player) {
                lWorld = ((Player)aCommandSender).getWorld();
            } else if (aStrings.length > 2) {
                lWorld = Framework.plugin.getServer().getWorld(aStrings[2]);
            }
            if (lWorld == null) {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cno world or unknown world!"));
                return true;
            }
            WorldConfiguration lConf = Framework.plugin.getWorldConfigurationDB().getByName(lWorld.getName());
            if (lConf != null) {
                lConf.updateFromWorld();
                if (aStrings[0].equalsIgnoreCase("generateStructures")) {
                    lConf.generateStructures = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("generator")) {
                    lConf.generator = aStrings[1];
                } else if (aStrings[0].equalsIgnoreCase("seed")) {
                    lConf.seed = Long.parseLong(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("type")) {
                    lConf.type = WorldType.valueOf(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("environment")) {
                    lConf.environment = World.Environment.valueOf(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("spawnMonsters")) {
                    lConf.spawnMonsters = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("spawnAnimals")) {
                    lConf.spawnAnimals = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("difficulty")) {
                    lConf.difficulty = Difficulty.valueOf(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("ownInventory")) {
                    lConf.ownInventory = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("gameMode")) {
                    lConf.gameMode = GameMode.valueOf(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("playerVsPlayer")) {
                    lConf.playerVsPlayer = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("entitySpawnCheck")) {
                    lConf.entitySpawnCheck = Boolean.parseBoolean(aStrings[1]);
                } else if (aStrings[0].equalsIgnoreCase("naturalEntityTypes")) {
                    lConf.naturalEntityTypes.add(EntityType.valueOf(aStrings[1]));
                } else if (aStrings[0].equalsIgnoreCase("customEntityTypes")) {
                    lConf.customEntityTypes.add(EntityType.valueOf(aStrings[1]));
                } else if (aStrings[0].equalsIgnoreCase("inventoryName")) {
                    lConf.inventoryName = aStrings[1];
                } else {
                    aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cunkown attribute %s!", aStrings[0]));
                    return true;
                }
                lConf.updateToWorld();
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "set %s to %s in world %s", aStrings[0], aStrings[1], lWorld.getName()));
            } else {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cno configuration for world %s found!", lWorld.getName()));
            }
        }
        return true;
    }
    
}
