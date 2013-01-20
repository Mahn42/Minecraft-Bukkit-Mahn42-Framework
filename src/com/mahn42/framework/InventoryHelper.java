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

    public static int removeItems(ItemStack[] aStack, ItemStack aItem) {
        ItemStack lRemItem = new ItemStack(aItem);
        int aO = lRemItem.getAmount();
        int i = -1;
        for (ItemStack lItem : aStack) {
            i++;
            if (lItem != null && lItem.isSimilar(lRemItem)) {
                if (lItem.getAmount() > lRemItem.getAmount()) {
                    lItem.setAmount(lItem.getAmount() - lRemItem.getAmount());
                    lRemItem.setAmount(0);
                    break;
                } else {
                    lRemItem.setAmount(lRemItem.getAmount() - lItem.getAmount());
                    aStack[i] = null;
                    if (lRemItem.getAmount() == 0) {
                        break;
                    }
                }
            }
        }
        return aO - lRemItem.getAmount();
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
        ItemStack lNewItem = new ItemStack(aItem);
        int aO = lNewItem.getAmount();
        for (ItemStack lItem : aStack) {
            if (lItem != null && lItem.isSimilar(lNewItem)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= lNewItem.getAmount()) {
                        lItem.setAmount(lItem.getAmount() + lNewItem.getAmount());
                        lNewItem.setAmount(0);
                        break;
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        lNewItem.setAmount(lNewItem.getAmount() - lplace);
                    }
                }
            }
        }
        if (lNewItem.getAmount() > 0) {
            int i = -1;
            for (ItemStack lItem : aStack) {
                i++;
                if (lItem == null) {
                    lItem = new ItemStack(lNewItem);
                    aStack[i] = lItem;
                    lNewItem.setAmount(0);
                    break;
                }
            }
        }
        return aO - lNewItem.getAmount();
    }
    
    public static int canInsertItems(ItemStack[] aStack, ItemStack aItem) {
        ItemStack lNewItem = new ItemStack(aItem);
        int aO = lNewItem.getAmount();
        for (ItemStack lItem : aStack) {
            if (lItem != null && lItem.isSimilar(lNewItem)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= lNewItem.getAmount()) {
                        //lItem.setAmount(lItem.getAmount() + lNewItem.getAmount());
                        lNewItem.setAmount(0);
                        break;
                    } else {
                        //lItem.setAmount(lItem.getMaxStackSize());
                        lNewItem.setAmount(lNewItem.getAmount() - lplace);
                    }
                }
            }
        }
        if (lNewItem.getAmount() > 0) {
            int i = -1;
            for (ItemStack lItem : aStack) {
                i++;
                if (lItem == null) {
                    //lItem = new ItemStack(lNewItem);
                    //aStack[i] = lItem;
                    lNewItem.setAmount(0);
                    break;
                }
            }
        }
        return aO - lNewItem.getAmount();
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
                    lNew.setAmount(aCount);
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
    
    public static int canInsertItems(Inventory aInv, ItemStack aStack) {
        ItemStack lStack = new ItemStack(aStack);
        int aO = lStack.getAmount();
        for (ItemStack lItem : aInv) {
            if (lItem != null && lItem.isSimilar(lStack)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= lStack.getAmount()) {
                        //lItem.setAmount(lItem.getAmount() + lStack.getAmount());
                        lStack.setAmount(0);
                    } else {
                        //lItem.setAmount(lItem.getMaxStackSize());
                        lStack.setAmount(lStack.getAmount() - lplace);
                    }
                    break;
                }
            }
        }
        if (lStack.getAmount() > 0) {
            int i = -1;
            for (ItemStack lItem : aInv) {
                i++;
                if (lItem == null) {
                    //lItem = new ItemStack(lStack);
                    //aInv.setItem(i, lItem);
                    lStack.setAmount(0);
                    break;
                }
            }
        }
        return aO - lStack.getAmount();
    }
    
    public static int insertItems(Inventory aInv, ItemStack aStack) {
        ItemStack lStack = new ItemStack(aStack);
        int aO = lStack.getAmount();
        for (ItemStack lItem : aInv) {
            if (lItem != null && lItem.isSimilar(lStack)) {
                if (lItem.getAmount() < lItem.getMaxStackSize()) {
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= lStack.getAmount()) {
                        lItem.setAmount(lItem.getAmount() + lStack.getAmount());
                        lStack.setAmount(0);
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        lStack.setAmount(lStack.getAmount() - lplace);
                    }
                    break;
                }
            }
        }
        if (lStack.getAmount() > 0) {
            int i = -1;
            for (ItemStack lItem : aInv) {
                i++;
                if (lItem == null) {
                    lItem = new ItemStack(lStack);
                    aInv.setItem(i, lItem);
                    lStack.setAmount(0);
                    /*
                    int lplace = lItem.getMaxStackSize() - lItem.getAmount();
                    if (lplace >= aCount) {
                        lItem.setAmount(lItem.getAmount() + aCount);
                        aCount = 0;
                    } else {
                        lItem.setAmount(lItem.getMaxStackSize());
                        aCount -= lplace;
                    }
                    */
                    break;
                }
            }
        }
        return aO - lStack.getAmount();
    }
}
