/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class BuildingDescription {

    public enum RelatedPosition {
        Vector,
        Nearby
    }
    
    public class RelatedTo {
        public Vector direction;
        public RelatedPosition position = RelatedPosition.Vector;
        public String block;
        public BlockDescription description;
        public int minDistance = 0;
        public ArrayList<Material> materials = new ArrayList<Material>();
        
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
    }
    
    public class BlockDescription {
        public String name;
        public ArrayList<Material> materials = new ArrayList<Material>();
        public boolean redstoneSensible = false;
        public boolean nameSensible = false;
        public boolean signSensible = false;
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
        
        @Override
        public String toString() {
            return name;
        }

        public void cloneFrom(BlockDescription aDesc) {
            name = aDesc.name;
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
    }
    
    public enum Position {
        onGround,
        underGround,
        everywhere
    }
    
    public String typeName;
    public String name;
    public Position position = Position.onGround;
    public ArrayList<BlockDescription> blocks = new ArrayList<BlockDescription>();
    public boolean active;
    public double influenceRadiusFactor = 0;
    public BuildingHandler handler = null;
    
    public BuildingDescription() {
    }
    
    public BuildingDescription(String aName) {
        name = aName;
    }
    
    public String getName() {
        return typeName != null ? typeName : name;
    }

    public void cloneFrom(BuildingDescription aDesc) {
        typeName = aDesc.typeName;
        if (name == null || name.isEmpty()) {
            name = aDesc.name;
        }
        position = aDesc.position;
        blocks.clear();
        for(BlockDescription lBDesc : aDesc.blocks) {
            BlockDescription lNew = new BlockDescription();
            lNew.cloneFrom(lBDesc);
            blocks.add(lNew);
        }
        influenceRadiusFactor = aDesc.influenceRadiusFactor;
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
    
    public void activate() {
        HashMap<String, BlockDescription> lHash = new HashMap<String, BlockDescription>();
        for(BlockDescription lBDesc : blocks) {
            lHash.put(lBDesc.name, lBDesc);
        }
        for(BlockDescription lBDesc : blocks) {
            for(RelatedTo lRel : lBDesc.relatedTo) {
                lRel.description = lHash.get(lRel.block);
                if (lRel.description == null) {
                    Framework.plugin.getLogger().info("block description " + lRel.block + " of building description '" + name + "' not found!");
                }
            }
        }
        active = true;
    }

    public Building matchDescription(World aWorld, int lX, int lY, int lZ) {
        Material lMat = aWorld.getBlockAt(lX, lY, lZ).getType();
        ArrayList<BlockDescription> lExcludes = new ArrayList<BlockDescription>();
        Logger.getLogger("detect").info(this.name);
        for(BlockDescription lBlockDesc : blocks) {
            if (lBlockDesc.materials.contains(lMat)) {
                lExcludes.clear();
                Building aBuilding = new Building();
                aBuilding.description = this;
                aBuilding.setWorld(aWorld);
                //Logger.getLogger("detect").info("mat " + lMat.name());
                if (!canFollowRelateds(lExcludes, aBuilding, aWorld, lBlockDesc, lX, lY, lZ)) {
                    Logger.getLogger("detect").info("not ok");
                    //return null;
                } else {
                    if (lExcludes.size() >= blocks.size()) {
                        aBuilding.update();
                        Logger.getLogger("detect").info("found: " + this.name);
                        return aBuilding;
                    } else {
                        Logger.getLogger("detect").info("not enough matches (excluder)");
                    }
                }
            }
        }
        Logger.getLogger("detect").info(this.name + " does not match!");
        return null;
    }
    
    protected class RelFollower {
        BlockPosition pos;
        BlockDescription desc;
    }
    private boolean canFollowRelateds(ArrayList<BlockDescription> aExcludes, Building aBuilding, World aWorld, BlockDescription lBlockDesc, int lX, int lY, int lZ) {
        if (!aExcludes.contains(lBlockDesc)) {
            BlockPosition lStartPos = new BlockPosition(lX, lY, lZ);
            aBuilding.blocks.add(new BuildingBlock(lBlockDesc, new BlockPosition(lStartPos)));
            if (lBlockDesc.relatedTo.size() > 0) {
                Logger.getLogger("detect").info("check desc " + lBlockDesc.name + " at " + lStartPos);
                ArrayList<RelFollower> lFs = new ArrayList<RelFollower>();
                for(RelatedTo lRel : lBlockDesc.relatedTo) {
                    boolean lRelated = false;
                    boolean lFirst = true;
                    BlockPosition lRelatedPos = null;
                    switch(lRel.position) {
                        case Vector:
                            int lSkip = lRel.minDistance;
                            for(BlockPosition lPos : new WorldLineWalk(
                                    lStartPos,
                                    new BlockPosition(lX + lRel.direction.getBlockX(), lY + lRel.direction.getBlockY(), lZ + lRel.direction.getBlockZ()))) {
                                if (lFirst) {
                                    lFirst = false;
                                } else {
                                    Material lMat = lPos.getBlockType(aWorld);
                                    if (lSkip >= 0) {
                                        Logger.getLogger("detect").info("rel " + lRel.description.name + " at " + lPos + " mat " + lPos.getBlockType(aWorld).name());
                                        if (lRel.description.materials.contains(lMat)) {
                                            Logger.getLogger("detect").info("found rel1 " + lRel.description.name);
                                            lRelatedPos = lPos;
                                            lRelated = true;
                                            break;
                                        } else if (!lRel.materials.isEmpty() && !lRel.materials.contains(lMat)) {
                                            Logger.getLogger("detect").info("break rel1 " + lRel.description.name + " mat " + lMat.toString());
                                            break;
                                        }
                                    } else {
                                        lSkip--;
                                        if (!lRel.materials.isEmpty() && !lRel.materials.contains(lMat)) {
                                            Logger.getLogger("detect").info("break rel1 " + lRel.description.name + " mat " + lMat.toString());
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        case Nearby:
                            int lLength = (int)Math.round(lRel.direction.length());
                            for(int dx = -lLength; dx >= lLength; dx++) {
                                for(int dy = -lLength; dy >= lLength; dy++) {
                                    for(int dz = -lLength; dz >= lLength; dz++) {
                                        if (dx != 0 && dy != 0 && dz != 0) {
                                            if (lRel.description.materials.contains(lStartPos.getBlockAt(aWorld, dx, dy, dz).getType())) {
                                                lRelated = true;
                                                lRelatedPos = new BlockPosition(lX + dx, lY + dy, lZ + dz);
                                                break;
                                            }
                                        }
                                    }
                                    if (lRelated) break;
                                }
                                if (lRelated) break;
                            }
                            break;
                    }
                    if (!lRelated) {
                        Logger.getLogger("detect").info("rel1 " + lRel.description.name + " not match");
                        return false;
                    }
                    if (!aExcludes.contains(lRel.description)) {
                        RelFollower lF = new RelFollower();
                        lF.pos = lRelatedPos;
                        lF.desc = lRel.description;
                        lFs.add(lF);
                        //Logger.getLogger("detect").info("found rel2 " + lF.desc.name);
                    }
                }
                if (!aExcludes.contains(lBlockDesc)) {
                    Logger.getLogger("detect").info("excluded add " + lBlockDesc.name);
                    aExcludes.add(lBlockDesc);
                }
                for(RelFollower lF : lFs) {
                    Logger.getLogger("detect").info("follow rel " + lF.desc.name);
                    if (!canFollowRelateds(aExcludes, aBuilding, aWorld, lF.desc, lF.pos.x, lF.pos.y, lF.pos.z)) {
                        Logger.getLogger("detect").info("rel2 " + lF.desc.name + " not match");
                        return false;
                    }
                }
                return true;
            } else {
                Logger.getLogger("detect").info("excluded add " + lBlockDesc.name);
                aExcludes.add(lBlockDesc);
                return true;
            }
        } else {
            return true;
        }
    }

}
