/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BlockPositionDelta;
import com.mahn42.framework.EntityControl;
import com.mahn42.framework.EntityControlPathItemRelative;
import com.mahn42.framework.Framework;
import com.mahn42.framework.npc.entity.EntityHumanNPC;
import java.util.Random;
import net.minecraft.server.v1_4_5.ItemInWorldManager;
import net.minecraft.server.v1_4_5.WorldServer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author andre
 */
public class CommandTest implements CommandExecutor {

    //fw_test playerspawn <name>
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0 && aCommandSender instanceof Player) {
            Player player = (Player)aCommandSender;
            Location loc = player.getLocation();
            if (aStrings[0].equalsIgnoreCase("playerspawn")) {
                WorldServer ws = ((CraftWorld) loc.getWorld()).getHandle();
                EntityHumanNPC handle = new EntityHumanNPC(ws.getServer().getServer(), ws, aStrings[1], new ItemInWorldManager(ws));
                final CraftPlayer bukkitEntity = handle.getBukkitEntity();
                ws.addEntity(handle, CreatureSpawnEvent.SpawnReason.CUSTOM);
                bukkitEntity.teleport(loc);
                bukkitEntity.setSleepingIgnored(true);
                bukkitEntity.setItemInHand(new ItemStack(Material.IRON_PICKAXE));
                bukkitEntity.setGameMode(GameMode.SURVIVAL);
                PlayerInventory inventory = bukkitEntity.getInventory();
                Random r = new Random();
                inventory.setLeggings(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_LEGGINGS),r.nextInt()));
                inventory.setChestplate(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_CHESTPLATE),r.nextInt()));
                inventory.setBoots(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_BOOTS),r.nextInt()));
                inventory.setHelmet(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_HELMET),r.nextInt()));
                /*
                Framework.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Framework.plugin, new Runnable() {
                    @Override
                    public void run() {
                        boolean removeFromPlayerList = false; //Setting.REMOVE_PLAYERS_FROM_PLAYER_LIST.asBoolean();
                        EntityLiving handle = ((CraftLivingEntity) bukkitEntity).getHandle();
                        if (handle.world == null)
                            return;
                        if (removeFromPlayerList) {
                            handle.world.players.remove(handle);
                            Logger.getAnonymousLogger().info("removed");
                        } else if (!handle.world.players.contains(handle)) {
                            handle.world.players.add(handle);
                            Logger.getAnonymousLogger().info("added");
                        }

                        //NMS.addOrRemoveFromPlayerList(getBukkitEntity(),
                        //        data().get("removefromplayerlist", removeFromPlayerList));
                    }
                }, 1);
                */
                EntityControl lC = new EntityControl(bukkitEntity);
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta( 10,  0,   0)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(  0,  0,  10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(  0,  0, -10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta( 10,  0,   0)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta( 10,  0,  10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(-10,  0, -10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(  5,  0,   5)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta( -5,  0,  -5)));
                Framework.plugin.getEntityController().add(lC);
                player.sendMessage("entity " + bukkitEntity);
            } else if (aStrings[0].equalsIgnoreCase("fisher")) {
                WorldServer ws = ((CraftWorld) loc.getWorld()).getHandle();
                EntityHumanNPC handle = new EntityHumanNPC(ws.getServer().getServer(), ws, aStrings[1], new ItemInWorldManager(ws));
                final CraftPlayer bukkitEntity = handle.getBukkitEntity();
                ws.addEntity(handle, CreatureSpawnEvent.SpawnReason.CUSTOM);
                bukkitEntity.teleport(loc);
                bukkitEntity.setSleepingIgnored(true);
                bukkitEntity.setItemInHand(new ItemStack(Material.FISHING_ROD));
                bukkitEntity.setGameMode(GameMode.SURVIVAL);
                PlayerInventory inventory = bukkitEntity.getInventory();
                inventory.setLeggings(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_LEGGINGS),0x8080F0));
                EntityControl lC = new EntityControl(bukkitEntity);
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta( 1,  0,   0)));
                Framework.plugin.getEntityController().add(lC);
            } else {
                player.sendMessage("unkown " + aStrings[0]);
            }
        }
        return true;
    }
    
}