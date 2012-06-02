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
    
    protected SyncBlockSetter fSyncBlockSetter;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }

    @Override
    public void onEnable() { 
        plugin = this;
        readFrameworkConfig();
        fSyncBlockSetter = new SyncBlockSetter();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        fSyncBlockSetter.setTypeAndData(aLocation, aMaterial, aData, aPhysics);
    }
    
    private void readFrameworkConfig() {
        FileConfiguration lConfig = getConfig();
        configSyncBlockSetterTicks = lConfig.getInt("SyncBlockSetter.Ticks");
    }
}
