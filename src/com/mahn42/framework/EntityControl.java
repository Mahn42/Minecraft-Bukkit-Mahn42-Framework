/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import net.minecraft.server.v1_4_6.EntityCreature;
import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.Navigation;
import net.minecraft.server.v1_4_6.PathEntity;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 *
 * @author andre
 */
public class EntityControl {
    
    public int id;
    public Entity entity;
    public boolean active = true;
    public boolean remove = false;
    
    public EntityControlPath path = new EntityControlPath();

    protected EntityControlPathItem pathItem;
    
    public EntityControl(Entity aEntity) {
        entity = aEntity;
        id = entity.getEntityId();
    }
    
    public void run() {
        if (entity.isDead()) {
            remove = true;
            return;
        }
        if (pathItem == null) {
            getNextPathItem();
        }
        if (pathItem != null) {
            BlockPosition lDest = pathItem.getDestination(entity);
            BlockPosition lEntityPos = new BlockPosition(entity.getLocation());
            while (pathItem != null && lDest != null && lDest.nearly(lEntityPos)) {
                EntityReachedPathItemEvent lEvent = new EntityReachedPathItemEvent(entity, this, pathItem);
                lEvent.raise();
                if (pathItem.shouldStay(entity)) {
                    break;
                }
                getNextPathItem();
                if (pathItem != null) {
                    lDest = pathItem.getDestination(entity);
                }
            }
            if (pathItem != null && lDest != null) {
                net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity)entity).getHandle();
                if (lMCEntitiy instanceof EntityCreature) {
                    EntityCreature lCreature = (EntityCreature)lMCEntitiy;
                    PathEntity lPE = ((CraftWorld)entity.getWorld()).getHandle().a(lCreature, lDest.x, lDest.y, lDest.z, 100.0f, true, false, false, true);
                    lCreature.setPathEntity(lPE);
                    Navigation lNavigation = lCreature.getNavigation();
                    lNavigation.a(lPE, pathItem.getSpeed(entity));
                } else if (lMCEntitiy instanceof EntityPlayer) {
                    EntityPlayer lCreature = (EntityPlayer)lMCEntitiy;
                    PathEntity lPE = ((CraftWorld)entity.getWorld()).getHandle().a(lCreature, lDest.x, lDest.y, lDest.z, 100.0f, true, false, false, true);
                    //lCreature.setPathEntity(lPE);
                    Navigation lNavigation = lCreature.getNavigation();
                    lNavigation.a(lPE, pathItem.getSpeed(entity));
                } else {
                    //todo
                }
            }
        }
    }

    protected void getNextPathItem() {
        if (!path.isEmpty()) {
            pathItem = path.get(0);
            path.remove(0);
        } else {
            pathItem = null;
            remove = true;
        }
    }
}
