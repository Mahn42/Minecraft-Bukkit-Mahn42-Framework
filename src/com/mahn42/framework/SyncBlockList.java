/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Art;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class SyncBlockList implements Iterable<SyncBlockList.SyncBlockItem> {
    public World world;

    public class SyncBlockItem {
        public BlockPosition pos;
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
        public Art art;
        
        public SyncBlockItem(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount) {
            pos = aPos.clone();
            material = aMaterial;
            data = aData;
            physics = aPhysics;
            skipCount = aSkipCount;
        }

        public SyncBlockItem(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount, String aSignLine0, String aSignLine1, String aSignLine2, String aSignLine3, ItemStack[] aItemStacks, EntityType aEntityType, Art aArt) {
            pos = aPos.clone();
            material = aMaterial;
            data = aData;
            physics = aPhysics;
            skipCount = aSkipCount;
            signLine0 = aSignLine0;
            signLine1 = aSignLine1;
            signLine2 = aSignLine2;
            signLine3 = aSignLine3;
            itemStacks = aItemStacks;
            entityType = aEntityType;
            art = aArt;
        }

        public boolean isDependantBlock() {
            return Framework.dependsOnOtherBlock.contains(material);
        }
    }
    
    protected ArrayList<SyncBlockItem> fList = new ArrayList<SyncBlockItem>();
    
    public SyncBlockList(World aWorld) {
        world = aWorld;
    }
    
    public void add(BlockPosition aPos, Material aMaterial, byte aData) {
        fList.add(new SyncBlockItem(aPos, aMaterial, aData, true, 0));
    }
    
    public void add(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics) {
        fList.add(new SyncBlockItem(aPos, aMaterial, aData, aPhysics, 0));
    }
    
    public void add(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount) {
        fList.add(new SyncBlockItem(aPos, aMaterial, aData, aPhysics, aSkipCount));
    }
    
    public void add(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount, String aSignLine0, String aSignLine1, String aSignLine2, String aSignLine3, ItemStack[] aItemStacks, EntityType aEntityType, Art aArt) {
        fList.add(new SyncBlockItem(aPos, aMaterial, aData, aPhysics, aSkipCount, aSignLine0, aSignLine1, aSignLine2, aSignLine3, aItemStacks, aEntityType, aArt));
    }
    
    public void add(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount, String aSignLine0, String aSignLine1, String aSignLine2, String aSignLine3, ItemStack[] aItemStacks, EntityType aEntityType) {
        fList.add(new SyncBlockItem(aPos, aMaterial, aData, aPhysics, aSkipCount, aSignLine0, aSignLine1, aSignLine2, aSignLine3, aItemStacks, aEntityType, null));
    }
    
    public void add(BlockPosition aPos, EntityType aEntityType) {
        fList.add(new SyncBlockItem(aPos, null, (byte)0, false, 0, null, null, null, null, null, aEntityType, null));
    }
    
    public void moveDependantBlocksAtEnd() {
        ArrayList<SyncBlockItem> lDeps = new ArrayList<SyncBlockItem>();
        for(SyncBlockItem lItem : fList) {
            if (lItem.isDependantBlock()) {
                lDeps.add(lItem);
            }
        }
        for(SyncBlockItem lItem : lDeps) {
            fList.remove(lItem);
        }
        for(SyncBlockItem lItem : lDeps) {
            fList.add(lItem);
        }
    }

    public void execute() {
        Framework.plugin.fSyncBlockSetter.addList(this);
    }
    
    public int size() {
        return fList.size();
    }

    @Override
    public Iterator<SyncBlockItem> iterator() {
        return fList.iterator();
    }
}
