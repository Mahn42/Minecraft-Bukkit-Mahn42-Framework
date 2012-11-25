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
public class EntityControlPathItem {
    protected BlockPosition destination;
    protected float speed = 0.3f;
    public long maxTicks = -1;
    public long stayTicks = 0;
    
    public EntityControlPathItem() {
    }

    public EntityControlPathItem(BlockPosition aDestination) {
        destination = aDestination;
    }

    public EntityControlPathItem(BlockPosition aDestination, float aSpeed) {
        destination = aDestination;
        speed = aSpeed;
    }

    public BlockPosition getDestination(Entity entity) {
        return destination;
    }

    public float getSpeed(Entity entity) {
        return speed;
    }
    
    public boolean shouldStay(Entity entity) {
        stayTicks--;
        return stayTicks > 0;
    }
}
