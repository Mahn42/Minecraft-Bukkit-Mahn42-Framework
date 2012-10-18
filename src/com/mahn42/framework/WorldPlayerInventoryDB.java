/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;

/**
 *
 * @author andre
 */
public class WorldPlayerInventoryDB extends DBSet<WorldPlayerInventory> {
    public WorldPlayerInventoryDB(File aFile) {
        super(WorldPlayerInventory.class, aFile);
    }
    
    public WorldPlayerInventoryDB() {
        super(WorldPlayerInventory.class, new File(Framework.plugin.getDataFolder().getPath() + File.separatorChar + "PlayerInventory.csv"));
    }

    public WorldPlayerInventory get(String aInvenoryName, String aPlayerName) {
        for(WorldPlayerInventory lInv : this) {
            if(lInv.inventoryName.equals(aInvenoryName) && lInv.playerName.equals(aPlayerName)) {
                return lInv;
            }
        }
        return null;
    }
    
    public WorldPlayerInventory getOrCreate(String aInvenoryName, String aPlayerName) {
        WorldPlayerInventory lInv = get(aInvenoryName, aPlayerName);
        if (lInv == null) {
            lInv = new WorldPlayerInventory();
            lInv.inventoryName = aInvenoryName;
            lInv.playerName = aPlayerName;
            addRecord(lInv);
        }
        return lInv;
    }
}
