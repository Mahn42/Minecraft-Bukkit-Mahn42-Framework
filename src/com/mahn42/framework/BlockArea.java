/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

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
        
        public void toBlock(Block aBlock) {
            BlockState lState = aBlock.getState();
            lState.setTypeId(id);
            lState.setRawData(data);
            lState.update(true);
        }
        
        public void toList(SyncBlockList aList, BlockPosition aPos) {
            aList.add(aPos, Material.getMaterial(id), data);
        }
        
        @Override
        public String toString() {
            return Material.getMaterial(id).toString() + "(" + data + ")";
        }

        private String toFileString() {
            return id + "," + data;
        }

        private void fromFileString(String aText) {
            String lParts[] = aText.split(",");
            id = Integer.parseInt(lParts[0]);
            data = Byte.parseByte(lParts[1]);
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
    
    public BlockArea(World aWorld, BlockPosition aEdge1, BlockPosition aEdge2) {
        BlockPosition lWHD = aEdge1.getWHD(aEdge2);
        width = lWHD.x;
        height = lWHD.y;
        depth = lWHD.z;
        initItems();
        BlockPosition lPos = aEdge1.getMinPos(aEdge2);
        fromWorld(aWorld, lPos);
    }
    
    public void initItems() {
        items = new ArrayList<BlockArea.BlockAreaItem>(width * height * depth);
        items.clear();
        int lSize = width * height * depth;
        //Framework.plugin.getLogger().info("initItems: size=" + lSize + " w=" + width + " h=" + height + " d=" + depth);
        for(int lIndex = 0; lIndex < lSize; lIndex++) {
            items.add(lIndex, new BlockAreaItem());
        }
    }
    
    public void clear(Material aMaterial, byte aData) {
        clear(aMaterial.getId(), aData);
    }
    
    public void clear(int aMaterialId, byte aData) {
        for(BlockAreaItem lItem : items) {
            lItem.id = aMaterialId;
            lItem.data = aData;
        }
    }
    
    public BlockAreaItem get(int aX, int aY, int aZ) {
        return items.get(aX + width * aY + width * height * aZ);
    }
    
    public void fromBlock(int aX, int aY, int aZ, Block aBlock) {
        BlockAreaItem lItem = get(aX, aY, aZ);
        lItem.fromBlock(aBlock);
    }

    public void fromWorld(World aWorld, BlockPosition aEdge1) {
        for(int lX = 0; lX < width; lX++) {
            for(int lY = 0; lY < height; lY++) {
                for(int lZ = 0; lZ < depth; lZ++) {
                    get(lX, lY, lZ).fromBlock(aWorld.getBlockAt(aEdge1.x + lX, aEdge1.y + lY, aEdge1.z + lZ));
                }
            }
        }
    }

    public void toWorld(World aWorld, BlockPosition aEdge1) {
        for(int lX = 0; lX < width; lX++) {
            for(int lY = 0; lY < height; lY++) {
                for(int lZ = 0; lZ <= depth; lZ++) {
                    get(lX,lY,lZ).toBlock(aWorld.getBlockAt(aEdge1.x + lX, aEdge1.y + lY, aEdge1.z + lZ));
                }
            }
        }
    }

    public void toList(SyncBlockList aList, BlockPosition aEdge1) {
        toList(aList, aEdge1, false, false, false, false);
    }
    
    public boolean isStep(int aId, int aStep) {
        boolean lResult; 
        switch (aStep) {
            case 0:
                lResult = aId != Material.PAINTING.getId();
                break;
            case 1:
                lResult = true;
                break;
            default:
                lResult = true;
                break;
        }
        return lResult;
    }
    
    public void toList(SyncBlockList aList, BlockPosition aEdge1, boolean aMirrorX, boolean aMirrorZ, boolean aMirrorY, boolean aSwapXZ) {
        int lfX = aMirrorX ? -1 : 1;
        int lfY = aMirrorY ? -1 : 1;
        int lfZ = aMirrorZ ? -1 : 1;
        for(int lStep = 0; lStep < 2; lStep++) {
            for(int lX = 0; lX < width; lX++) {
                for(int lY = 0; lY < height; lY++) {
                    for(int lZ = 0; lZ < depth; lZ++) {
                        BlockAreaItem lItem = get(lX,lY,lZ);
                        if (isStep(lItem.id, lStep)) {
                            BlockPosition aPos = new BlockPosition(aEdge1.x + lX * lfX, aEdge1.y + lY * lfY, aEdge1.z + lZ * lfZ);
                            if (aSwapXZ) {
                                int lSwap = aPos.x;
                                aPos.x = aPos.z;
                                aPos.z = lSwap;
                            }
                            lItem.toList(aList, aPos);
                        }
                    }
                }
            }
        }
    }
    
    public String toFileString() {
        String lResult;
        lResult = "1," + width + "," + height + "," + depth;
        for(BlockAreaItem lItem : items) {
            lResult += ";" + lItem.toFileString();
        }
        return lResult;
    }
    
    public void fromFileString(String aText) {
        String lParts[] = aText.split(";");
        String lHeader[] = lParts[0].split(",");
        width = Integer.parseInt(lHeader[1]);
        height = Integer.parseInt(lHeader[2]);
        depth = Integer.parseInt(lHeader[3]);
        initItems();
        for(int lIndex = 1; lIndex < lParts.length; lIndex++) {
            items.get(lIndex - 1).fromFileString(lParts[lIndex]);
        }
    }
}
