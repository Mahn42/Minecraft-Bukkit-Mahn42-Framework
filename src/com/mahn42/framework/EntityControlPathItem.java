/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.entity.Entity;

/**
 *
 * @author andre
 */
public abstract class EntityControlPathItem {
    public long maxTicks = -1;
    public long stayTicks = 0;
    
    public EntityControlPathItem() {
    }

    public abstract BlockPosition getDestination(Entity entity);

    public abstract float getSpeed(Entity aEntity);
    
    public boolean shouldStay(Entity aEntity) {
        stayTicks--;
        return stayTicks > 0;
    }
}
