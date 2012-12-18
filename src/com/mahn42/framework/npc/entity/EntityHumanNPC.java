/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import com.mahn42.framework.npc.network.EmptyNetHandler;
import com.mahn42.framework.npc.network.EmptyNetworkManager;
import com.mahn42.framework.npc.network.EmptySocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_4_5.EntityPlayer;
import net.minecraft.server.v1_4_5.EnumGamemode;
import net.minecraft.server.v1_4_5.ItemInWorldManager;
import net.minecraft.server.v1_4_5.MinecraftServer;
import net.minecraft.server.v1_4_5.Navigation;
import net.minecraft.server.v1_4_5.NetHandler;
import net.minecraft.server.v1_4_5.NetworkManager;
import net.minecraft.server.v1_4_5.World;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer;

/**
 *
 * @author andre
 */
public class EntityHumanNPC extends EntityPlayer {
    
    public EntityHumanNPC(MinecraftServer minecraftServer, World world, String string,
            ItemInWorldManager itemInWorldManager) {
        super(minecraftServer, world, string, itemInWorldManager);
        itemInWorldManager.setGameMode(EnumGamemode.SURVIVAL);
        initialize(minecraftServer);
    }

    @Override
    public float bB() {
        return super.bB(); // * npc.getNavigator().getDefaultParameters().speed();
    }

    @Override
    public void collide(net.minecraft.server.v1_4_5.Entity entity) {
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
            //Logger.getAnonymousLogger().info("new NPCEntity");
            bukkitEntity = new NPCEntity(this);
        }
        return super.getBukkitEntity();
    }

    private void initialize(MinecraftServer minecraftServer) {
        Socket socket = new EmptySocket();
        NetworkManager netMgr = null;
        try {
            netMgr = new EmptyNetworkManager(socket, "npc mgr", new NetHandler() {
                @Override
                public boolean a() {
                    return false;
                }
            }, server.F().getPrivate());
            netServerHandler = new EmptyNetHandler(minecraftServer, netMgr, this);
            netMgr.a(netServerHandler);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "initialize", e);
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
        if (Math.abs(motX) < EPSILON && Math.abs(motY) < EPSILON && Math.abs(motZ) < EPSILON)
            motX = motY = motZ = 0;
        this.aA().a();
        Navigation navigation = getNavigation();
        if (navigation != null) {
            if (!navigation.f())
                navigation.e();
        }
        moveOnCurrentHeading();
        if (motX != 0 || motZ != 0 || motY != 0)
            e(0, 0); // is this necessary? it does controllable but sometimes
        if (noDamageTicks > 0)
            --noDamageTicks;
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
        if (bE) {
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
