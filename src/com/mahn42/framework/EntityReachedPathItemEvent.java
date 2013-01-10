/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author andre
 */
public class EntityReachedPathItemEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    protected Entity entity;
    protected EntityControlPathItem item;
    protected EntityControl control;
    
    public EntityReachedPathItemEvent(Entity aEntity, EntityControl aControl, EntityControlPathItem aItem) {
        entity = aEntity;
        control = aControl;
        item = aItem;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public EntityControlPathItem getPathItem() {
        return item;
    }

    public EntityControl getEntityControl() {
        return control;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public void raise() {
        Framework.plugin.log("fw", "ERPI: " + entity.getEntityId() + " " + item.getDestination(entity) + " " + control.path.size());
        Framework.plugin.getServer().getPluginManager().callEvent(this);
    }
}
