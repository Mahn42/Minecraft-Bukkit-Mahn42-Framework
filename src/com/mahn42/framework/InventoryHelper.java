/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
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

    public static int insertItems(ItemStack[] aStack, ItemStack aItem) {
        int aO = aItem.getAmount();
        for (ItemStack lItem : aStack) {
            if (lItem != null && lItem.isSimilar(aItem)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= aItem.getAmount()) {
                        lItem.setAmount(lItem.getAmount() + aItem.getAmount());
                        aItem.setAmount(0);
                        break;
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        aItem.setAmount(aItem.getAmount() - lplace);
                    }
                }
            }
        }
        if (aItem.getAmount() > 0) {
            int i = -1;
            for (ItemStack lItem : aStack) {
                i++;
                if (lItem == null) {
                    lItem = new ItemStack(aItem);
                    aStack[i] = lItem;
                    aItem.setAmount(0);
                    break;
                }
            }
        }
        return aO - aItem.getAmount();
    }
    
    public static List<ItemStack> removeItemsByMaterial(Inventory aInv, Material aMat, int aCount) {
        ArrayList<ItemStack> lRes = new ArrayList<ItemStack>();
        int i = -1;
        for(ItemStack lItem : aInv) {
            i++;
            if (lItem != null && lItem.getType().equals(aMat)) {
                if (lItem.getAmount() > aCount) {
                    ItemStack lNew = new ItemStack(lItem);
                    lItem.setAmount(lItem.getAmount() - aCount);
                    lItem.setAmount(aCount);
                    aCount = 0;
                    lRes.add(lNew);
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
        return lRes;
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
