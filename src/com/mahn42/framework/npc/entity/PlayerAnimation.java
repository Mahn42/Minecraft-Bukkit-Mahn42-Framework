/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import net.minecraft.server.v1_4_5.EntityPlayer;
import net.minecraft.server.v1_4_5.Packet;
import net.minecraft.server.v1_4_5.Packet17EntityLocationAction;
import net.minecraft.server.v1_4_5.Packet18ArmAnimation;
import net.minecraft.server.v1_4_5.Packet40EntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public enum PlayerAnimation {
    ARM_SWING {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            Packet18ArmAnimation packet = new Packet18ArmAnimation(player, 1);
            sendPacketNearby(packet, player, radius);
        }
    },
    HURT {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            Packet18ArmAnimation packet = new Packet18ArmAnimation(player, 2);
            sendPacketNearby(packet, player, radius);
        }
    },
    SIT {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            player.mount(player);
        }
    },
    SLEEP {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            Packet17EntityLocationAction packet = new Packet17EntityLocationAction(player, 0,
                    (int) player.locX, (int) player.locY, (int) player.locZ);
            sendPacketNearby(packet, player, radius);
        }
    },
    SNEAK {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            player.getBukkitEntity().setSneaking(true);
            sendPacketNearby(new Packet40EntityMetadata(player.id, player.getDataWatcher(), true), player,
                    radius);
        }
    },
    STOP_SITTING {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            player.mount(null);
        }
    },
    STOP_SLEEPING {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            Packet18ArmAnimation packet = new Packet18ArmAnimation(player, 3);
            sendPacketNearby(packet, player, radius);
        }
    },
    /**
     *
     */
    STOP_SNEAKING {
        @Override
        protected void playAnimation(EntityPlayer player, int radius) {
            player.getBukkitEntity().setSneaking(false);
            sendPacketNearby(new Packet40EntityMetadata(player.id, player.getDataWatcher(), true), player,
                    radius);
        }
    };

    public void play(Player player) {
        play(player, 64);
    }

    public void play(Player player, int radius) {
        playAnimation(((CraftPlayer) player).getHandle(), radius);
    }

    protected void playAnimation(EntityPlayer player, int radius) {
        throw new UnsupportedOperationException("unimplemented animation");
    }

    protected void sendPacketNearby(Packet packet, EntityPlayer player, int radius) {
        sendPacketNearby(player.getBukkitEntity().getLocation(), packet, radius);
    }

    public static void sendPacketNearby(Location location, Packet packet, double radius) {
        radius *= radius;
        final World world = location.getWorld();
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

    public static void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(packet);
    }
}
