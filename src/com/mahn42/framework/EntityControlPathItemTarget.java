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
public class EntityControlPathItemTarget extends EntityControlPathItem {

    protected Entity entity;
    protected float speed = 0.3f;

    public EntityControlPathItemTarget(Entity aEntity) {
        entity = aEntity;
    }
    
    public EntityControlPathItemTarget(Entity aEntity, float aSpeed) {
        entity = aEntity;
        speed = aSpeed;
    }
    
    @Override
    public BlockPosition getDestination(Entity aEntity) {
        return new BlockPosition(entity.getLocation());
    }

    @Override
    public float getSpeed(Entity aEntity) {
        return speed;
    }
    
}
