/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class BuildingDescription {

    public enum RelatedPosition {
        Vector,
        Nearby,
        AreaXZ,
        AreaYX,
        AreaYZ
    }
    
    public class RelatedTo {
        public Vector direction;
        public RelatedPosition position = RelatedPosition.Vector;
        public String block;
        public BlockDescription description;
        public int minDistance = 0;
        public BlockMaterialArray materials = new BlockMaterialArray();
        
        public RelatedTo() {
        }

        public RelatedTo(Vector aDirection, String aBlock) {
            direction = aDirection;
            block = aBlock;
            position = RelatedPosition.Vector;
        }

        public RelatedTo(Vector aDirection, String aBlock, RelatedPosition aRelPos) {
            direction = aDirection;
            block = aBlock;
            position = aRelPos;
        }

        public void cloneFrom(RelatedTo aRel) {
            direction = aRel.direction.clone();
            position = aRel.position;
            block = aRel.block;
            description = aRel.description;
            minDistance = aRel.minDistance;
            materials.clear();
            materials.addAll(aRel.materials);
        }

        public void multiply(Vector aVector) {
            direction.multiply(aVector);
        }

        public void swapXYZ(SwapType aType) {
            int lSwap;
            switch (aType) {
                case XZ:
                    lSwap = direction.getBlockX();
                    direction.setX(direction.getBlockZ());
                    direction.setZ(lSwap);
                    break;
                case XY:
                    lSwap = direction.getBlockX();
                    direction.setX(direction.getBlockY());
                    direction.setY(lSwap);
                    break;
                case YZ:
                    lSwap = direction.getBlockY();
                    direction.setY(direction.getBlockZ());
                    direction.setZ(lSwap);
                    break;
            }
        }

        private void dump(Logger aLogger) {
            aLogger.info("direction = " + direction);
            aLogger.info("position = " + position);
            aLogger.info("block = " + block);
            aLogger.info("description = " + ((description == null) ? "no" : description.name));
            aLogger.info("minDistance = " + minDistance);
            materials.dump(aLogger);
        }
    }
    
    public class BlockMaterial {
        public Material material = null;
        public boolean withData = false;
        public byte data = 0;
        public EntityType entityType = EntityType.UNKNOWN;

        public BlockMaterial() {
        }
        
        public BlockMaterial(Material aMaterial) {
            if (Framework.materialToEntity.containsKey(aMaterial)) {
                entityType = Framework.materialToEntity.get(aMaterial);
            } else {
                material = aMaterial;
            }
        }
        
        public BlockMaterial(Material aMaterial, byte aData) {
            material = aMaterial;
            withData = true;
            data = aData;
        }
        
        public BlockMaterial(Entity aEntity) {
            entityType = aEntity.getType();
            switch(entityType) {
                case DROPPED_ITEM:
                    Item lItem = (Item)aEntity;
                    material = lItem.getItemStack().getType();
                    break;
                case ITEM_FRAME:
                    ItemFrame lFrame = (ItemFrame)aEntity;
                    material = lFrame.getItem().getType();
            }
        }
        
        public BlockMaterial(EntityType aEntityType) {
            entityType = aEntityType;
        }
        
        public BlockMaterial(EntityType aEntityType, Material aMaterial) {
            entityType = aEntityType;
            material = aMaterial;
        }
        
        @Override
        public boolean equals(Object aObject) {
            if (aObject instanceof BlockMaterial) {
                BlockMaterial lMat = (BlockMaterial)aObject;
                if (entityType != EntityType.UNKNOWN) {
                    if (material != null) {
                        return entityType.equals(lMat.entityType) && material.equals(lMat.material);
                    } else {
                        return entityType.equals(lMat.entityType);
                    }
                } else {
                    if (withData) {
                        return material.equals(lMat.material) && (data == lMat.data);
                    } else {
                        return material.equals(lMat.material);
                    }
                }
            } else if (aObject instanceof Material) {
                return material.equals(aObject) && !withData;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + (this.material != null ? this.material.hashCode() : 0);
            hash = 61 * hash + this.data;
            return hash;
        }
        
        @Override
        public String toString() {
            String lResult = material == null ? "null" : material.toString();
            if (withData) {
                lResult += "(" + data + ")";
            }
            if (entityType != EntityType.UNKNOWN) {
                lResult = entityType.toString() + " with " + lResult;
            }
            return lResult;
        }

        private void dump(Logger aLogger) {
            aLogger.info("material = " + material + (withData ? " " + data : " no data"));
        }
    }
    
    public class BlockMaterialArray extends ArrayList<BlockMaterial> {
        public boolean add(Material aMaterial) {
            return add(new BlockMaterial(aMaterial));
        }
        public boolean add(Material aMaterial, byte aData) {
            return add(new BlockMaterial(aMaterial, aData));
        }
        public boolean add(EntityType aEntityType) {
            return add(new BlockMaterial(aEntityType));
        }
        public boolean add(EntityType aEntityType, Material aMaterial) {
            return add(new BlockMaterial(aEntityType, aMaterial));
        }
        public void add(BlockMaterialArray aList) {
            for(BlockMaterial lMat : aList) {
                add(lMat);
            }
        }
        //public boolean contains(Material aMaterial) {
        //    return contains(new BlockMaterial(aMaterial));
        //}
        //public boolean contains(Material aMaterial, byte aData) {
        //    return contains(new BlockMaterial(aMaterial, aData));
        //}
        public boolean contains(Block aBlock) {
            BlockMaterial lMat = new BlockMaterial(aBlock.getType(), aBlock.getData());
            for(BlockMaterial lItem : this) {
                if (lItem.equals(lMat)) {
                    return true;
                }
            }
            return false;
            //return contains(new BlockMaterial(aBlock.getType(), aBlock.getData()));
        }

        public boolean contains(BlockMaterial aBMat) {
            for(BlockMaterial lItem : this) {
                if (lItem.equals(aBMat)) {
                    return true;
                }
            }
            return false;
        }

        private void dump(Logger aLogger) {
            if (size() == 0) {
                aLogger.info("no materials");
            } else if (size() == 1) {
                get(0).dump(aLogger);
            } else {
                aLogger.info("materials { // count = " + size());
                for(BlockMaterial lMat : this) {
                    lMat.dump(aLogger);
                }
                aLogger.info("}");
            }
        }
    }
    
    public class BlockDescription {
        public String name;
        public BlockMaterialArray materials = new BlockMaterialArray();
        public boolean detectSensible = false;
        public boolean redstoneSensible = false;
        public boolean nameSensible = false;
        public boolean signSensible = false;
        public boolean shareable = false;
        public ArrayList<RelatedTo> relatedTo = new ArrayList<RelatedTo>();
        
        public RelatedTo newRelatedTo() {
            RelatedTo lRel = new RelatedTo();
            relatedTo.add(lRel);
            return lRel;
        }

        public RelatedTo newRelatedTo(Vector aDirection, String aBlock) {
            RelatedTo lRel = newRelatedTo();
            lRel.direction = aDirection;
            lRel.block = aBlock;
            return lRel;
        }

        public RelatedTo newRelatedTo(Vector aDirection, String aBlock, RelatedPosition aRelPos) {
            RelatedTo lRel = newRelatedTo();
            lRel.direction = aDirection;
            lRel.block = aBlock;
            lRel.position = aRelPos;
            return lRel;
        }
        
        public RelatedTo newRelatedTo(String aBlock, RelatedPosition aRelPos, int aLength) {
            RelatedTo lRel = newRelatedTo();
            lRel.direction = new Vector(0,aLength,0);
            lRel.block = aBlock;
            lRel.position = aRelPos;
            return lRel;
        }
        
        public RelatedTo getRelation(String aDestBlock) {
            for(RelatedTo lRel : relatedTo) {
                if (lRel.block.equals(aDestBlock)) {
                    return lRel;
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return name;
        }

        public void cloneFrom(BlockDescription aDesc) {
            name = aDesc.name;
            detectSensible = aDesc.detectSensible;
            redstoneSensible = aDesc.redstoneSensible;
            nameSensible = aDesc.nameSensible;
            signSensible = aDesc.signSensible;
            materials.clear();
            materials.addAll(aDesc.materials);
            relatedTo.clear();
            for(RelatedTo lRel : aDesc.relatedTo) {
                RelatedTo lNew = new RelatedTo();
                lNew.cloneFrom(lRel);
                relatedTo.add(lNew);
            }
        }

        public void multiply(Vector aVector) {
            for(RelatedTo lRel : relatedTo) {
                lRel.multiply(aVector);
            }
        }

        public void swapXYZ(SwapType aType) {
            for(RelatedTo lRel : relatedTo) {
                lRel.swapXYZ(aType);
            }
        }

        private void dump(Logger aLogger) {
            aLogger.info("name = " + name);
            aLogger.info("detectSensible = " + detectSensible);
            aLogger.info("redstoneSensible = " + redstoneSensible);
            aLogger.info("nameSensible = " + nameSensible);
            aLogger.info("signSensible = " + signSensible);
            materials.dump(aLogger);
            aLogger.info("relations { // count = " + relatedTo.size());
            for(RelatedTo lRel : relatedTo) {
                lRel.dump(aLogger);
            }
            aLogger.info("}");
        }
    }
    
    public enum Position {
        onGround,
        underGround,
        everywhere
    }
    
    public String typeName;
    public String name;
    public Position position = Position.everywhere;
    public ArrayList<BlockDescription> blocks = new ArrayList<BlockDescription>();
    public boolean active;
    public double influenceRadiusFactor = 0;
    public BuildingHandler handler = null;
    public int detectPriority = -1;
    public String iconName = null;
    public boolean visibleOnMap = true;
    public int circleRadius = 0;
    public int color = 0x80A000;
    public double fillStyleOpacity = 0.1;
    
    public BuildingDescription() {
    }
    
    public BuildingDescription(String aName) {
        name = aName;
    }
    
    public String getName() {
        return typeName != null ? typeName : name;
    }

    public void dump(Logger aLogger) {
        aLogger.info("name = " + name);
        aLogger.info("typeName = " + typeName);
        aLogger.info("position = " + position);
        aLogger.info("active = " + active);
        aLogger.info("influenceRadiusFactor = " + influenceRadiusFactor);
        aLogger.info("handler = " + handler == null ? "no" : handler.getClass().getName());
        aLogger.info("detectPriority = " + detectPriority);
        aLogger.info("blocks { // count = " + blocks.size());
        for(BlockDescription lB : blocks) {
            lB.dump(aLogger);
        }
        aLogger.info("}");
    }

    public void cloneFrom(BuildingDescription aDesc) {
        typeName = aDesc.typeName;
        if (name == null || name.isEmpty()) {
            name = aDesc.name;
        }
        iconName = aDesc.iconName;
        visibleOnMap = aDesc.visibleOnMap;
        position = aDesc.position;
        blocks.clear();
        for(BlockDescription lBDesc : aDesc.blocks) {
            BlockDescription lNew = new BlockDescription();
            lNew.cloneFrom(lBDesc);
            blocks.add(lNew);
        }
        influenceRadiusFactor = aDesc.influenceRadiusFactor;
        detectPriority = aDesc.detectPriority;
        handler = aDesc.handler;
    }
    
    public void multiply(Vector aVector) {
        for(BlockDescription lBDesc : blocks) {
            lBDesc.multiply(aVector);
        }
    }
    
    public enum SwapType {
        XZ,
        XY,
        YZ
    }
    
    public void swapXYZ(SwapType aType) {
        for(BlockDescription lBDesc : blocks) {
            lBDesc.swapXYZ(aType);
        }
    }
    
    protected BuildingDescription create(String aName) {
        return Framework.plugin.getBuildingDetector().newDescription(aName);
    }
    
    public void createAndActivateXZ() {
        createAndActivateXZ(false);
    }
    
    public void createAndActivateXZ(boolean aWithMirror) {
        BuildingDescription lDesc, lDesc2, lDesc3;
        String lName = new String(name);
        this.name = this.name + ".X1";
        activate();

        lDesc = create(lName + ".X3");
        lDesc.cloneFrom(this);
        lDesc.multiply(new Vector(-1, 1, -1));
        lDesc.activate();

        if (aWithMirror) {
            lDesc2 = create(lName + ".X2");
            lDesc2.cloneFrom(this);
            lDesc2.multiply(new Vector(-1, 1, 1));
            lDesc2.activate();
            
            lDesc2 = create(lName + ".X4");
            lDesc2.cloneFrom(lDesc);
            lDesc2.multiply(new Vector(-1, 1, 1));
            lDesc2.activate();
        }

        lDesc = create(lName + ".Z1");
        lDesc.cloneFrom(this);
        lDesc.swapXYZ(BuildingDescription.SwapType.XZ);
        lDesc.multiply(new Vector( 1, 1, -1));
        lDesc.activate();

        lDesc2 = create(lName + ".Z3");
        lDesc2.cloneFrom(lDesc);
        lDesc2.multiply(new Vector(-1, 1, -1));
        lDesc2.activate();

        if (aWithMirror) {
            lDesc3 = create(lName + ".Z2");
            lDesc3.cloneFrom(lDesc);
            lDesc3.multiply(new Vector( 1, 1,-1));
            lDesc3.activate();

            lDesc3 = create(lName + ".Z4");
            lDesc3.cloneFrom(lDesc2);
            lDesc3.multiply(new Vector( 1, 1,-1));
            lDesc3.activate();
        }
    }
    
    public BlockDescription getBlock(String lDescName) {
        for(BlockDescription lBlock : blocks) {
            if (lDescName.equalsIgnoreCase(lBlock.name)) {
                return lBlock;
            }
        }
        return null;
    }

    public BlockDescription newBlockDescription(String aName) {
        BlockDescription lBDesc = new BlockDescription();
        lBDesc.name = aName;
        blocks.add(lBDesc);
        return lBDesc;
    }
    
    public BlockMaterialArray newBlockMaterialArray() {
        return new BlockMaterialArray();
    }
    
    public void activate() {
        HashMap<String, BlockDescription> lHash = new HashMap<String, BlockDescription>();
        for(BlockDescription lBDesc : blocks) {
            lHash.put(lBDesc.name, lBDesc);
            if (lBDesc.materials.isEmpty()) {
                Framework.plugin.getLogger().info("no materials on block " + lBDesc.name + " of " + name + "!");
            }
        }
        for(BlockDescription lBDesc : blocks) {
            for(RelatedTo lRel : lBDesc.relatedTo) {
                lRel.description = lHash.get(lRel.block);
                if (lRel.description == null) {
                    Framework.plugin.getLogger().info("block description " + lRel.block + " of building description '" + name + "' not found!");
                }
            }
        }
        if (detectPriority < 0) {
            detectPriority = blocks.size();
        }
        active = true;
    }

    public Building matchDescription(World aWorld, Map<BlockPosition, Entity> aEntities, int lX, int lY, int lZ) {
        //Block lBlock = aWorld.getBlockAt(lX, lY, lZ);
        BlockMaterial lBMat = getBlockMaterial(aWorld, aEntities, new BlockPosition(lX, lY, lZ));
        ArrayList<BlockDescription> lExcludes = new ArrayList<BlockDescription>();
        Framework.plugin.log("bd", this.name);
        for(BlockDescription lBlockDesc : blocks) {
            if (lBlockDesc.detectSensible) {
//                if (lBlockDesc.materials.contains(lBlock)) {
                if (lBlockDesc.materials.contains(lBMat)) {
                    lExcludes.clear();
                    Building aBuilding = new Building();
                    aBuilding.description = this;
                    aBuilding.setWorld(aWorld);
                    //Logger.getLogger("detect").info("mat " + lMat.name());
                    if (!canFollowRelateds(lExcludes, aBuilding, aWorld, aEntities, lBlockDesc, lX, lY, lZ, lBMat)) {
                        Framework.plugin.log("bd", "not ok, excludes " + lExcludes.size());
                        //return null;
                    } else {
                        if (lExcludes.size() >= blocks.size()) {
                            aBuilding.update();
                            Framework.plugin.log("bd", "found: " + this.name);
                            return aBuilding;
                        } else {
                            Framework.plugin.log("bd", "not enough matches (excluder " + lExcludes.size() + " )");
                        }
                    }
                }
            }
        }
        Framework.plugin.log("bd", this.name + " does not match!");
        return null;
    }
    
    protected class RelFollower {
        BlockPosition pos;
        BlockDescription desc;
        BlockMaterial bmat;
    }
    
    private BlockMaterial getBlockMaterial(World aWorld, Map<BlockPosition, Entity> aEntities, BlockPosition aPos) {
        BlockMaterial lMat = null;
        Block lBlock = aPos.getBlock(aWorld);
        if (lBlock.getType().equals(Material.AIR) && aEntities != null) {
            Entity lEntity = aEntities.get(aPos);
            if (lEntity != null) {
                lMat = new BlockMaterial(lEntity);
            } else {
                lMat = new BlockMaterial(lBlock.getType(), lBlock.getData());
            }
        } else {
            lMat = new BlockMaterial(lBlock.getType(), lBlock.getData());
        }
        return lMat;
    }
    
    private boolean canFollowRelateds(ArrayList<BlockDescription> aExcludes, Building aBuilding, World aWorld, Map<BlockPosition, Entity> aEntities, BlockDescription lBlockDesc, int lX, int lY, int lZ, BlockMaterial aMat) {
        if (!aExcludes.contains(lBlockDesc)) {
            BlockPosition lStartPos = new BlockPosition(lX, lY, lZ);
            aBuilding.blocks.add(new BuildingBlock(lBlockDesc, new BlockPosition(lStartPos), aMat == null ? null : aMat.material));
            if (lBlockDesc.relatedTo.size() > 0) {
                Framework.plugin.log("bd", "check desc " + lBlockDesc.name + " at " + lStartPos);
                ArrayList<RelFollower> lFs = new ArrayList<RelFollower>();
                for(RelatedTo lRel : lBlockDesc.relatedTo) {
                    boolean lRelated = false;
                    boolean lFirst = true;
                    BlockMaterial lRelatedMaterial = null;
                    BlockPosition lRelatedPos = null;
                    BlockPosition lPos1 = lStartPos.clone();
                    BlockPosition lPos2 = lStartPos.clone();
                    int lCount = 0;
                    lPos2.add(lRel.direction);
                    switch(lRel.position) {
                        case Vector:
                            int lSkip = lRel.minDistance;
                            ArrayList<BlockPosition> lPoss = new WorldLineWalk(
                                    lStartPos,
                                    new BlockPosition(lX + lRel.direction.getBlockX(), lY + lRel.direction.getBlockY(), lZ + lRel.direction.getBlockZ())).getPositions();
                            lPoss.remove(0); // remove the first, its the startpoint
                            int lLastRel = -1;
                            for(int lIndex = lPoss.size() - 1; lIndex >= 0; lIndex--) {
                                //Logger.getLogger("detect").info("rel " + lRel.description.name + " index " + lIndex);
                                BlockPosition lPos = lPoss.get(lIndex);
                                //Block lBlock = lPos.getBlock(aWorld);
                                BlockMaterial lBMat = getBlockMaterial(aWorld, aEntities, lPos);
                                if (!lRelated && lIndex < lRel.minDistance) {
                                    Framework.plugin.log("bd", "break rel " + lRel.description.name + " mindist " + lIndex);
                                    break;
                                }
                                if (lRelated && !lRel.materials.isEmpty() && !lRel.materials.contains(lBMat)) {
                                    Framework.plugin.log("bd", "break rel " + lRel.description.name + " mat " + lBMat);
                                    lRelatedPos = null;
                                    lRelated = false;
                                    lIndex = lLastRel-1;
                                }
                                if (!lRelated && lRel.description.materials.contains(lBMat)) {
                                    Framework.plugin.log("bd", "found rel " + lRel.description.name);
                                    lRelatedPos = lPos;
                                    lRelatedMaterial = lBMat;
                                    lRelated = true;
                                    lLastRel = lIndex;
                                }
                                /*
                                if (lSkip == 0) {
                                    Logger.getLogger("detect").info("check rel " + lRel.description.name + " at " + lPos + " mat " + lBlock.getType() + " " + lBlock.getData() + " -> " + lRel.description.materials.get(0));
                                    if (lRel.description.materials.contains(lBlock)) {
                                        Logger.getLogger("detect").info("found rel " + lRel.description.name);
                                        lRelatedPos = lPos;
                                        lRelated = true;
                                        break;
                                    } else if (!lRel.materials.isEmpty() && !lRel.materials.contains(lBlock)) {
                                        Logger.getLogger("detect").info("break rel " + lRel.description.name + " mat " + lBlock.getType() + " " + lRel.materials.get(0));
                                        break;
                                    }
                                } else {
                                    lSkip--;
                                    if (!lRel.materials.isEmpty() && !lRel.materials.contains(lBlock)) {
                                        Logger.getLogger("detect").info("break rel " + lRel.description.name + " mat " + lBlock.getType());
                                        break;
                                    }
                                }
                                */
                            }
                            break;
                        case AreaYX:
                        case AreaYZ:
                        case AreaXZ:
                            switch (lRel.position) {
                                case AreaYX:
                                    lCount = lPos2.z - lPos1.z;
                                    lPos2.z = lPos1.z;
                                    break;
                                case AreaYZ:
                                    lCount = lPos2.x - lPos1.x;
                                    lPos2.x = lPos1.x;
                                    break;
                                case AreaXZ:
                                    lCount = lPos2.y - lPos1.y;
                                    lPos2.y = lPos1.y;
                                    break;
                            }
                            for(int lStep = 0; lStep <= Math.abs(lCount); lStep++) {
                                lSkip = lRel.minDistance;
                                for(BlockPosition lPos : new WorldLineWalk(lPos1, lPos2)) {
                                    if (lFirst) {
                                        lFirst = false;
                                    } else {
                                        //Block lBlock = lPos.getBlock(aWorld);
                                        BlockMaterial lBMat = getBlockMaterial(aWorld, aEntities, lPos);
                                        if (lSkip == 0) {
                                            Framework.plugin.log("bd", "check rel " + lRel.description.name + " at " + lPos + " mat " + lBMat + " -> " + lRel.description.materials.get(0));
                                            if (lRel.description.materials.contains(lBMat)) {
                                                Framework.plugin.log("bd", "found rel " + lRel.description.name);
                                                lRelatedPos = lPos;
                                                lRelatedMaterial = lBMat;
                                                lRelated = true;
                                                break;
                                            } else if (!lRel.materials.isEmpty() && !lRel.materials.contains(lBMat)) {
                                                Framework.plugin.log("bd", "break rel " + lRel.description.name + " mat " + lBMat + " " + lRel.materials.get(0));
                                                break;
                                            }
                                        } else {
                                            lSkip--;
                                            if (!lRel.materials.isEmpty() && !lRel.materials.contains(lBMat)) {
                                                Framework.plugin.log("bd", "break rel " + lRel.description.name + " mat " + lBMat);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (lRelated)
                                    break;
                                switch (lRel.position) {
                                    case AreaYX:
                                        lPos1.z += lCount >= 0 ? 1 : -1;
                                        lPos2.z += lCount >= 0 ? 1 : -1;
                                        break;
                                    case AreaYZ:
                                        lPos1.x += lCount >= 0 ? 1 : -1;
                                        lPos2.x += lCount >= 0 ? 1 : -1;
                                        break;
                                    case AreaXZ:
                                        lPos1.y += lCount >= 0 ? 1 : -1;
                                        lPos2.y += lCount >= 0 ? 1 : -1;
                                        break;
                                }
                            }
                            break;
                        case Nearby:
                            for(BlockPosition lPos : new BlockPositionWalkAround(lStartPos, BlockPositionDelta.HorizontalAndVertical)) {
                                //Block lBlock = lPos.getBlock(aWorld);
                                BlockMaterial lBMat = getBlockMaterial(aWorld, aEntities, lPos);
                                if (lRel.description.materials.contains(lBMat)) {
                                    Framework.plugin.log("bd", "found rel nearby " + lRel.description.name);
                                    lRelated = true;
                                    lRelatedMaterial = lBMat;
                                    lRelatedPos = lPos.clone();
                                    break;
                                } else {
                                    Framework.plugin.log("bd", "break rel nearby " + lRel.description.name + " mat " + lBMat);
                                }
                            }
                            break;
                    }
                    if (!lRelated) {
                        Framework.plugin.log("bd", "rel " + lRel.description.name + " not match");
                        return false;
                    }
                    if (!aExcludes.contains(lRel.description)) {
                        RelFollower lF = new RelFollower();
                        lF.pos = lRelatedPos;
                        lF.desc = lRel.description;
                        lF.bmat = lRelatedMaterial;
                        lFs.add(lF);
                        //Logger.getLogger("detect").info("found rel2 " + lF.desc.name);
                    }
                }
                if (!aExcludes.contains(lBlockDesc)) {
                    aExcludes.add(lBlockDesc);
                    Framework.plugin.log("bd", "excluded add " + lBlockDesc.name + " " + aExcludes.size());
                }
                for(RelFollower lF : lFs) {
                    Framework.plugin.log("bd", "follow rel " + lF.desc.name);
                    if (!canFollowRelateds(aExcludes, aBuilding, aWorld, aEntities, lF.desc, lF.pos.x, lF.pos.y, lF.pos.z, lF.bmat)) {
                        Framework.plugin.log("bd", "rel2 " + lF.desc.name + " not match");
                        return false;
                    }
                }
                return true;
            } else {
                aExcludes.add(lBlockDesc);
                Framework.plugin.log("bd", "excluded add " + lBlockDesc.name + " " + aExcludes.size());
                return true;
            }
        } else {
            return true;
        }
    }

}
