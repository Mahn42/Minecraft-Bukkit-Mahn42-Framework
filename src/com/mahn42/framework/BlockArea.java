/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class BlockArea {
    public class BlockAreaItem {
        public int id = Material.AIR.getId();
        public byte data = 0;
        
        public BlockAreaItem() {
        }

        public BlockAreaItem(int aId, byte aData) {
            id = aId;
            data = aData;
        }

        public BlockAreaItem(Material aMat, byte aData) {
            id = aMat.getId();
            data = aData;
        }

        public BlockAreaItem(Material aMat) {
            id = aMat.getId();
        }
        
        public void fromBlock(Block aBlock) {
            id = aBlock.getTypeId();
            data = aBlock.getData();
        }
        
        @Override
        public String toString() {
            return Material.getMaterial(id).toString() + "(" + data + ")";
        }
    }
    
    public int width;
    public int height;
    public int depth;
    public ArrayList<BlockAreaItem> items;
    
    public BlockArea() {
        items = new ArrayList<BlockArea.BlockAreaItem>();
    }

    public BlockArea(int aWidth, int aHeight, int aDepth) {
        width = aWidth;
        height = aHeight;
        depth = aDepth;
        initItems();
    }
    
    public void initItems() {
        items = new ArrayList<BlockArea.BlockAreaItem>(width * height * depth);
        items.clear();
        int lSize = width * height * depth;
        for(int lIndex = 0; lIndex < lSize; lIndex++) {
            items.add(lIndex, new BlockAreaItem());
        }
    }
    
    public BlockAreaItem get(int aX, int aY, int aZ) {
        return items.get(aX + width * aY + width * depth * aZ);
    }
    
    public void fromBlock(int aX, int aY, int aZ, Block aBlock) {
        BlockAreaItem lItem = get(aX, aY, aZ);
        lItem.fromBlock(aBlock);
    }

    public void fromBlock(World aWorld, BlockPosition aEdge1, BlockPosition aEdge2) {
        //TODO
    }
}
