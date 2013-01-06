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
public class EntityControlPathItemRelative extends EntityControlPathItem {

    protected BlockPositionDelta delta;
    protected BlockPosition destination;
    protected float speed = -1.0f;
    
    public EntityControlPathItemRelative(BlockPositionDelta aDelta) {
        delta = aDelta;
    }
    
    public EntityControlPathItemRelative(BlockPositionDelta aDelta, float aSpeed) {
        delta = aDelta;
        speed = aSpeed;
    }
    
    @Override
    public BlockPosition getDestination(Entity aEntity) {
        if (destination == null) {
            destination = new BlockPosition(aEntity.getLocation());
            destination.add(delta);
        }
        return destination;
    }

    @Override
    public float getSpeed(Entity aEntity) {
        return getDefaultSpeed(aEntity, speed);
    }
    
}
