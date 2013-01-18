/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import java.util.List;
import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_6.CraftServer;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author andre
 */
public class NPCEntityPlayer extends CraftPlayer {

    protected Object fDataObject = null;

    public Object getDataObject() {
        return fDataObject;
    }

    public void setDataObject(Object aObject) {
        fDataObject = aObject;
    }

    public Player getAsPlayer() {
        return this;
    }

    public void setMot(float x, float y, float z) {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        lNPC.motX = x;
        lNPC.motY = y;
        lNPC.motZ = z;
    }

    public NPCEntityPlayer(EntityPlayer entity) {
        super((CraftServer) Bukkit.getServer(), entity);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public void swingArm() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.ARM_SWING.play(humanHandle.getBukkitEntity());
    }

    public void sitDown() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.SIT.play(humanHandle.getBukkitEntity());
    }

    public void standUp() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.STOP_SITTING.play(humanHandle.getBukkitEntity());
    }

    public void goSleep() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.SLEEP.play(humanHandle.getBukkitEntity());
    }

    public void awake() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.STOP_SLEEPING.play(humanHandle.getBukkitEntity());
    }

    public void jump() {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        lNPC.motY += 0.3;
    }

    public void swim() {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        lNPC.motY += 0.6;
    }
    
    public void attack(Entity aEntity) {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        lNPC.attack(((CraftEntity)aEntity).getHandle());
    }
    
    public void stop() {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        setMot(0, 0, 0);
        lNPC.getNavigation().a((PathEntity)null, 0.0f);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NPCEntityPlayer) ? getAsPlayer().getEntityId() == ((NPCEntityPlayer)obj).getAsPlayer().getEntityId() : false;
    }
}
