/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class BlockArea {
    public class BlockAreaEntity {
        public EntityType type;
        public BlockPosition location;

        private void fromEntity(Entity lEntity, BlockPosition aEdge1) {
            location = new BlockPosition(lEntity.getLocation());
            location.add(-aEdge1.x, -aEdge1.y, -aEdge1.z);
            type = lEntity.getType();
        }

        private void toWorld(World aWorld, BlockPosition aEdge1) {
            Location lLoc = location.getLocation(aWorld);
            lLoc.add(aEdge1.x, aEdge1.y, aEdge1.z);
            try {
                aWorld.spawnEntity(lLoc, type);
            } catch (Exception lEx) {
                Framework.plugin.getLogger().log(Level.SEVERE, null, lEx);
            }
        }

        private String toFileString() {
            return "" + type.getTypeId() + "," + location.x + "," + location.y + "," + location.z;
        }

        private void fromFileString(String aText) {
            String[] lParts = aText.split(",");
            type = EntityType.fromId(Integer.parseInt(lParts[0]));
            location = new BlockPosition(Integer.parseInt(lParts[1]), Integer.parseInt(lParts[2]), Integer.parseInt(lParts[3]));
        }

        private void toList(SyncBlockList aList, BlockPosition aEdge1) {
            BlockPosition lLoc = location.clone();
            lLoc.add(aEdge1.x, aEdge1.y, aEdge1.z);
            aList.add(lLoc, type);
        }
    }

    public class BlockAreaItem {
        public int id = Material.AIR.getId();
        public byte data = 0;
        public String signLine0 = null;
        public String signLine1 = null;
        public String signLine2 = null;
        public String signLine3 = null;
        public ItemStack[] itemStacks;
        
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
            if ((id == Material.SIGN.getId()) || (id == Material.SIGN_POST.getId()) || (id == Material.WALL_SIGN.getId())) {
                Sign lSign = (Sign)aBlock.getState();
                signLine0 = lSign.getLine(0);
                signLine1 = lSign.getLine(1);
                signLine2 = lSign.getLine(2);
                signLine3 = lSign.getLine(3);
            } else if (id == Material.CHEST.getId()) {
                Chest lChest = (Chest)aBlock.getState();
                Inventory lInventory = lChest.getBlockInventory();
                itemStacks = lInventory.getContents().clone();
            } else if (id == Material.FURNACE.getId() || id == Material.BURNING_FURNACE.getId()) {
                Furnace lFurnace = (Furnace)aBlock.getState();
                itemStacks = new ItemStack[3];
                itemStacks[0] = lFurnace.getInventory().getFuel();
                itemStacks[1] = lFurnace.getInventory().getResult();
                itemStacks[2] = lFurnace.getInventory().getSmelting();
            } else if (id == Material.DISPENSER.getId()) {
                Dispenser lDispenser = (Dispenser)aBlock.getState();
                Inventory lInventory = lDispenser.getInventory();
                itemStacks = lInventory.getContents().clone();
            }
        }
        
        public void toBlock(Block aBlock) {
            BlockState lState = aBlock.getState();
            lState.setTypeId(id);
            lState.setRawData(data);
            lState.update(true);
        }
        
        public void toList(SyncBlockList aList, BlockPosition aPos) {
            aList.add(aPos, Material.getMaterial(id), data, false, 0, signLine0, signLine1, signLine2, signLine3, itemStacks, EntityType.UNKNOWN);
        }
        
        @Override
        public String toString() {
            return Material.getMaterial(id).toString() + "(" + data + ")";
        }

        protected String escapeSignLine(String aLine) {
            if (aLine != null) {
                return aLine.replaceAll(",", "").replaceAll(";", "");
            } else {
                return "";
            }
        }
        
        protected String unescapeSignLine(String aLine) {
            if (aLine != null && aLine.length() > 0) {
                return aLine;
            } else {
                return null;
            }
        }
        
        private String toFileString() {
            String lResult = id + "," + data;
            if (Framework.plugin.isSign(Material.getMaterial(id))) {
                if (signLine0 != null || signLine1 != null || signLine2 != null || signLine3 != null) {
                    lResult += "," + escapeSignLine(signLine0) + "," + escapeSignLine(signLine1) + "," + escapeSignLine(signLine2) + "," + escapeSignLine(signLine3);
                }
            } else if (id == Material.CHEST.getId() || id == Material.FURNACE.getId() || id == Material.DISPENSER.getId() || id == Material.BURNING_FURNACE.getId()) {
                if (itemStacks != null) {
                    for(ItemStack lStack : itemStacks) {
                        if (lStack != null) {
                            lResult += "," + lStack.getTypeId() + "," + lStack.getData().getData() + "," + lStack.getAmount();
                        } else {
                        }
                    }
                }
            }
            return lResult; 
        }

        private void fromFileString(String aText) {
            String lParts[] = aText.split(",");
            id = Integer.parseInt(lParts[0]);
            data = Byte.parseByte(lParts[1]);
            if (Framework.plugin.isSign(Material.getMaterial(id))) {
                if (lParts.length > 5) {
                    signLine0 = unescapeSignLine(lParts[2]);
                    signLine1 = unescapeSignLine(lParts[3]);
                    signLine2 = unescapeSignLine(lParts[4]);
                    signLine3 = unescapeSignLine(lParts[5]);
                } 
            } else if (id == Material.CHEST.getId() || id == Material.FURNACE.getId() || id == Material.DISPENSER.getId() || id == Material.BURNING_FURNACE.getId()) {
                ArrayList<ItemStack> lItemStacks = new ArrayList<ItemStack>();
                for(int lIndex = 2; lIndex < lParts.length; lIndex+=3) {
                    lItemStacks.add(new ItemStack(Integer.parseInt(lParts[lIndex]), Integer.parseInt(lParts[lIndex+2]), (short)0, Byte.parseByte(lParts[lIndex+1])));
                }
                itemStacks = new ItemStack[lItemStacks.size()];
                itemStacks = lItemStacks.toArray(itemStacks);
            }
        }
    }
    
    public int width;
    public int height;
    public int depth;
    public ArrayList<BlockAreaItem> items;
    public ArrayList<BlockAreaEntity> entities = new ArrayList<BlockArea.BlockAreaEntity>();;
    
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
        entities.clear();
        List<Entity> lEntities = aWorld.getEntities();
        BlockPosition lEdge2 = aEdge1.clone();
        lEdge2.add(width - 1, height - 1, depth - 1);
        for(Entity lEntity : lEntities) {
            if (lEntity.getType() != EntityType.PLAYER) {
                BlockPosition lPos = new BlockPosition(lEntity.getLocation());
                if (lPos.x >= aEdge1.x && lPos.x <= lEdge2.x
                    && lPos.y >= aEdge1.y && lPos.y <= lEdge2.y
                    && lPos.z >= aEdge1.z && lPos.z <= lEdge2.z) {
                    BlockAreaEntity lItem = new BlockAreaEntity();
                    lItem.fromEntity(lEntity, aEdge1);
                    entities.add(lItem);
                    Framework.plugin.getLogger().info("Entity: " + lEntity);
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
        for(BlockAreaEntity lItem : entities) {
            lItem.toWorld(aWorld, aEdge1);
        }
    }

    public void toList(SyncBlockList aList, BlockPosition aEdge1) {
        toList(aList, aEdge1, false, false, false, false, false);
    }
    
    public void toListMixed(SyncBlockList aList, BlockPosition aEdge1) {
        toList(aList, aEdge1, false, false, false, false, true);
    }
    
    public static ArrayList<Material> dependsOnOtherBlock;
    {
        dependsOnOtherBlock = new ArrayList<Material>();
        dependsOnOtherBlock.add(Material.PAINTING);
        dependsOnOtherBlock.add(Material.TORCH);
        dependsOnOtherBlock.add(Material.LEVER);
        dependsOnOtherBlock.add(Material.STONE_BUTTON);
    }
    
    public boolean isStep(int aId, int aStep) {
        boolean lResult; 
        switch (aStep) {
            case 0:
                lResult = !dependsOnOtherBlock.contains(Material.getMaterial(aId));
                break;
            case 1:
                lResult = dependsOnOtherBlock.contains(Material.getMaterial(aId));
                break;
            default:
                lResult = true;
                break;
        }
        return lResult;
    }
    
    public void toList(SyncBlockList aList, BlockPosition aEdge1, boolean aMirrorX, boolean aMirrorZ, boolean aMirrorY, boolean aSwapXZ, boolean aNoAir) {
        int lfX = aMirrorX ? -1 : 1;
        int lfY = aMirrorY ? -1 : 1;
        int lfZ = aMirrorZ ? -1 : 1;
        for(int lStep = 0; lStep < 2; lStep++) {
            for(int lX = 0; lX < width; lX++) {
                for(int lY = 0; lY < height; lY++) {
                    for(int lZ = 0; lZ < depth; lZ++) {
                        BlockAreaItem lItem = get(lX,lY,lZ);
                        if (lItem.id != 0 || !aNoAir) {
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
        for(BlockAreaEntity lItem : entities) {
//            lItem.toWorld(aList.world, aEdge1);
            lItem.toList(aList, aEdge1);
        }
    }
    
    public String toFileString() {
        String lResult;
        lResult = "1," + width + "," + height + "," + depth;
        for(BlockAreaItem lItem : items) {
            lResult += ";" + lItem.toFileString();
        }
        lResult += ";" + entities.size();
        for(BlockAreaEntity lItem : entities) {
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
        int lPos = 1;
        for(int lIndex = lPos; lIndex <= items.size(); lIndex++) {
            items.get(lIndex - 1).fromFileString(lParts[lIndex]);
            lPos++;
        }
        entities.clear();
        if (lPos < (lParts.length - 1)) {
            int lEntSize = Integer.parseInt(lParts[lPos++]);
            for (int lIndex = 0; lIndex < lEntSize; lIndex++) {
                BlockAreaEntity lItem = new BlockAreaEntity();
                lItem.fromFileString(lParts[lPos]);
                entities.add(lItem);
                lPos++;
            }
        }
    }
}
