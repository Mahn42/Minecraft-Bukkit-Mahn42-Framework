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
import java.util.List;
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
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

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
        /*
        if (npc != null && bukkitEntity == null) {
            bukkitEntity = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
        }
        */
        if (bukkitEntity == null) {
            Logger.getAnonymousLogger().info("new PlayerNPC");
            bukkitEntity = new PlayerNPC(this);
        }
        return super.getBukkitEntity();
    }

    /*
    @Override
    public NPC getNPC() {
        return npc;
    }
    */

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
            // swallow
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "initialize", e);
        }

        getNavigation().e(true);
        X = 0.5F; // stepHeight - must not stay as the default 0 (breaks steps).
                  // Check the EntityPlayer constructor for the new name.

        try {
            socket.close();
        } catch (IOException ex) {
            // swallow
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "initialize", ex);
        }
    }

    @Override
    public void j_() {
        //super.j_();
        //if (npc == null)
        //    return;

        //updateEquipment();
        if (Math.abs(motX) < EPSILON && Math.abs(motY) < EPSILON && Math.abs(motZ) < EPSILON)
            motX = motY = motZ = 0;

        this.aA().a();
        //if (npc.getNavigator().isNavigating()) {
            Navigation navigation = getNavigation();
            if (navigation != null) {
                if (!navigation.f())
                    navigation.e();
            }
            moveOnCurrentHeading();
        //} else 
        if (motX != 0 || motZ != 0 || motY != 0)
            e(0, 0); // is this necessary? it does controllable but sometimes
                     // players sink into the ground

        //if (noDamageTicks > 0)
        //    --noDamageTicks;
        //npc.update();
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
/*
    private void updateEquipment() {
        for (int i = 0; i < previousEquipment.length; i++) {
            net.minecraft.server.ItemStack previous = previousEquipment[i];
            net.minecraft.server.ItemStack current = getEquipment(i);
            if (previous != current) {
                Util.sendPacketNearby(getBukkitEntity().getLocation(), new Packet5EntityEquipment(id, i,
                        current));
                previousEquipment[i] = current;
            }
        }
    }
    */

    public static class PlayerNPC extends CraftPlayer {

        private PlayerNPC(EntityHumanNPC entity) {
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
    }

    private static final float EPSILON = 0.005F;
}
