/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Testplugin extends JavaPlugin {

    public Logger log;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    @Override
    public void onEnable() { 
        log = this.getLogger();
	//log.info("anhalter42 plugin has been enabled!");
        this.getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        //this.getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        //TestRunner lRunner = new TestRunner(this);
        //TestASyncRunner lAsyncRunner = new TestASyncRunner(this, lRunner);
        //this.getServer().getScheduler().scheduleSyncRepeatingTask(this, lRunner, 10, 10);
        //this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, lAsyncRunner, 1, 10);
    }
 
    @Override
    public void onDisable() { 
        //log.info("anhalter42 plugin has been disabled.");
    }
    
}
