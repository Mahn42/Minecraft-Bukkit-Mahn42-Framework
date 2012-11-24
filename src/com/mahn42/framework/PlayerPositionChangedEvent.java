/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author andre
 */
public class PlayerPositionChangedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private BlockPosition from;
    private BlockPosition to;
    
    public PlayerPositionChangedEvent(Player aPlayer, BlockPosition aFrom, BlockPosition aTo) {
        player = aPlayer;
        from = aFrom.clone();
        to = aTo.clone();
    }
    
    public Player getPlayer() {
        return player;
    }
 
    public BlockPosition getFrom() {
        return from;
    }

    public BlockPosition getTo() {
        return to;
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
