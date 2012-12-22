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
import net.minecraft.server.v1_4_6.Connection;
import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.EnumGamemode;
import net.minecraft.server.v1_4_6.MinecraftServer;
import net.minecraft.server.v1_4_6.Navigation;
import net.minecraft.server.v1_4_6.NetworkManager;
import net.minecraft.server.v1_4_6.PlayerInteractManager;
import net.minecraft.server.v1_4_6.World;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;

/**
 *
 * @author andre
 */
public class EntityHumanNPC extends EntityPlayer {
    
    public EntityHumanNPC(MinecraftServer minecraftServer, World world, String string, PlayerInteractManager manager) {
        super(minecraftServer, world, string, manager);
        manager.setGameMode(EnumGamemode.SURVIVAL);
        initialize(minecraftServer);
    }

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

    @Override
    public void g(double x, double y, double z) {
        super.g(x, y, z);
        /*
        if (npc == null) {
            super.g(x, y, z);
            return;
        }
        if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
            if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                super.g(x, y, z);
            return;
        }
        Vector vector = new Vector(x, y, z);
        NPCPushEvent event = Util.callPushEvent(npc, vector);
        if (!event.isCancelled()) {
            vector = event.getCollisionVector();
            super.g(vector.getX(), vector.getY(), vector.getZ());
        }
        // when another entity collides, this method is called to push the
        // NPC so we prevent it from doing anything if the event is
        // cancelled.
        */
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = new NPCEntity(this);
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
        this.aA().a();
        this.getNavigation().e();
        this.getControllerMove().c();
        this.getControllerLook().a();
        this.getControllerJump().b();
        // taken from EntityLiving update method
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
        e(bB, bC); // movement method
        aM = prev;
        //NMS.setHeadYaw(this, yaw);
        this.ay = yaw;
    }

    private static final float EPSILON = 0.005F;

}
