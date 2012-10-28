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
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class BlockArea {
    public enum BlockAreaPlaceMode {
        full,
        mixed,
        reverse
    }
    
    public class BlockAreaEntity {
        public EntityType type;
        public BlockPosition location;
        public ItemStack[] itemStacks;

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
            if (lParts.length > 4) {
                itemStacks = new ItemStack[lParts.length - 4];
                for(int lIndex = 4; lIndex < lParts.length; lIndex++) {
                    int lId = Integer.parseInt(lParts[lIndex]);
                    if (lId >= 0) {
                        itemStacks[lIndex-4] = new ItemStack(lId, Integer.parseInt(lParts[lIndex+2]), (short)0, Byte.parseByte(lParts[lIndex+1]));
                    } else {
                        itemStacks[lIndex-4] = null;
                    }
                }
            }
        }

        private void toList(SyncBlockList aList, BlockPosition aEdge1) {
            BlockPosition lLoc = location.clone();
            lLoc.add(aEdge1.x, aEdge1.y, aEdge1.z);
            aList.add(lLoc, null, (byte)0, true, 0, null, null, null, null, itemStacks, type);
        }

        private void toStringBuilder(StringBuilder lBuilder) {
            lBuilder.append(Integer.toString(type.getTypeId()));
            lBuilder.append(",");
            lBuilder.append(Integer.toString(location.x));
            lBuilder.append(",");
            lBuilder.append(Integer.toString(location.y));
            lBuilder.append(",");
            lBuilder.append(Integer.toString(location.z));
            if (itemStacks != null) {
                for(ItemStack lStack : itemStacks) {
                    if (lStack != null) {
                        lBuilder.append(",");
                        lBuilder.append(Integer.toString(lStack.getTypeId()));
                        lBuilder.append(",");
                        lBuilder.append(Byte.toString(lStack.getData().getData()));
                        lBuilder.append(",");
                        lBuilder.append(Integer.toString(lStack.getAmount()));
                    } else {
                        lBuilder.append(",-1,0,0");
                    }
                }
            }
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
        
        public void cloneFrom(BlockAreaItem aSrc) {
            id = aSrc.id;
            data = aSrc.data;
            if (aSrc.itemStacks != null) {
                itemStacks = aSrc.itemStacks.clone();
            } else {
                itemStacks = null;
            }
            signLine0 = aSrc.signLine0;
            signLine1 = aSrc.signLine1;
            signLine2 = aSrc.signLine2;
            signLine3 = aSrc.signLine3;
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
                if (lChest instanceof DoubleChest) {
                    DoubleChest lDChest = (DoubleChest)lChest;
                    Inventory lInventory = lDChest.getInventory();
                    itemStacks = lInventory.getContents().clone();
                } else {
                    Inventory lInventory = lChest.getBlockInventory();
                    itemStacks = lInventory.getContents().clone();
                }
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
        
        private void toStringBuilder(StringBuilder lBuilder) {
            lBuilder.append(Integer.toString(id));
            lBuilder.append(",");
            lBuilder.append(Byte.toString(data));
            //String lResult = id + "," + data;
            if (Framework.plugin.isSign(Material.getMaterial(id))) {
                if (signLine0 != null || signLine1 != null || signLine2 != null || signLine3 != null) {
                    lBuilder.append(",");
                    lBuilder.append(escapeSignLine(signLine0));
                    lBuilder.append(",");
                    lBuilder.append(escapeSignLine(signLine1));
                    lBuilder.append(",");
                    lBuilder.append(escapeSignLine(signLine2));
                    lBuilder.append(",");
                    lBuilder.append(escapeSignLine(signLine3));
                    //lResult += "," + escapeSignLine(signLine0) + "," + escapeSignLine(signLine1) + "," + escapeSignLine(signLine2) + "," + escapeSignLine(signLine3);
                }
            } else if (id == Material.CHEST.getId() || id == Material.FURNACE.getId() || id == Material.DISPENSER.getId() || id == Material.BURNING_FURNACE.getId()) {
                if (itemStacks != null) {
                    for(ItemStack lStack : itemStacks) {
                        if (lStack != null) {
                            lBuilder.append(",");
                            lBuilder.append(Integer.toString(lStack.getTypeId()));
                            lBuilder.append(",");
                            lBuilder.append(Byte.toString(lStack.getData().getData()));
                            lBuilder.append(",");
                            lBuilder.append(Integer.toString(lStack.getAmount()));
                            //lResult += "," + lStack.getTypeId() + "," + lStack.getData().getData() + "," + lStack.getAmount();
                        } else {
                            lBuilder.append(",-1,0,0");
                            //lResult += ",-1,0,0";
                        }
                    }
                }
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
                            lResult += ",-1,0,0";
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
                    int lId = Integer.parseInt(lParts[lIndex]);
                    if (lId >= 0) {
                        lItemStacks.add(new ItemStack(lId, Integer.parseInt(lParts[lIndex+2]), (short)0, Byte.parseByte(lParts[lIndex+1])));
                    } else {
                        lItemStacks.add(null);
                    }
                }
                itemStacks = new ItemStack[lItemStacks.size()];
                itemStacks = lItemStacks.toArray(itemStacks);
            }
        }
    }
    
    public class BlockAreaBuilding {
        public BlockPosition pos = new BlockPosition();
        
        public BlockAreaBuilding() {
        }

        public BlockAreaBuilding(BlockPosition aPos) {
            pos = aPos.clone();
        }

        public void toStringBuilder(StringBuilder aBuilder) {
            pos.toCSV(aBuilder, ",");
        }

        public void fromFileString(String aString) {
            pos.fromCSV(aString, "\\,");
        }

        public void toList(SyncBlockList aList, BlockPosition aEdge1) {
            BuildingDetectTask lTask = new BuildingDetectTask();
            lTask.player = null;
            lTask.world = aList.world;
            lTask.event = null;
            lTask.position = pos.clone();
            lTask.position.add(aEdge1);
            //Framework.plugin.getLogger().info("will detect building at " + lTask.position);
            Framework.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(Framework.plugin, lTask, 10);
        }
    }
    
    public int width;
    public int height;
    public int depth;
    public ArrayList<BlockAreaItem> items;
    public ArrayList<BlockAreaEntity> entities = new ArrayList<BlockArea.BlockAreaEntity>();
    public ArrayList<BlockAreaBuilding> buildings = new ArrayList<BlockArea.BlockAreaBuilding>();
    
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
        int lIndex = aX + width * aY + width * height * aZ;
        if (lIndex<0 || lIndex>items.size()) {
            Framework.plugin.getLogger().info(getClass().getSimpleName() + ": (" + aX + "," + aY + "," + aZ + ") is out of (" + width + "," + height + "," + depth + ")!");
            return null;
        } else {
            return items.get(aX + width * aY + width * height * aZ);
        }
    }
    
    public BlockAreaEntity newEntity() {
        BlockAreaEntity lResult = new BlockAreaEntity();
        entities.add(lResult);
        return lResult;
    }
    
    public void fromBlock(int aX, int aY, int aZ, Block aBlock) {
        BlockAreaItem lItem = get(aX, aY, aZ);
        lItem.fromBlock(aBlock);
    }

    public void fromWorld(World aWorld, BlockPosition aEdge1) {
        fromWorld(aWorld, aEdge1, true, true);
    }
    
    public void fromWorld(World aWorld, BlockPosition aEdge1, boolean aWithEntities, boolean aWithBuildings) {
        for(int lX = 0; lX < width; lX++) {
            for(int lY = 0; lY < height; lY++) {
                for(int lZ = 0; lZ < depth; lZ++) {
                    get(lX, lY, lZ).fromBlock(aWorld.getBlockAt(aEdge1.x + lX, aEdge1.y + lY, aEdge1.z + lZ));
                }
            }
        }
        BlockPosition lEdge2 = aEdge1.clone();
        lEdge2.add(width - 1, height - 1, depth - 1);
        entities.clear();
        if (aWithEntities) {
            List<Entity> lEntities = aWorld.getEntities();
            for(Entity lEntity : lEntities) {
                if (lEntity.getType() != EntityType.PLAYER) {
                    BlockPosition lPos = new BlockPosition(lEntity.getLocation());
                    if (lPos.x >= aEdge1.x && lPos.x <= lEdge2.x
                        && lPos.y >= aEdge1.y && lPos.y <= lEdge2.y
                        && lPos.z >= aEdge1.z && lPos.z <= lEdge2.z) {
                        BlockAreaEntity lItem = new BlockAreaEntity();
                        lItem.fromEntity(lEntity, aEdge1);
                        entities.add(lItem);
                        //Framework.plugin.getLogger().info("Entity: " + lEntity);
                    }
                }
            }
        }
        buildings.clear();
        if (aWithBuildings) {
            ArrayList<Building> lBuildings = Framework.plugin.getBuildingDetector().getBuildingsWithDetectBlock(aEdge1, lEdge2);
            for(Building lBuilding : lBuildings) {
                BlockPosition lPos = lBuilding.getDetectBlock().position.clone();
                lPos.subtract(aEdge1);
                buildings.add(new BlockAreaBuilding(lPos));
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

    public void toList(SyncBlockList aList, BlockPosition aEdge1, BlockAreaPlaceMode aMode) {
        toList(aList, aEdge1, false, false, false, false, aMode);
    }
    
    public void toListMixed(SyncBlockList aList, BlockPosition aEdge1) {
        toList(aList, aEdge1, false, false, false, false, BlockAreaPlaceMode.mixed);
    }
    
    public static ArrayList<Material> dependsOnOtherBlock;
    {
        dependsOnOtherBlock = new ArrayList<Material>();
        dependsOnOtherBlock.add(Material.PAINTING);
        dependsOnOtherBlock.add(Material.REDSTONE_WIRE);
        dependsOnOtherBlock.add(Material.REDSTONE_TORCH_ON);
        dependsOnOtherBlock.add(Material.REDSTONE_TORCH_OFF);
        dependsOnOtherBlock.add(Material.TORCH);
        dependsOnOtherBlock.add(Material.LEVER);
        dependsOnOtherBlock.add(Material.CACTUS);
        dependsOnOtherBlock.add(Material.DEAD_BUSH);
        dependsOnOtherBlock.add(Material.DETECTOR_RAIL);
        dependsOnOtherBlock.add(Material.LONG_GRASS);
        dependsOnOtherBlock.add(Material.TRIPWIRE);
        dependsOnOtherBlock.add(Material.TRIPWIRE_HOOK);
        dependsOnOtherBlock.add(Material.RAILS);
        dependsOnOtherBlock.add(Material.POWERED_RAIL);
        dependsOnOtherBlock.add(Material.SAPLING);
        dependsOnOtherBlock.add(Material.VINE);
        dependsOnOtherBlock.add(Material.WHEAT);
        dependsOnOtherBlock.add(Material.SEEDS);
        dependsOnOtherBlock.add(Material.SUGAR_CANE_BLOCK);
        dependsOnOtherBlock.add(Material.WEB);
        dependsOnOtherBlock.add(Material.WATER_LILY);
        dependsOnOtherBlock.add(Material.LADDER);
        dependsOnOtherBlock.add(Material.STONE_BUTTON);
        dependsOnOtherBlock.add(Material.YELLOW_FLOWER);
        dependsOnOtherBlock.add(Material.RED_ROSE);
        dependsOnOtherBlock.add(Material.BROWN_MUSHROOM);
        dependsOnOtherBlock.add(Material.RED_MUSHROOM);
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
    
    public void toList(SyncBlockList aList, BlockPosition aEdge1, boolean aMirrorX, boolean aMirrorZ, boolean aMirrorY, boolean aSwapXZ, BlockAreaPlaceMode aMode) {
        int lfX = aMirrorX ? -1 : 1;
        int lfY = aMirrorY ? -1 : 1;
        int lfZ = aMirrorZ ? -1 : 1;
        int lCount = 0;
        // first we will remove all dependentBlocks, so there should no drop
        for(int lX = 0; lX < width; lX++) {
            for(int lY = 0; lY < height; lY++) {
                for(int lZ = 0; lZ < depth; lZ++) {
                    BlockAreaItem lItem = get(lX,lY,lZ);
                    if (aMode == BlockAreaPlaceMode.full
                            || (aMode == BlockAreaPlaceMode.mixed && lItem.id != 0)
                            || (aMode == BlockAreaPlaceMode.reverse && lItem.id == 0)) {
                        BlockPosition lPos = new BlockPosition(aEdge1.x + lX * lfX, aEdge1.y + lY * lfY, aEdge1.z + lZ * lfZ);
                        if (aSwapXZ) {
                            int lSwap = lPos.x;
                            lPos.x = lPos.z;
                            lPos.z = lSwap;
                        }
                        if (dependsOnOtherBlock.contains(lPos.getBlockType(aList.world))) { // isStep(lPos.getBlockTypeId(aList.world), 1)) {
                            aList.add(lPos, Material.AIR, (byte)0);
                            lCount++;
                        }
                    }
                }
            }
        }
        Framework.plugin.log("ba", "Count depentent blocks " + lCount);
        // now we place blocks in two phases, first the stable blocks, second the dependent blocks
        for(int lStep = 0; lStep < 2; lStep++) {
            for(int lX = 0; lX < width; lX++) {
                for(int lY = 0; lY < height; lY++) {
                    for(int lZ = 0; lZ < depth; lZ++) {
                        BlockAreaItem lItem = get(lX,lY,lZ);
                        if (aMode == BlockAreaPlaceMode.full
                                || (aMode == BlockAreaPlaceMode.mixed && lItem.id != 0)
                                || (aMode == BlockAreaPlaceMode.reverse && lItem.id == 0)) {
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
            lItem.toList(aList, aEdge1);
        }
        for(BlockAreaBuilding lItem : buildings) {
            lItem.toList(aList, aEdge1);
        }
    }
    
    public String toFileString() {
        StringBuilder lBuilder = new StringBuilder();
        lBuilder.append("1,");
        lBuilder.append(Integer.toString(width));
        lBuilder.append(",");
        lBuilder.append(Integer.toString(height));
        lBuilder.append(",");
        lBuilder.append(Integer.toString(depth));
        for(BlockAreaItem lItem : items) {
            lBuilder.append(";");
            lItem.toStringBuilder(lBuilder);
        }
        lBuilder.append(";");
        lBuilder.append(Integer.toString(entities.size()));
        for(BlockAreaEntity lItem : entities) {
            lBuilder.append(";");
            lItem.toStringBuilder(lBuilder);
        }
        lBuilder.append(";");
        lBuilder.append(Integer.toString(buildings.size()));
        for(BlockAreaBuilding lItem : buildings) {
            lBuilder.append(";");
            lItem.toStringBuilder(lBuilder);
        }
        return lBuilder.toString();
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
            if (lPos < (lParts.length - 1)) {
                int lBuildSize = Integer.parseInt(lParts[lPos++]);
                for (int lIndex = 0; lIndex < lBuildSize; lIndex++) {
                    BlockAreaBuilding lItem = new BlockAreaBuilding();
                    lItem.fromFileString(lParts[lPos]);
                    buildings.add(lItem);
                    lPos++;
                }
            }
        }
    }
    
    public BuildingDescription createDescription(String aName) {
        BuildingDescription lDesc = new BuildingDescription(aName);
        BuildingDescription.BlockDescription lBlock = null;
        int lx;
        int ly;
        int lz;
        lx = ly = lz = 0;
        for(int x=0;x<width;x++) {
            for(int y=0;y<width;y++) {
                for(int z=0;z<width;z++) {
                    BlockAreaItem lItem = get(x, y, z);
                    if (lItem.id != Material.AIR.getId()) {
                        if (lBlock == null) {
                            lx = x; ly = y; lz = z;
                            lBlock = lDesc.newBlockDescription("B" + x + "-" + y + "-" + z);
                            lBlock.materials.add(Material.getMaterial(lItem.id));
                        } else {
                            BuildingDescription.BlockDescription lNext = null;
                            lNext = lDesc.newBlockDescription("B" + x + "-" + y + "-" + z);
                            lNext.materials.add(Material.getMaterial(lItem.id));
                            lBlock.newRelatedTo(new Vector(x - lx, y - ly, z - lz), lNext.name);
                            lx = x; ly = y; lz = z;
                            lBlock = lNext;
                        }
                    }
                }
            }
        }
        return lDesc;
    }
}
