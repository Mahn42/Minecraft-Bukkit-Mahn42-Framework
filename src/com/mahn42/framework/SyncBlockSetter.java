/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author andre
 */
public class SyncBlockSetter implements Runnable {

    protected Framework plugin;
    protected int fCount = 0;
    
    protected class SyncBlockSetterItem {
        public Location location;
        public Material material;
        public byte data;
        public boolean physics;

        protected void execute() {
            location.getBlock().setTypeIdAndData(material.getId(), data, physics);
        }
    }
    
    protected ArrayList<SyncBlockSetterItem> fItems = new ArrayList<SyncBlockSetterItem>();
    
    public SyncBlockSetter(Framework aPlugin) {
        plugin = aPlugin;
    }

    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        SyncBlockSetterItem lItem = new SyncBlockSetterItem();
        lItem.location = aLocation;
        lItem.material = aMaterial;
        lItem.data = aData;
        lItem.physics = aPhysics;
        fItems.add(lItem);
    }

    @Override
    public void run() {
        try {
            if (!fItems.isEmpty()) {
                ArrayList<SyncBlockSetterItem> lWorking = fItems;
                fItems = new ArrayList<SyncBlockSetterItem>();
                //Logger.getLogger("SyncBlockSetter").info("count = " + new Integer(lWorking.size()) + " stat = " + new Integer(fCount));
                while (!lWorking.isEmpty()) {
                    SyncBlockSetterItem lCurrent = lWorking.get(0);
                    lCurrent.execute();
                    lWorking.remove(0);
                    fCount++;
                }
            }
        } catch(Exception lEx) {
            Logger.getLogger("SyncBlockSetter").info(lEx.getMessage());
        }
    }
    
}
