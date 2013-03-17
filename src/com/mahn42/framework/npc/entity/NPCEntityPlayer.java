/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.Framework;
import java.util.List;
import net.minecraft.server.v1_5_R1.EntityPlayer;
import net.minecraft.server.v1_5_R1.EnumBedResult;
import net.minecraft.server.v1_5_R1.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R1.CraftServer;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftSheep;
import org.bukkit.craftbukkit.v1_5_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
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

    public void goSleep(BlockPosition aPos) {
        EntityPlayer humanHandle = getHandle();
        EnumBedResult res = humanHandle.a(aPos.x, aPos.y, aPos.z);
        if (res != EnumBedResult.OK) {
            goSleep();
            Framework.plugin.log("fw", "go Sleep returns " + res);
        }
    }

    /*
    public void awake() {
        EntityPlayer humanHandle = getHandle();
        PlayerAnimation.STOP_SLEEPING.play(humanHandle.getBukkitEntity());
    }
    */

    public void awake() {
        EntityPlayer humanHandle = getHandle();
        humanHandle.a(true, true, true);
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
    
    public void shearSheep(Sheep aSheep) {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        ((CraftSheep)aSheep).getHandle().a(lNPC);
    }
    
    public void setEquipment(int i, ItemStack aItem) {
        EntityPlayerNPC lNPC = (EntityPlayerNPC) getHandle();
        lNPC.setEquipment(i, CraftItemStack.asNMSCopy(aItem));
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NPCEntityPlayer) ? getAsPlayer().getEntityId() == ((NPCEntityPlayer)obj).getAsPlayer().getEntityId() : false;
    }
}
