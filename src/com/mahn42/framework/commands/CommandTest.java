/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.BlockPositionDelta;
import com.mahn42.framework.EntityControl;
import com.mahn42.framework.EntityControlPathItemRelative;
import com.mahn42.framework.Framework;
import com.mahn42.framework.IMarker;
import com.mahn42.framework.InventoryHelper;
import com.mahn42.framework.WorldScanner;
import com.mahn42.framework.npc.entity.NPCEntityPlayer;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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
            Player player = (Player) aCommandSender;
            Location loc = player.getLocation();
            if (aStrings[0].equalsIgnoreCase("playerspawn")) {
                final NPCEntityPlayer bukkitEntity = Framework.plugin.createPlayerNPC(player.getWorld(), new BlockPosition(loc), aStrings[1], player);
                bukkitEntity.setItemInHand(new ItemStack(Material.IRON_PICKAXE));
                PlayerInventory inventory = bukkitEntity.getInventory();
                Random r = new Random();
                inventory.setLeggings(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_LEGGINGS), r.nextInt()));
                inventory.setChestplate(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_CHESTPLATE), r.nextInt()));
                inventory.setBoots(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_BOOTS), r.nextInt()));
                inventory.setHelmet(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_HELMET), r.nextInt()));
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
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(10, 0, 0)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(0, 0, 10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(0, 0, -10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(10, 0, 0)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(10, 0, 10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(-10, 0, -10)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(5, 0, 5)));
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(-5, 0, -5)));
                Framework.plugin.getEntityController().add(lC);
                player.sendMessage("entity " + bukkitEntity);
            } else if (aStrings[0].equalsIgnoreCase("fisher")) {
                final NPCEntityPlayer bukkitEntity = Framework.plugin.createPlayerNPC(player.getWorld(), new BlockPosition(loc), aStrings[1], player);
                bukkitEntity.setItemInHand(new ItemStack(Material.FISHING_ROD));
                PlayerInventory inventory = bukkitEntity.getInventory();
                inventory.setLeggings(Framework.plugin.setItemStackColor(new ItemStack(Material.LEATHER_LEGGINGS), 0x8080F0));
                EntityControl lC = new EntityControl(bukkitEntity);
                lC.path.add(new EntityControlPathItemRelative(new BlockPositionDelta(1, 0, 0)));
                Framework.plugin.getEntityController().add(lC);
            } else if (aStrings[0].equalsIgnoreCase("biome")) {
                Biome biome = player.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ());
                player.sendMessage("biome was " + biome);
                if (aStrings.length > 1) {
                    biome = Biome.valueOf(aStrings[1]);
                    int lradius = 0;
                    if (aStrings.length > 2) {
                        lradius = Integer.parseInt(aStrings[2]);
                    }
                    for (int x = -lradius; x <= lradius; x++) {
                        for (int z = -lradius; z <= lradius; z++) {
                            player.getWorld().setBiome(loc.getBlockX() + x, loc.getBlockZ() + z, biome);
                        }
                    }
                    player.sendMessage("biome is now " + biome);
                }
            } else if (aStrings[0].equalsIgnoreCase("snow")) {
                if (aStrings.length > 1) {
                    int lradius = 0;
                    if (aStrings.length > 1) {
                        lradius = Integer.parseInt(aStrings[1]);
                    }
                    for (int x = -lradius; x <= lradius; x++) {
                        for (int z = -lradius; z <= lradius; z++) {
                            Block highestBlockAt = player.getWorld().getHighestBlockAt(loc.getBlockX() + x, loc.getBlockZ() + z);
                            if (highestBlockAt.getType().equals(Material.AIR)) {
                                byte h = 1;
                                h = (byte) (Math.sin(x / 4) * Math.sin(z / 4) * 7);
                                highestBlockAt.setTypeIdAndData(Material.SNOW.getId(), (byte) h, true);
                            }
                        }
                    }
                    player.sendMessage("snowing " + lradius);
                }
            } else if (aStrings[0].equalsIgnoreCase("resnow")) {
                if (aStrings.length > 1) {
                    int lradius = 0;
                    if (aStrings.length > 1) {
                        lradius = Integer.parseInt(aStrings[1]);
                    }
                    for (int x = -lradius; x <= lradius; x++) {
                        for (int z = -lradius; z <= lradius; z++) {
                            Block highestBlockAt = player.getWorld().getHighestBlockAt(loc.getBlockX() + x, loc.getBlockZ() + z);
                            while (highestBlockAt.getType().equals(Material.AIR)) {
                                highestBlockAt = player.getWorld().getBlockAt(highestBlockAt.getLocation().add(0, -1, 0));
                            }
                            if (highestBlockAt.getType().equals(Material.SNOW)) {
                                highestBlockAt.setTypeId(Material.AIR.getId(), true);
                            }
                        }
                    }
                    player.sendMessage("resnowing " + lradius);
                }
            } else if (aStrings[0].equalsIgnoreCase("showpath")) {
                World lWorld = player.getWorld();
                List<IMarker> findMarkers = Framework.plugin.findMarkers(lWorld, aStrings[1]);
                BlockPosition lp1 = findMarkers.get(0).getPosition();
                boolean f0 = true;
                boolean f1 = true;
                boolean f2 = true;
                boolean f3 = true;
                if (aStrings.length > 2) {
                    f0 = aStrings[2].equalsIgnoreCase("x");
                    if (aStrings.length > 3) {
                        f1 = aStrings[3].equalsIgnoreCase("x");
                        if (aStrings.length > 4) {
                            f2 = aStrings[4].equalsIgnoreCase("x");
                            if (aStrings.length > 5) {
                                f3 = aStrings[5].equalsIgnoreCase("x");
                            }
                        }
                    }
                }
                EntityControl lec = new EntityControl(player);
                lec.showPath(lp1, f0, f1, f2, f3);
                player.sendMessage("path exists " + f0 + " " + f1 + " " + f2 + " " + f3 + " : " + EntityControl.existsPath(player, lp1, f0, f1, f2, f3));
            } else if (aStrings[0].equalsIgnoreCase("istree")) {
                Block targetBlock = player.getTargetBlock(null, 30);
                List<BlockPosition> treePoss = WorldScanner.getTreePoss(player.getWorld(), new BlockPosition(targetBlock.getLocation()));
                aCommandSender.sendMessage("tree poss = " + treePoss.size());
            } else if (aStrings[0].equalsIgnoreCase("checktree")) {
                Block targetBlock = player.getTargetBlock(null, 30);
                List<BlockPosition> treePoss = WorldScanner.getTreePoss(player.getWorld(), new BlockPosition(targetBlock.getLocation()));
                for (BlockPosition lPos : treePoss) {
                    lPos.getBlock(player.getWorld()).setType(Material.WOOL);
                }
                aCommandSender.sendMessage("tree poss = " + treePoss.size());
            } else if (aStrings[0].equalsIgnoreCase("dropwool")) {
                //Wool lWool = new Wool(DyeColor.BROWN);
                //ItemStack lStack = new ItemStack(Material.WOOL, 2, (short)0, (byte)10);
                //ItemStack lStack = new ItemStack(Material.WOOL, 2);
                //lStack.setData(lWool);
                //ItemStack lStack = new ItemStack(Material.WOOL, 2);
                //lStack.setData(new MaterialData(Material.WOOL, (byte)10));
                net.minecraft.server.v1_5_R1.ItemStack lItem = new net.minecraft.server.v1_5_R1.ItemStack(Material.WOOL.getId(), 1, 10);
                CraftItemStack lStack = CraftItemStack.asCraftMirror(lItem);
                Block targetBlock = player.getTargetBlock(null, 30);
                Item dropItemNaturally = player.getWorld().dropItem(targetBlock.getLocation(), lStack);
                //((EntityItem)((CraftItem)dropItemNaturally).getHandle()).
            } else if (aStrings[0].equalsIgnoreCase("filter")) {
                Framework.plugin.logFilter = aStrings[1];
            } else if (aStrings[0].equalsIgnoreCase("profile")) {
                Framework.plugin.getProfiler().dump(Framework.plugin.getLogger());
            } else {
                player.sendMessage("unkown " + aStrings[0]);
            }
        }
        if (aStrings.length > 0 ) {
            if (aStrings[0].equalsIgnoreCase("inv1")) {
                ItemStack[] lInv = new ItemStack[36];
                lInv[4] = new ItemStack(Material.SAPLING);
                int removeItems = InventoryHelper.removeItems(lInv, new ItemStack(Material.SAPLING, (byte) 3));
                aCommandSender.sendMessage("remitems = " + removeItems);
            } else if (aStrings[0].equalsIgnoreCase("system")) {
                Properties properties = System.getProperties();
                for(String lName : properties.stringPropertyNames()) {
                    aCommandSender.sendMessage(lName + " = " + properties.getProperty(lName));
                }
            } else if (aStrings[0].equalsIgnoreCase("cpu")) {
                OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                double systemLoadAverage = operatingSystemMXBean.getSystemLoadAverage();
                int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
                Runtime lRuntime = Runtime.getRuntime();
                lRuntime.gc();
                aCommandSender.sendMessage("cpu: " + systemLoadAverage + " count: " + availableProcessors + " (" + lRuntime.availableProcessors() + ")");
                long lFree = lRuntime.freeMemory() + (lRuntime.maxMemory() - lRuntime.totalMemory());
                aCommandSender.sendMessage("memory: total: " + lRuntime.totalMemory() + " max: " + lRuntime.maxMemory() + " free: " + lFree + " used: " + (lRuntime.maxMemory() - lFree));
            } else if (aStrings[0].equalsIgnoreCase("profile")) {
                Framework.plugin.getProfiler().dump(Framework.plugin.getLogger());
            } else {
                aCommandSender.sendMessage("what?");
            }
        }
        return true;
    }

    public List<BlockPosition> getTreePoss(World aWorld, BlockPosition aPos) {
        ArrayList<BlockPosition> lRes = new ArrayList<BlockPosition>();
        BlockPosition lPos = aPos.clone();
        Block lBlock = lPos.getBlock(aWorld);
        while (lBlock.getType().equals(Material.LOG)) {
            lRes.add(lPos.clone());
            lPos.y--;
            lBlock = lPos.getBlock(aWorld);
        }
        lPos = aPos.clone();
        lPos.y++;
        lBlock = lPos.getBlock(aWorld);
        while (lBlock.getType().equals(Material.LOG)) {
            lRes.add(lPos.clone());
            lPos.y++;
            lBlock = lPos.getBlock(aWorld);
        }
        int lCountLeaves = 0;
        ArrayList<BlockPosition> lRes2 = new ArrayList<BlockPosition>();
        for (int i = 1; i <= 2; i++) {
            lRes2.clear();
            for (BlockPosition lP : lRes) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPosition lPP = lP.clone();
                        lPP.add(x, 0, z);
                        lBlock = lPP.getBlock(aWorld);
                        if (lBlock.getType().equals(Material.LEAVES)
                                && (lBlock.getData() & 0x4) == 0) {
                            lCountLeaves++;
                        } else if (lBlock.getType().equals(Material.LOG)) {
                            lRes2.add(lPP);
                        }
                    }
                }
            }
            for (BlockPosition lP : lRes2) {
                if (!lRes.contains(lP)) {
                    lRes.add(lP);
                }
            }
        }
        if (lCountLeaves < 3) {
            lRes.clear();
        }
        return lRes;
    }
}
