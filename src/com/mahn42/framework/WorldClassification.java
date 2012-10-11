/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

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
    public boolean entitySpawnCheck = false;
    public ArrayList<EntityType> naturalEntityTypes = new ArrayList<EntityType>();
    public ArrayList<EntityType> customEntityTypes = new ArrayList<EntityType>();
    
    public void fromSectionValue(Object aObject) {
        HashMap<String, Object> lMap = null;
        if (aObject instanceof Map) {
            lMap = (HashMap<String, Object>)aObject;
        } else if (aObject instanceof MemorySection) {
            lMap = (HashMap<String, Object>) ((MemorySection)aObject).getValues(false);
        }
        if (lMap != null) {
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
                    } else if (lEntry.getKey().equalsIgnoreCase("entitySpawnCheck")) {
                        entitySpawnCheck = Boolean.parseBoolean(lValue);
                    } else if (lEntry.getKey().equalsIgnoreCase("naturalEntityTypes")) {
                        setNaturalEntityTypesFromSectionValue(lEntry.getValue());
                    } else if (lEntry.getKey().equalsIgnoreCase("customEntityTypes")) {
                        setCustomEntityTypesFromSectionValue(lEntry.getValue());
                    } else {
                        Framework.plugin.getLogger().info("WC ? unkown attribute " + lEntry.getKey());
                    }
                }
            }
        } else {
            Framework.plugin.getLogger().info("WC ? " + aObject.getClass().getName());
        }
        
    }
    
    public void setNaturalEntityTypesFromSectionValue(Object aObject) {
        naturalEntityTypes.clear();
        if (aObject instanceof MemorySection) {
        }
        if (aObject instanceof ArrayList) {
            for(Object lObj : ((ArrayList)aObject)) {
                String lStr = lObj.toString();
                if (lStr.equalsIgnoreCase("all")) {
                    naturalEntityTypes.addAll(java.util.Arrays.asList(EntityType.values()));
                } else {
                    naturalEntityTypes.add(EntityType.valueOf(lStr));
                }
            }
        } else {
            Framework.plugin.getLogger().info("WC nat ? " + aObject.getClass().getName() + " " + aObject.toString());
        }
    }

    public void setCustomEntityTypesFromSectionValue(Object aObject) {
        customEntityTypes.clear();
        if (aObject instanceof ArrayList) {
            for(Object lObj : ((ArrayList)aObject)) {
                String lStr = lObj.toString();
                if (lStr.equalsIgnoreCase("all")) {
                    naturalEntityTypes.addAll(java.util.Arrays.asList(EntityType.values()));
                } else {
                    customEntityTypes.add(EntityType.valueOf(lStr));
                }
            }
        } else {
            Framework.plugin.getLogger().info("WC cus ? " + aObject.getClass().getName());
        }
    }
}
