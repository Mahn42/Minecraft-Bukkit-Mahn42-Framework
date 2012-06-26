/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Framework extends JavaPlugin {

    public static Framework plugin;
    public int configSyncBlockSetterTicks = 2;
    public int configDBSaverTicks = 18000;
    
    protected SyncBlockSetter fSyncBlockSetter;
    protected DBSaverTask fSaverTask;
    protected DynMapBuildingRenderer fDynMapTask;
    protected BuildingDetector fBuildingDetector;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }

    @Override
    public void onEnable() { 
        plugin = this;
        fBuildingDetector = new BuildingDetector();
        readFrameworkConfig();
        fSyncBlockSetter = new SyncBlockSetter();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
        fSaverTask = new DBSaverTask();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSaverTask, 100, configDBSaverTicks);
        fDynMapTask = new DynMapBuildingRenderer();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fDynMapTask, 100, 20);
        

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
