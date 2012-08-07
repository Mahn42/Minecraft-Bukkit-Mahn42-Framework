/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class SyncBlockSetter implements Runnable {

    protected int fCount = 0;
    
    protected class SyncBlockSetterItem {
        public Location location;
        public Material material;
        public byte data;
        public boolean physics;
        public int skipCount = 0;
        public String signLine0 = null;
        public String signLine1 = null;
        public String signLine2 = null;
        public String signLine3 = null;
        public ItemStack[] itemStacks = null;
        public EntityType entityType = EntityType.UNKNOWN;

        protected void execute() {
            if (entityType == EntityType.UNKNOWN) {
                if (physics) {
                    BlockState lState = location.getBlock().getState();
                    lState.setType(material);
                    lState.setRawData(data);
                    lState.update(true);
                } else {
                    location.getBlock().setTypeIdAndData(material.getId(), data, physics);
                }
                if (material.equals(Material.SIGN) || material.equals(Material.SIGN_POST) || material.equals(Material.WALL_SIGN)) {
                    Sign lSign = (Sign)location.getBlock().getState();
                    if (signLine0 != null) {
                        lSign.setLine(0, signLine0);
                        Framework.plugin.getLogger().info("signLine0 = " + signLine0);
                    }
                    if (signLine1 != null) {
                        lSign.setLine(1, signLine1);
                    }
                    if (signLine2 != null) {
                        lSign.setLine(2, signLine2);
                    }
                    if (signLine3 != null) {
                        lSign.setLine(3, signLine3);
                    }
                    lSign.update(true);
                } else if (material.equals(Material.CHEST)) {
                    if (itemStacks != null) {
                        Chest lChest = (Chest)location.getBlock().getState();
                        lChest.getInventory().setContents(itemStacks);
                        lChest.update(true);
                    }
                } else if (material.equals(Material.DISPENSER)) {
                    if (itemStacks != null) {
                        Dispenser lDispenser = (Dispenser)location.getBlock().getState();
                        lDispenser.getInventory().setContents(itemStacks);
                        lDispenser.update(true);
                    }
                } else if (material.equals(Material.FURNACE) || material.equals(Material.BURNING_FURNACE)) {
                    Framework.plugin.getLogger().info("Furnace " + (itemStacks != null ? itemStacks.length: 0));
                    if (itemStacks != null && itemStacks.length >= 3) {
                        Furnace lFurnace = (Furnace)location.getBlock().getState();
                        lFurnace.getInventory().setFuel(itemStacks[0]);
                        Framework.plugin.getLogger().info("Fuel: " + itemStacks[0]);
                        lFurnace.getInventory().setResult(itemStacks[1]);
                        Framework.plugin.getLogger().info("Result: " + itemStacks[1]);
                        lFurnace.getInventory().setSmelting(itemStacks[2]);
                        Framework.plugin.getLogger().info("Smelting: " + itemStacks[2]);
                        lFurnace.update(true);
                    }
                }
            } else {
                try {
                    //Framework.plugin.getLogger().info("try to spawn " + entityType + " at " + location);
                    location.getWorld().spawnEntity(location, entityType);
                } catch (Exception lEx) {
                    Framework.plugin.getLogger().log(Level.SEVERE, null, lEx);
                }
            }
        }
    }
    
    protected ArrayList<SyncBlockSetterItem> fItems = new ArrayList<SyncBlockSetterItem>();
    protected final Object fsync = new Object();
    
    public SyncBlockSetter() {
    }

    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        SyncBlockSetterItem lItem = new SyncBlockSetterItem();
        lItem.location = aLocation;
        lItem.material = aMaterial;
        lItem.data = aData;
        lItem.physics = aPhysics;
        lItem.skipCount = 0;
        synchronized(fsync) {
            fItems.add(lItem);
        }
    }
    
    public void addList(SyncBlockList aList) {
        synchronized(fsync) {
            for(SyncBlockList.SyncBlockItem lBlock : aList) {
                SyncBlockSetterItem lItem = new SyncBlockSetterItem();
                lItem.location = lBlock.pos.getLocation(aList.world);
                lItem.material = lBlock.material;
                lItem.data = lBlock.data;
                lItem.physics = lBlock.physics;
                lItem.skipCount = lBlock.skipCount;
                lItem.signLine0 = lBlock.signLine0;
                lItem.signLine1 = lBlock.signLine1;
                lItem.signLine2 = lBlock.signLine2;
                lItem.signLine3 = lBlock.signLine3;
                lItem.itemStacks = lBlock.itemStacks;
                lItem.entityType = lBlock.entityType;
                fItems.add(lItem);
            }
        }
    }

    @Override
    public void run() {
        try {
            if (!fItems.isEmpty()) {
                ArrayList<SyncBlockSetterItem> lWorking;
                synchronized(fsync) {
                    lWorking = fItems;
                    fItems = new ArrayList<SyncBlockSetterItem>();
                }
                //Logger.getLogger("SyncBlockSetter").info("count = " + new Integer(lWorking.size()) + " stat = " + new Integer(fCount));
                ArrayList<SyncBlockSetterItem> lNext = new ArrayList<SyncBlockSetterItem>();
                while (!lWorking.isEmpty()) {
                    SyncBlockSetterItem lCurrent = lWorking.get(0);
                    if (lCurrent.skipCount <= 0) {
                        lCurrent.execute();
                        fCount++;
                    } else {
                        lCurrent.skipCount--;
                        lNext.add(lCurrent);
                    }
                    lWorking.remove(0);
                }
                if (lNext.size() > 0) {
                    synchronized(fsync) {
                        fItems.addAll(lNext);
                    }
                }
            }
        } catch(Exception lEx) {
            Logger.getLogger("SyncBlockSetter").info(lEx.getMessage());
        }
    }
    
}
