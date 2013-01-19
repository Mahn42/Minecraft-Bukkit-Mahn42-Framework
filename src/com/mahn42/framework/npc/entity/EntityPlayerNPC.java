/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import com.mahn42.framework.npc.network.EmptyConnection;
import com.mahn42.framework.npc.network.EmptyNetworkManager;
import com.mahn42.framework.npc.network.EmptySocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_4_R1.ChunkCoordinates;
import net.minecraft.server.v1_4_R1.Connection;
import net.minecraft.server.v1_4_R1.DamageSource;
import net.minecraft.server.v1_4_R1.EnchantmentManager;
import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntityMonster;
import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.EnumGamemode;
import net.minecraft.server.v1_4_R1.MathHelper;
import net.minecraft.server.v1_4_R1.MinecraftServer;
import net.minecraft.server.v1_4_R1.MobEffectList;
import net.minecraft.server.v1_4_R1.Navigation;
import net.minecraft.server.v1_4_R1.NetworkManager;
import net.minecraft.server.v1_4_R1.Packet32EntityLook;
import net.minecraft.server.v1_4_R1.PlayerInteractManager;
import net.minecraft.server.v1_4_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class EntityPlayerNPC extends EntityPlayer {

    public EntityPlayerNPC(MinecraftServer minecraftServer, World world, String string, PlayerInteractManager manager) {
        super(minecraftServer, world, string, manager);
        manager.setGameMode(EnumGamemode.SURVIVAL);
        this.canPickUpLoot = true;
        initialize(minecraftServer);
    }

    /*
    @Override
    public float bB() {
        return super.bB(); // * npc.getNavigator().getDefaultParameters().speed();
    }

    @Override
    public void collide(net.minecraft.server.v1_4_R1.Entity entity) {
        // this method is called by both the entities involved - cancelling
        // it will not stop the NPC from moving.
        super.collide(entity);
        //if (npc != null)
        //    Util.callCollisionEvent(npc, entity);
    }
    */
    
    @Override
    public CraftPlayer getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = new NPCEntityPlayer(this);
            //Logger.getAnonymousLogger().info("new NPCEntity " + bukkitEntity.getEntityId());
        }
        return super.getBukkitEntity();
    }

    private void initialize(MinecraftServer minecraftServer) {
        Socket socket = new EmptySocket();
        NetworkManager netMgr = null;
        Connection lConn = new Connection()
        {
            @Override
            public boolean a() {
                return false;
            }
        };
        try {
            netMgr = new EmptyNetworkManager(socket, "npc mgr", lConn, server.F().getPrivate());
            playerConnection = new EmptyConnection(minecraftServer, netMgr, this);
            netMgr.a(playerConnection);
        } catch (IOException ex) {
            Logger.getLogger(EntityHumanNPC.class.getName()).log(Level.SEVERE, null, ex);
        }

        getNavigation().e(true);
        X = 0.5F; // stepHeight - must not stay as the default 0 (breaks steps).
                  // Check the EntityPlayer constructor for the new name.

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "initialize", ex);
        }
    }

    @Override
    public void j_() {
        super.j_();
        Packet32EntityLook packet = new Packet32EntityLook(id, (byte) MathHelper.d(yaw * 256.0F / 360.0F), (byte) MathHelper.d(pitch * 256.0F / 360.0F));
        double radius = 64.0d * 64.0d;
        Location location = getBukkitEntity().getLocation();
        final org.bukkit.World lworld = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || lworld != ply.getWorld()) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }
            ((CraftPlayer) ply).getHandle().playerConnection.sendPacket(packet);
        }
                
        
        if (Math.abs(motX) < EPSILON && Math.abs(motY) < EPSILON && Math.abs(motZ) < EPSILON) {
            motX = motY = motZ = 0;
        }
        this.aA().a();
        Navigation navigation = getNavigation();
        if (navigation != null) {
            if (!navigation.f()) {
                navigation.e();
            }
        }
        moveOnCurrentHeading();
        if (motX != 0 || motZ != 0 || motY != 0) {
            e(0, 0);
        } // is this necessary? it does controllable but sometimes
        if (noDamageTicks > 0) {
            --noDamageTicks;
        }
    }

    private void moveOnCurrentHeading() {
        //NMS.updateAI(this);
        //updateSenses(entity);
        //if (Math.abs(motX) > 0.1f || Math.abs(motY) > 0.1f || Math.abs(motZ) > 0.1f ) {
        //    Framework.plugin.getLogger().info("motX = " + motX + " motY = " + motY + " motZ = " + motZ);
        //}
        this.aA().a();
        this.getNavigation().e();
        this.getControllerMove().c();
        this.getControllerLook().a();
        this.getControllerJump().b();
        // taken from EntityLiving update method
        
        if (bF) {
             boolean inLiquid = H() || J();
             if (inLiquid) {
                 motY += 0.04;
             } else //(handled elsewhere)*/
            if (onGround && bV == 0) {
                bi();
                bV = 10;
            }
        } else
            bV = 0;

        bC *= 0.98F;
        bD *= 0.98F;
        bE *= 0.9F;

        float prev = aN;
        aN *= bB();
        e(bC, bD); // movement method
        aN = prev;
        
        //NMS.setHeadYaw(this, yaw);
        this.az = yaw;
    }

    private static final float EPSILON = 0.005F;

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

    /*
    public void attack(EntityLiving target) {
        int damage = 2;

        if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
            damage += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
        }

        if (this.hasEffect(MobEffectList.WEAKNESS)) {
            damage -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
        }

        int knockbackLevel = 0;

        if (target instanceof EntityLiving) {
            damage += EnchantmentManager.a(this, target);
            knockbackLevel += EnchantmentManager.getKnockbackEnchantmentLevel(this, target);
        }

        boolean success = target.damageEntity(DamageSource.mobAttack(this), damage);

        if (!success) {
            return;
        }
        if (knockbackLevel > 0) {
            target.g(-MathHelper.sin((float) (this.yaw * Math.PI / 180.0F)) * knockbackLevel * 0.5F, 0.1D,
                    MathHelper.cos((float) (this.yaw * Math.PI / 180.0F)) * knockbackLevel * 0.5F);
            this.motX *= 0.6D;
            this.motZ *= 0.6D;
        }

        int fireAspectLevel = EnchantmentManager.getFireAspectEnchantmentLevel(this);

        if (fireAspectLevel > 0) {
            target.setOnFire(fireAspectLevel * 4);
        }
    }*/
}
