/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.EntityType;

/**
 *
 * @author andre
 */
public class WorldConfiguration extends DBRecord {
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
    public String classificationName;
    public boolean entitySpawnCheck = false;
    public ArrayList<EntityType> naturalEntityTypes = new ArrayList<EntityType>();
    public ArrayList<EntityType> customEntityTypes = new ArrayList<EntityType>();
    public String inventoryName;
    public boolean noInventory = false;
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(name);
        aCols.add(generateStructures);
        aCols.add(generator);
        aCols.add(seed);
        aCols.add(type);
        aCols.add(environment);
        aCols.add(spawnMonsters);
        aCols.add(spawnAnimals);
        aCols.add(difficulty);
        aCols.add(ownInventory);
        aCols.add(gameMode);
        aCols.add(playerVsPlayer);
        aCols.add(classificationName);
        aCols.add(entitySpawnCheck);
        String lStr = "";
        for(EntityType lType : naturalEntityTypes) {
            if (lStr.isEmpty()) {
                lStr = lType.toString();
            } else {
                lStr += "," + lType.toString();
            }
        }
        aCols.add(lStr);
        lStr = "";
        for(EntityType lType : customEntityTypes) {
            if (lStr.isEmpty()) {
                lStr = lType.toString();
            } else {
                lStr += "," + lType.toString();
            }
        }
        aCols.add(lStr);
        aCols.add(inventoryName);
        aCols.add(noInventory);
    }

    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        name = aCols.pop();
        generateStructures = Boolean.parseBoolean(aCols.pop());
        generator = aCols.pop();
        seed = Long.parseLong(aCols.pop());
        type = WorldType.valueOf(aCols.pop());
        environment = World.Environment.valueOf(aCols.pop());
        spawnMonsters = Boolean.parseBoolean(aCols.pop());
        spawnAnimals = Boolean.parseBoolean(aCols.pop());
        difficulty = Difficulty.valueOf(aCols.pop());
        ownInventory = Boolean.parseBoolean(aCols.pop());
        gameMode = GameMode.valueOf(aCols.pop());
        playerVsPlayer = Boolean.parseBoolean(aCols.pop());
        classificationName = aCols.pop();
        entitySpawnCheck = Boolean.parseBoolean(aCols.pop());
        String lStr = aCols.pop();
        naturalEntityTypes.clear();
        if (lStr != null && !lStr.isEmpty()) {
            String[] lParts = lStr.split("\\,");
            for(String lPart : lParts) {
                naturalEntityTypes.add(EntityType.valueOf(lPart));
            }
        }
        lStr = aCols.pop();
        customEntityTypes.clear();
        if (lStr != null && !lStr.isEmpty()) {
            String[] lParts = lStr.split("\\,");
            for(String lPart : lParts) {
                customEntityTypes.add(EntityType.valueOf(lPart));
            }
        }
        inventoryName = aCols.pop();
        noInventory = Boolean.parseBoolean(aCols.pop());
    }
    
    public WorldCreator getCreator() {
        WorldCreator lCreator = new WorldCreator(name);
        lCreator.generateStructures(generateStructures);
        if (generator != null && !generator.isEmpty()) {
            lCreator.generator(generator);
        }
        lCreator.seed(seed);
        lCreator.type(type);
        lCreator.environment(environment);
        return lCreator;
    }
    
    public void updateToWorld() {
        World lWorld = Framework.plugin.getServer().getWorld(name);
        if (lWorld != null) {
            lWorld.setSpawnFlags(spawnMonsters, spawnAnimals);
            lWorld.setDifficulty(difficulty);
            lWorld.setPVP(playerVsPlayer);
        }
    }
    
    public void updateFromWorld() {
        World lWorld = Framework.plugin.getServer().getWorld(name);
        if (lWorld != null) {
            generateStructures = lWorld.canGenerateStructures();
            //generator = lWorld.getGenerator().getClass().getName();
            seed = lWorld.getSeed();
            type = lWorld.getWorldType();
            environment = lWorld.getEnvironment();
            spawnAnimals = lWorld.getAllowAnimals();
            spawnMonsters = lWorld.getAllowMonsters();
            difficulty = lWorld.getDifficulty();
            playerVsPlayer = lWorld.getPVP();
        }
    }

    public void updateFromClassification(WorldClassification aWC) {
        generateStructures = aWC.generateStructures;
        generator = aWC.generator;
        seed = aWC.seed;
        type = aWC.type;
        environment = aWC.environment;
        spawnAnimals = aWC.spawnAnimals;
        spawnMonsters = aWC.spawnMonsters;
        difficulty = aWC.difficulty;
        playerVsPlayer = aWC.playerVsPlayer;
        classificationName = aWC.name;
        ownInventory = aWC.ownInventory;
        entitySpawnCheck = aWC.entitySpawnCheck;
        naturalEntityTypes.clear();
        naturalEntityTypes.addAll(aWC.naturalEntityTypes);
        customEntityTypes.clear();
        customEntityTypes.addAll(aWC.customEntityTypes);
        inventoryName = aWC.inventoryName;
        noInventory = aWC.noInventory;
    }
    
    public boolean isEntityAllowed(boolean aNatural, EntityType aEntityType) {
        boolean lResult = true;
        if (entitySpawnCheck) {
            if (aNatural) {
                lResult = naturalEntityTypes.contains(aEntityType);
            } else {
                lResult = customEntityTypes.contains(aEntityType);
            }
        } else if (spawnAnimals && spawnMonsters) {
            lResult = true;
        } else if (!spawnAnimals && Framework.isAnimal(aEntityType)) {
            lResult = false;
        } else if (!spawnMonsters && !Framework.isAnimal(aEntityType) && aEntityType.isAlive()) {
            lResult = false;
        }
        return lResult;
    }
}
