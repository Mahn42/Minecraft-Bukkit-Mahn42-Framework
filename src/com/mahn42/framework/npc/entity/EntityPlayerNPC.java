/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import com.mahn42.framework.Framework;
import com.mahn42.framework.npc.network.EmptyConnection;
import com.mahn42.framework.npc.network.EmptyNetworkManager;
import com.mahn42.framework.npc.network.EmptySocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_6_R3.AttributeInstance;
import net.minecraft.server.v1_6_R3.ChunkCoordinates;
import net.minecraft.server.v1_6_R3.Connection;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityHuman;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.EnumGamemode;
import net.minecraft.server.v1_6_R3.GenericAttributes;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.Navigation;
import net.minecraft.server.v1_6_R3.NetworkManager;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet32EntityLook;
import net.minecraft.server.v1_6_R3.Packet35EntityHeadRotation;
import net.minecraft.server.v1_6_R3.Packet5EntityEquipment;
import net.minecraft.server.v1_6_R3.PlayerInteractManager;
import net.minecraft.server.v1_6_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class EntityPlayerNPC extends EntityPlayer {

    protected PlayerControllerJump controllerJump;
    protected PlayerControllerLook controllerLook;
    protected PlayerControllerMove controllerMove;
    protected PlayerNavigation navigation;
    protected PlayerEntitySenses entitySenses;
    
    private int jumpTicks = 0;
    
    public EntityPlayerNPC(MinecraftServer minecraftServer, World world, String string, PlayerInteractManager manager) {
        super(minecraftServer, world, string, manager);
        manager.setGameMode(EnumGamemode.SURVIVAL);
        //this.canPickUpLoot = true;
        initialize(minecraftServer);
    }

    /*
     @Override
     public float bB() {
     return super.bB(); // * npc.getNavigator().getDefaultParameters().speed();
     }

     @Override
     public void collide(net.minecraft.server.v1_6_R3.Entity entity) {
     // this method is called by both the entities involved - cancelling
     // it will not stop the NPC from moving.
     super.collide(entity);
     //if (npc != null)
     //    Util.callCollisionEvent(npc, entity);
     }
     */
    
    public Navigation getNavigation() {
        return navigation;
    }
    
    public boolean isNavigating() {
        return true;
    }

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
        NetworkManager netMgr;
        Connection lConn = new Connection() {
            @Override
            public boolean a() {
                return false;
            }
        };
        try {
            netMgr = new EmptyNetworkManager(server.getLogger(), socket, "npc mgr", lConn, server.H().getPrivate());
            playerConnection = new EmptyConnection(minecraftServer, netMgr, this);
            netMgr.a(playerConnection);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        controllerJump = new PlayerControllerJump(this);
        controllerLook = new PlayerControllerLook(this);
        controllerMove = new PlayerControllerMove(this);
        entitySenses = new PlayerEntitySenses(this);
        navigation = new PlayerNavigation(this, world);
        getNavigation().e(true);
        X = 1; //0.5F; // stepHeight - must not stay as the default 0 (breaks steps).
        // Check the EntityPlayer constructor for the new name.

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "initialize", ex);
        }
        AttributeInstance range = this.getAttributeInstance(GenericAttributes.b);
        if (range == null) {
            range = this.aX().b(GenericAttributes.b);
        }
        //range.setValue(Setting.DEFAULT_PATHFINDING_RANGE.asDouble());
    }

    @Override
    public void l_() {
        super.l_();

        updateEquipment();
        if (Math.abs(motX) < EPSILON && Math.abs(motY) < EPSILON && Math.abs(motZ) < EPSILON) {
            motX = motY = motZ = 0;
        }
        this.getEntitySenses().a();
        /*
        Navigation navigation = getNavigation();
        if (navigation != null) {
            if (!navigation.f()) {
                navigation.e();
            }
        }
        */
        moveOnCurrentHeading();
        if (motX != 0 || motZ != 0 || motY != 0) {
            e(0, 0);
        } // is this necessary? it does controllable but sometimes
        if (noDamageTicks > 0) {
            --noDamageTicks;
        }
    }
    
    public PlayerControllerJump getControllerJump() {
        return controllerJump;
    }


    private void moveOnCurrentHeading() {
        updateAI();
        if (bd) {
            /* boolean inLiquid = H() || J();
             if (inLiquid) {
                 motY += 0.04;
             } else //(handled elsewhere)*/
            if (onGround && jumpTicks == 0) {
                be();
                jumpTicks = 10;
            }
        } else {
            jumpTicks = 0;
        }
        be *= 0.98F;
        bf *= 0.98F;
        bg *= 0.9F;

        e(be, bf); // movement method
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        this.aP = yaw;
        if (!(this instanceof EntityHuman))
            this.aN = yaw;
        this.aQ = yaw;
    }
    private static final float EPSILON = 0.005F;

    @Override
    public boolean a(int i, String string) {
        return false;
    }

    @Override
    public ChunkCoordinates b() {
        return new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY + 0.5D), MathHelper.floor(this.locZ));
    }
    net.minecraft.server.v1_6_R3.ItemStack[] previousEquipment = new net.minecraft.server.v1_6_R3.ItemStack[5];

    private void updateEquipment() {
        for (int i = 0; i < previousEquipment.length; i++) {
            net.minecraft.server.v1_6_R3.ItemStack previous = previousEquipment[i];
            net.minecraft.server.v1_6_R3.ItemStack current = getEquipment(i);
            if (previous != current) {
                Framework.plugin.log("npc", "update Equi for entity " + id + " from " + previous + " to " + current);
                sendPacketNearby(getBukkitEntity().getLocation(), new Packet5EntityEquipment(id, i, current));
                previousEquipment[i] = current;
            }
        }
    }

    public void setMoveDestination(double x, double y, double z, float speed) {
        controllerMove.a(x, y, z, speed);
    }

    public void setShouldJump() {
        controllerJump.a();
    }

    public void setTargetLook(Entity target, float yawOffset, float renderOffset) {
        controllerLook.a(target, yawOffset, renderOffset);
    }

    public void updateAI() {
        entitySenses.a();
        controllerMove.c();
        controllerLook.a();
        controllerJump.b();
    }
    public static void sendPacket(Player player, Packet packet) {
        Framework.plugin.log("npc", "send packet " + packet.getClass().getSimpleName() + " to " + player.getName());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendPacketNearby(Location location, Packet packet) {
        sendPacketNearby(location, packet, 64);
    }

    public static void sendPacketNearby(Location location, Packet packet, double radius) {
        radius *= radius;
        final org.bukkit.World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld()) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }
            sendPacket(ply, packet);
        }
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

    private PlayerControllerLook getControllerLook() {
        return controllerLook;
   }

    private PlayerControllerMove getControllerMove() {
        return controllerMove;
    }

    private PlayerEntitySenses getEntitySenses() {
        return entitySenses;
    }
}
