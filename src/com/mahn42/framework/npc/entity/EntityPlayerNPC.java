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
import net.minecraft.server.v1_4_6.ChunkCoordinates;
import net.minecraft.server.v1_4_6.Connection;
import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.EnumGamemode;
import net.minecraft.server.v1_4_6.MathHelper;
import net.minecraft.server.v1_4_6.MinecraftServer;
import net.minecraft.server.v1_4_6.Navigation;
import net.minecraft.server.v1_4_6.NetworkManager;
import net.minecraft.server.v1_4_6.Packet32EntityLook;
import net.minecraft.server.v1_4_6.PlayerInteractManager;
import net.minecraft.server.v1_4_6.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
    public void collide(net.minecraft.server.v1_4_6.Entity entity) {
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
        /*
        if (be()) {
            boolean inLiquid = H() || J();
            if (inLiquid) {
                motY += 0.04;
            } else //(handled elsewhere)
            if (onGround && bU == 0) {
                bi();
                bU = 10;
            }
        } else
            bU = 0;

        bB *= 0.98F;
        bC *= 0.98F;
        bD *= 0.9F;

        float prev = aM;
        aM *= bB();
        if (bB != 0.0f || bC != 0.0f) {
            Framework.plugin.getLogger().info("e(bB, bC) => " + bB + ", " + bC);
        }
        e(bB, bC); // movement method
        aM = prev;
        */
        
        if (bF) {
            /* boolean inLiquid = H() || J();
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
        this.ay = yaw;
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

}
