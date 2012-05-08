/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author andre
 */
public class BlockListener implements Listener {
    
    public Testplugin plugin;

    public BlockListener(Testplugin aPlugin) {
         plugin = aPlugin;
    }
            
    @EventHandler
    public void place(BlockPlaceEvent aEvent) {
        plugin.getServer().broadcastMessage("placing " + new Integer(aEvent.getBlockPlaced().getTypeId()));
    }

    @EventHandler
    public void signChange(SignChangeEvent aEvent) {
        plugin.getServer().broadcastMessage("sign change " + aEvent.getLines().toString());
        //aEvent.getPlayer().getWorld().
    }

    @EventHandler
    public void fromTo(BlockFromToEvent aEvent) {
        //plugin.getServer().broadcastMessage("from to " + aEvent.getBlock().getLocation().toString() + " -> " + aEvent.getToBlock().getType().toString());
        if (aEvent.getBlock().getType() == Material.WATER) {
            //Location lLoc = aEvent.getToBlock().getLocation();
            //lLoc.setY(lLoc.getY() + 1.0);
            //lLoc.getBlock().setType(Material.WATER);
            //aEvent.getToBlock().setType(Material.WATER);
        }
        //aEvent.getPlayer().getWorld().
    }
}
