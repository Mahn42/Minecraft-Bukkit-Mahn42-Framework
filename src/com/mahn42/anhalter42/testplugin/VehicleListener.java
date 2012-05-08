/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
 
/**
 *
 * @author andre
 */
public class VehicleListener implements Listener  {

    public Testplugin plugin;
    
    public VehicleListener(Testplugin aPlugin) {
        plugin = aPlugin;
    }

    /*
    @EventHandler
    public void vehicleCollision(VehicleEntityCollisionEvent event) {
        Vehicle lV = event.getVehicle();
        EntityType lET = lV.getType();
        if ( lET == EntityType.BOAT ) {
            plugin.log.info("collision boat canceled.");
            lV.setVelocity(new Vector(0,0,0));
            event.setCancelled(true);
        }
    } 
    */
    
    @EventHandler
    public void vehicleDestroy(VehicleDestroyEvent event) {
        Vehicle lV = event.getVehicle();
        EntityType lET = lV.getType();
        if ( lET == EntityType.BOAT ) {
            if (event.getAttacker() == null) {
                plugin.log.info("destroy canceled.");
                lV.setVelocity(new Vector(0,0,0));
                event.setCancelled(true);
            } else {
                plugin.log.info("boat droped.");
                lV.remove();
                event.setCancelled(true);
                ItemStack boatStack = new ItemStack(Material.BOAT, 1);
                Location loc = lV.getLocation();
                loc.getWorld().dropItemNaturally(loc, boatStack);
            }
        }
    } 
}
