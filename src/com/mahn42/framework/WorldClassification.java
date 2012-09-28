/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;

/**
 *
 * @author andre
 */
public class WorldClassification {
    public String name;
    public boolean generateStructures = true;
    public String generator = null;
    public long seed = 0;
    public WorldType type = WorldType.FLAT;
    public World.Environment environment = World.Environment.NORMAL;
    public boolean spawnMonsters = false;
    public boolean spawnAnimals = false;
    public Difficulty difficulty = Difficulty.NORMAL;
    public boolean ownInventory = false;
    public GameMode gameMode = GameMode.CREATIVE;
    public boolean playerVsPlayer = false;
    
    public void fromSectionValue(Object aObject) {
        if (aObject instanceof HashMap) {
            HashMap<String, Object> lMap = (HashMap)aObject;
            for(Entry<String, Object> lEntry : lMap.entrySet()) {
                if (lEntry.getValue() != null) {
                    String lValue = lEntry.getValue().toString();
                    if (lEntry.getKey().equalsIgnoreCase("name")) {
                        name = lValue;
                    } else if (lEntry.getKey().equalsIgnoreCase("generateStructures")) {
                        generateStructures = Boolean.parseBoolean(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("generator")) {
                        generator = lValue;
                    } else if (lEntry.getKey().equalsIgnoreCase("seed")) {
                        seed = Long.parseLong(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("type")) {
                        type = WorldType.valueOf(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("environment")) {
                        environment = World.Environment.valueOf(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("spawnMonsters")) {
                        spawnMonsters = Boolean.parseBoolean(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("spawnAnimals")) {
                        spawnAnimals = Boolean.parseBoolean(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("difficulty")) {
                        difficulty = Difficulty.valueOf(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("ownInventory")) {
                        ownInventory = Boolean.parseBoolean(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("gameMode")) {
                        gameMode = GameMode.valueOf(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("playerVsPlayer")) {
                        playerVsPlayer = Boolean.parseBoolean(lValue);
                    }
                }
            }
        }
    }
}
