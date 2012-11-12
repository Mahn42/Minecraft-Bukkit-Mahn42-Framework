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
                    if (aStrings[1].startsWith("-")) {
                        if (aStrings[1].equalsIgnoreCase("-all")) {
                            lConf.naturalEntityTypes.clear();
                        } else {
                            lConf.naturalEntityTypes.remove(EntityType.valueOf(aStrings[1].substring(1)));
                        }
                    } else {
                        if (aStrings[1].equalsIgnoreCase("all")) {
                            for(EntityType lType : EntityType.values()) {
                                lConf.naturalEntityTypes.add(lType);
                            }
                        } else {
                            lConf.naturalEntityTypes.add(EntityType.valueOf(aStrings[1]));
                        }
                    }
                } else if (aStrings[0].equalsIgnoreCase("customEntityTypes")) {
                    if (aStrings[1].startsWith("-")) {
                        if (aStrings[1].equalsIgnoreCase("-all")) {
                            lConf.customEntityTypes.clear();
                        } else {
                            lConf.customEntityTypes.remove(EntityType.valueOf(aStrings[1].substring(1)));
                        }
                    } else {
                        if (aStrings[1].equalsIgnoreCase("all")) {
                            for(EntityType lType : EntityType.values()) {
                                lConf.customEntityTypes.add(lType);
                            }
                        } else {
                            lConf.customEntityTypes.add(EntityType.valueOf(aStrings[1]));
                        }
                    }
                } else if (aStrings[0].equalsIgnoreCase("inventoryName")) {
                    lConf.inventoryName = aStrings[1];
                } else if (aStrings[0].equalsIgnoreCase("noInventory")) {
                    lConf.noInventory = Boolean.parseBoolean(aStrings[1]);
                } else {
                    aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cunkown attribute %s!", aStrings[0]));
                    return true;
                }
                lConf.updateToWorld();
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "set %s to %s in world %s", aStrings[0], aStrings[1], lWorld.getName()));
            } else {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cno configuration for world %s found!", lWorld.getName()));
            }
        } else {
            if (aCommandSender instanceof Player) {
                lWorld = ((Player)aCommandSender).getWorld();
            } else if (aStrings.length > 0) {
                lWorld = Framework.plugin.getServer().getWorld(aStrings[0]);
            }
            if (lWorld == null) {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cno world or unknown world!"));
                return true;
            }
            WorldConfiguration lConf = Framework.plugin.getWorldConfigurationDB().getByName(lWorld.getName());
            if (lConf != null) {
                lConf.updateFromWorld();
                aCommandSender.sendMessage("generateStructures:" + lConf.generateStructures);
                aCommandSender.sendMessage("generator:" + lConf.generator);
                aCommandSender.sendMessage("seed:" + lConf.seed);
                aCommandSender.sendMessage("type:" + lConf.type);
                aCommandSender.sendMessage("environment:" + lConf.environment);
                aCommandSender.sendMessage("spawnMonsters:" + lConf.spawnMonsters);
                aCommandSender.sendMessage("spawnAnimals:" + lConf.spawnAnimals);
                aCommandSender.sendMessage("difficulty:" + lConf.difficulty);
                aCommandSender.sendMessage("ownInventory:" + lConf.ownInventory);
                aCommandSender.sendMessage("gameMode:" + lConf.gameMode);
                aCommandSender.sendMessage("playerVsPlayer:" + lConf.playerVsPlayer);
                aCommandSender.sendMessage("entitySpawnCheck:" + lConf.entitySpawnCheck);
                aCommandSender.sendMessage("naturalEntityTypes:" + lConf.naturalEntityTypes);
                aCommandSender.sendMessage("customEntityTypes:" + lConf.customEntityTypes);
                aCommandSender.sendMessage("inventoryName:" + lConf.inventoryName);
                aCommandSender.sendMessage("noInventory:" + lConf.noInventory);
            } else {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cno configuration for world %s found!", lWorld.getName()));
            }
        }
        return true;
    }
    
}
