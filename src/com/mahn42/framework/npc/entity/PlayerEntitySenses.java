/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

/**
 *
 * @author andre
 */
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityLiving;

public class PlayerEntitySenses {
    EntityLiving entity;
    List<Entity> seenEntities = new ArrayList<Entity>();
    List<Entity> unseenEntities = new ArrayList<Entity>();

    public PlayerEntitySenses(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
    }

    public void a() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    public boolean canSee(Entity entity) {
        if (this.seenEntities.contains(entity)) {
            return true;
        } else if (this.unseenEntities.contains(entity)) {
            return false;
        } else {
            boolean flag = this.entity.o(entity);
            if (flag) {
                this.seenEntities.add(entity);
            } else {
                this.unseenEntities.add(entity);
            }
            return flag;
        }
    }
}