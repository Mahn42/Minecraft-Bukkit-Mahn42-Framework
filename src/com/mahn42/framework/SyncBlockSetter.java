/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

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

        protected void execute() {
            BlockState lState = location.getBlock().getState();
            lState.setType(material);
            lState.setRawData(data);
            lState.update(true);
            //location.getBlock().setTypeIdAndData(material.getId(), data, physics);
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
