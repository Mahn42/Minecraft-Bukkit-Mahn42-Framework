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
    }
}
