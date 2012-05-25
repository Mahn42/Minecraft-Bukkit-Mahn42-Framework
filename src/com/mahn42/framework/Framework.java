/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

//import com.mahn42.framework.api.FrameworkAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Framework extends JavaPlugin { //implements FrameworkAPI {

    public int configSyncBlockSetterTicks = 2;
    
    protected SyncBlockSetter fSyncBlockSetter;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      /* Kommentar von Spielername steht hier drin! 20:53*/
    }

    @Override
    public void onEnable() { 
        readFrameworkConfig();
        fSyncBlockSetter = new SyncBlockSetter(this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    //@Override
    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        fSyncBlockSetter.setTypeAndData(aLocation, aMaterial, aData, aPhysics);
    }
    
    private void readFrameworkConfig() {
        FileConfiguration lConfig = getConfig();
        configSyncBlockSetterTicks = lConfig.getInt("SyncBlockSetter.Ticks");
    }
}
