/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class InventoryHelper {
    
    public static boolean hasAtleastItems(Inventory aInv, Material aMat, int aCount) {
        boolean lFound = false;
        for(ItemStack lItem : aInv) {
            if (lItem != null && lItem.getType().equals(aMat)) {
                aCount -= lItem.getAmount();
                if (aCount <= 0) {
                    lFound = true;
                    break;
                }
            }
        }
        return lFound;
    }

    public static int removeItems(Inventory aInv, Material aMat, int aCount) {
        int aO = aCount;
        int i = -1;
        for(ItemStack lItem : aInv) {
            i++;
            if (lItem != null && lItem.getType().equals(aMat)) {
                if (lItem.getAmount() > aCount) {
                    lItem.setAmount(lItem.getAmount() - aCount);
                    aCount = 0;
                    break;
                } else {
                    aCount -= lItem.getAmount();
                    aInv.setItem(i, null);
                    if (aCount == 0) {
                        break;
                    }
                }
            }
        }
        return aO - aCount;
    }

    public static int insertItems(Inventory aInv, Material aMat, int aCount) {
        int aO = aCount;
        for (ItemStack lItem : aInv) {
            if (lItem != null && lItem.getType().equals(aMat)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= aCount) {
                        lItem.setAmount(lItem.getAmount() + aCount);
                        aCount = 0;
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        aCount -= lplace;
                    }
                    break;
                }
            }
        }
        if (aCount > 0) {
            int i = -1;
            for (ItemStack lItem : aInv) {
                i++;
                if (lItem == null) {
                    lItem = new ItemStack(aMat);
                    aInv.setItem(i, lItem);
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= aCount) {
                        lItem.setAmount(lItem.getAmount() + aCount);
                        aCount = 0;
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        aCount -= lplace;
                    }
                    break;
                }
            }
        }
        return aO - aCount;
    }
    
}
