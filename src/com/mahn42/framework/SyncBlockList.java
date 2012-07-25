/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.World;

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
        
        public SyncBlockItem(BlockPosition aPos, Material aMaterial, byte aData, boolean aPhysics, int aSkipCount) {
            pos = aPos.clone();
            material = aMaterial;
            data = aData;
            physics = aPhysics;
            skipCount = aSkipCount;
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
