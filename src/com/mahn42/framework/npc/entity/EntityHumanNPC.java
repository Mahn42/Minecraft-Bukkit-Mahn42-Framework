/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import net.minecraft.server.v1_4_6.ChunkCoordinates;
import net.minecraft.server.v1_4_6.EntityHuman;
import net.minecraft.server.v1_4_6.MathHelper;
import net.minecraft.server.v1_4_6.World;
import org.bukkit.entity.HumanEntity;

/**
 *
 * @author andre
 */
public class EntityHumanNPC extends EntityHuman {

    public EntityHumanNPC(World world, String aName) {
        super(world);
        name = aName;
    }

    @Override
    public HumanEntity getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = new NPCEntityHuman(this);
        }
        return super.getBukkitEntity();
    }

    @Override
    public void sendMessage(String string) {
        //TODO send message to owner player?
    }

    @Override
    public boolean a(int i, String string) {
        return false;
    }

    @Override
    public ChunkCoordinates b() {
        return new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY + 0.5D), MathHelper.floor(this.locZ));
    }
}
