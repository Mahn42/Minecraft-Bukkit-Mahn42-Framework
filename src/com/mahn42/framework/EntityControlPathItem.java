/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

    public float getDefaultSpeed(Entity aEntity, float aSpeed) {
        if (aSpeed < 0) {
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
                return 0.3f;
            }
        } else {
            return aSpeed;
        }
    }
}
