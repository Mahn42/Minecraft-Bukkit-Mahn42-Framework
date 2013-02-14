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
        if (aInv != null) {
            for (ItemStack lItem : aInv) {
                if (lItem != null && lItem.getType().equals(aMat)) {
                    aCount -= lItem.getAmount();
                    if (aCount <= 0) {
                        lFound = true;
                        break;
                    }
                }
            }
        }
        return lFound;
    }

    public static int removeItems(ItemStack[] aStack, ItemStack aItem) {
        return removeItems(aStack, aItem, -1);
    }

    public static int removeItems(ItemStack[] aStack, ItemStack aItem, int aExcludeIndex) {
        ItemStack lRemItem = new ItemStack(aItem);
        int aO = lRemItem.getAmount();
        int i = -1;
        for (ItemStack lItem : aStack) {
            i++;
            if (i == aExcludeIndex) {
                continue;
            }
            if (lItem != null && isSimilarItem(lItem, lRemItem)) {
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
        for (ItemStack lItem : aInv) {
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
        return insertItems(aStack, aItem, -1);
    }

    public static int insertItems(ItemStack[] aStack, ItemStack aItem, int aExcludeIndex) {
        ItemStack lNewItem = new ItemStack(aItem);
        int aO = lNewItem.getAmount();
        int lIndex = -1;
        for (ItemStack lItem : aStack) {
            lIndex++;
            if (lIndex == aExcludeIndex) {
                continue;
            }
            if (lItem != null && isSimilarItem(lItem, lNewItem)) {
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
            lIndex = -1;
            for (ItemStack lItem : aStack) {
                lIndex++;
                if (lIndex == aExcludeIndex) {
                    continue;
                }
                if (lItem == null) {
                    lItem = new ItemStack(lNewItem);
                    aStack[lIndex] = lItem;
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
            if (lItem != null && isSimilarItem(lItem, lNewItem)) {
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
        for (ItemStack lItem : aInv) {
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
                    lRes.add(new ItemStack(lItem));
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
            if (lItem != null && isSimilarItem(lItem, lStack)) {
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
            if (lItem != null && isSimilarItem(lItem, lStack)) {
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

    public static boolean isSimilarItem(ItemStack aItem1, ItemStack aItem2) {
        if (aItem1 == null && aItem2 == null) {
            return true;
        } else if (aItem1 == null) {
            return false;
        } else if (aItem2 == null) {
            return false;
        } else {
            return aItem1.getType().equals(aItem2.getType()) && aItem1.getData().getData() == aItem2.getData().getData();
        }
    }
}
