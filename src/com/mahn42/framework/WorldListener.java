/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldSaveEvent;

/**
 *
 * @author andre
 */
public class WorldListener implements Listener {
    
    @EventHandler
    public void worldInit(WorldInitEvent aEvent) {
        World lWorld = aEvent.getWorld();
        WorldConfiguration lConf = Framework.plugin.getWorldConfigurationDB().getByName(lWorld.getName());
        if (lConf != null) {
            lConf.updateToWorld();
        }
    }
    
    @EventHandler
    public void worldSave(WorldSaveEvent aEvent) {
        World lWorld = aEvent.getWorld();
        WorldConfiguration lConf = Framework.plugin.getWorldConfigurationDB().getByName(lWorld.getName());
        if (lConf != null) {
            lConf.updateFromWorld();
        }
    }
}
