/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author andre
 */
public class PlayerManagerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private PlayerManager playerManager;
    
    public PlayerManagerEvent(PlayerManager aPlayerManager) {
        playerManager = aPlayerManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager; // should be override
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public void raise() {
        Framework.plugin.getServer().getPluginManager().callEvent(this);
    }
}
