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
public class EntityControlPathItemDestination extends EntityControlPathItem {
    protected BlockPosition destination;
    protected float speed = 0.3f;
    
    public EntityControlPathItemDestination() {
    }

    public EntityControlPathItemDestination(BlockPosition aDestination) {
        destination = aDestination;
    }

    public EntityControlPathItemDestination(BlockPosition aDestination, float aSpeed) {
        destination = aDestination;
        speed = aSpeed;
    }

    @Override
    public BlockPosition getDestination(Entity aEntity) {
        return destination;
    }

    @Override
    public float getSpeed(Entity aEntity) {
        return speed;
    }

}
