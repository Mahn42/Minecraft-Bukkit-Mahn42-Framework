/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class WorldPlayerInventory extends DBRecord {
    public String inventoryName;
    public String playerName;
    public String inventory;
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(inventoryName);
        aCols.add(playerName);
        aCols.add(inventory);
    }
    
    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        inventoryName = aCols.pop();
        playerName = aCols.pop();
        inventory = aCols.pop();
    }
    
    public void setFromInventory(Inventory aInv) {
            YamlConfiguration lYaml = new YamlConfiguration();
            ItemStack[] lContents = aInv.getContents();
            ArrayList<Map> lItems = new ArrayList<Map>();
            for(ItemStack lItem : lContents) {
                if (lItem != null) {
                    Map<String, Object> lMap = lItem.serialize();
                    lItems.add(lMap);
                } else {
                    lItems.add(null);
                }
            }
            lYaml.set("Inventory", lItems);
            String lInvStr = lYaml.saveToString();
            inventory = Base64.encodeBytes(lInvStr.getBytes());
    }
    
    public void setToInventory(Inventory aInv) {
        aInv.clear();
        if (inventory != null && !inventory.isEmpty()) {
            YamlConfiguration lYaml = new YamlConfiguration();
            try {
                String lInvStr = "";
                try {
                    lInvStr = new String(Base64.decode(inventory));
                } catch (IOException ex) {
                    Logger.getLogger(WorldPlayerSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
                lYaml.loadFromString(lInvStr);
                Object lObj = lYaml.get("Inventory");
                if (lObj instanceof ArrayList) {
                    int lIndex = 0;
                    for(Object lItem : (ArrayList)lObj) {
                        if (lItem == null || lItem.toString().equals("null")) {
                            //aInv.addItem((ItemStack)null);
                        } else {
                            ItemStack lIStack = ItemStack.deserialize((Map)lItem);
                            aInv.setItem(lIndex, lIStack);
                        }
                        lIndex++;
                    } 
                }
            } catch (InvalidConfigurationException ex) {
                Logger.getLogger(WorldPlayerSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<ItemStack> getAsItemStacks() {
        ArrayList<ItemStack> lResult = new ArrayList<ItemStack>();
        if (inventory != null && !inventory.isEmpty()) {
            YamlConfiguration lYaml = new YamlConfiguration();
            try {
                String lInvStr = "";
                try {
                    lInvStr = new String(Base64.decode(inventory));
                } catch (IOException ex) {
                    Logger.getLogger(WorldPlayerSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
                lYaml.loadFromString(lInvStr);
                Object lObj = lYaml.get("Inventory");
                if (lObj instanceof ArrayList) {
                    int lIndex = 0;
                    for(Object lItem : (ArrayList)lObj) {
                        if (lItem == null || lItem.toString().equals("null")) {
                            //aInv.addItem((ItemStack)null);
                            lResult.add(null);
                        } else {
                            ItemStack lIStack = ItemStack.deserialize((Map)lItem);
                            lResult.add(lIStack);
                        }
                        lIndex++;
                    } 
                }
            } catch (InvalidConfigurationException ex) {
                Logger.getLogger(WorldPlayerSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lResult;
    }
}
