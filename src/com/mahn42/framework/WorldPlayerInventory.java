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
}
