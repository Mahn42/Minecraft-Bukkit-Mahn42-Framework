/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Framework extends JavaPlugin {

    public static Framework plugin;
    public int configSyncBlockSetterTicks = 2;
    public int configDBSaverTicks = 18000;
    
    protected HashMap<String, BlockPosition> fPositionMarker = new HashMap<String, BlockPosition>();
    protected HashMap<String, Boolean> fDebugSet = new HashMap<String, Boolean>();
    protected SyncBlockSetter fSyncBlockSetter;
    protected DBSaverTask fSaverTask;
    protected DynMapBuildingRenderer fDynMapTask;
    protected BuildingDetector fBuildingDetector;
    protected HashMap<String, PlayerBuildings> fPlayerBuildings;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlockArea lArea = new BlockArea(10, 10, 10);
        Logger.getAnonymousLogger().info("Area Item 0 = " + lArea.items.get(0));
    }

    public boolean isDebugSet(String aName) {
        Boolean lValue = fDebugSet.get(aName);
        return lValue != null && lValue.booleanValue();
    }
    
    public void setDebugSet(String aName, boolean aValue) {
        fDebugSet.put(aName, new Boolean(aValue));
    }
    
    public void setPositionMarker(String aName, BlockPosition aPos) {
        fPositionMarker.put(aName, aPos);
    }
    
    public BlockPosition getPositionMarker(String aName) {
        return fPositionMarker.get(aName);
    }
    
    @Override
    public void onEnable() { 
        plugin = this;
        fPlayerBuildings = new HashMap<String, PlayerBuildings>();
        fBuildingDetector = new BuildingDetector();
        readFrameworkConfig();
        fSyncBlockSetter = new SyncBlockSetter();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
        fSaverTask = new DBSaverTask();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSaverTask, 100, configDBSaverTicks);
        fDynMapTask = new DynMapBuildingRenderer();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fDynMapTask, 100, 20);
        
        getCommand("fw_bd_dump").setExecutor(new CommandBD_Dump());
        getCommand("fw_bd_create").setExecutor(new CommandBD_Create());
        getCommand("fw_bd_detect").setExecutor(new CommandBD_Detect());
        getCommand("fw_set_spawn").setExecutor(new CommandSetSpawn());
        getCommand("fw_save").setExecutor(new CommandSave());
        getCommand("fw_debug").setExecutor(new CommandDebugSet());
        getCommand("fw_markpos").setExecutor(new CommandSetPosMarker());
        getCommand("fw_area_load").setExecutor(new CommandAreaLoad());
        getCommand("fw_area_save").setExecutor(new CommandAreaSave());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {
        fSaverTask.run();
        getServer().getScheduler().cancelTasks(this);
    }

    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        fSyncBlockSetter.setTypeAndData(aLocation, aMaterial, aData, aPhysics);
    }
    
    public BuildingDescription getBuildingDescription(String aName) {
        for(BuildingDescription lDesc : fBuildingDetector.fDescriptions) {
            if (aName.equalsIgnoreCase(lDesc.name)) {
                return lDesc;
            }
        }
        return null;
    }
    
    public BuildingDetector getBuildingDetector() {
        return fBuildingDetector;
    }
    
    public PlayerBuildings getPlayerBuildings(String aPlayerName) {
        return getPlayerBuildings(getServer().getPlayer(aPlayerName));
    }

    public PlayerBuildings getPlayerBuildings(Player aPlayer) {
        PlayerBuildings lBuildings = fPlayerBuildings.get(aPlayer.getName());
        if (lBuildings == null) {
            lBuildings = new PlayerBuildings(aPlayer.getName());
            lBuildings.playerPos = new BlockPosition(aPlayer.getLocation());
            fPlayerBuildings.put(lBuildings.playerName, lBuildings);
        }
        return lBuildings;
    }

    public void registerSaver(DBSave aSaver) {
        fSaverTask.registerSaver(aSaver);
    }
    
    public void unregisterSaver(DBSave aSaver) {
        fSaverTask.unregisterSaver(aSaver);
    }

    private void readFrameworkConfig() {
        FileConfiguration lConfig = getConfig();
        configSyncBlockSetterTicks = lConfig.getInt("SyncBlockSetter.Ticks");
    }
    
    public void log(String aDebugOption, String aText) {
        if (isDebugSet(aDebugOption)) {
            getLogger().info(aText);
        }
    }
    
    public static boolean isSpade(Material aMaterial) {
        return aMaterial != null
                && (aMaterial.equals(Material.STONE_SPADE)
                || aMaterial.equals(Material.WOOD_SPADE)
                || aMaterial.equals(Material.IRON_SPADE)
                || aMaterial.equals(Material.DIAMOND_SPADE)
                || aMaterial.equals(Material.GOLD_SPADE));
    }

    public static boolean isAxe(Material aMaterial) {
        return aMaterial != null
                && (aMaterial.equals(Material.STONE_AXE)
                || aMaterial.equals(Material.WOOD_AXE)
                || aMaterial.equals(Material.IRON_AXE)
                || aMaterial.equals(Material.DIAMOND_AXE)
                || aMaterial.equals(Material.GOLD_AXE));
    }
}
