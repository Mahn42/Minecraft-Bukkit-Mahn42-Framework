/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class EntityControlPathItemRelative extends EntityControlPathItem {

    protected BlockPositionDelta delta;
    protected BlockPosition destination;
    protected float speed = 0.5f;
    
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
        if (aEntity instanceof Player) {
            Player lPlayer = (Player)aEntity;
            if (lPlayer.isSneaking()) {
                return ((CraftPlayer)aEntity).getWalkSpeed() * 0.5f;
            } else if (lPlayer.isSprinting()) {
                return ((CraftPlayer)aEntity).getWalkSpeed() * 1.5f;
            } else {
                return ((CraftPlayer)aEntity).getWalkSpeed();
            }
        } else {
            return speed;
        }
    }
    
}
