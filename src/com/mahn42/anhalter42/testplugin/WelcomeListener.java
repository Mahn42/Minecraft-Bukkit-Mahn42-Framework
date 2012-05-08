/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

/**
 *
 * @author andre
 */
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
 
public class WelcomeListener implements Listener {
    /*
    @EventHandler(priority = EventPriority.LOW)
    public void normalLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        p.getServer().broadcastMessage("Welcome " + p.getName());
    } 
    */
    @EventHandler(priority = EventPriority.LOW)
    public void playerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        //p.chat("Welcome " + p.getName());
        p.sendMessage("Welcome " + p.getName());
        //p.getServer().broadcastMessage("Welcome " + p.getName());
    } 
}
