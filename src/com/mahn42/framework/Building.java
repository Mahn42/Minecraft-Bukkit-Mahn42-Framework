/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class Building extends DBRecordWorld {
    public String name;
    public String playerName;
    public BuildingDescription description;
    public BlockPosition edge1 = new BlockPosition();
    public BlockPosition edge2 = new BlockPosition();
    public int maxHeight = 0;
    public int influenceRadius = 0;
    public ArrayList<BuildingBlock> blocks = new ArrayList<BuildingBlock>();

    public String getName() {
        return name == null ? description.name + "(" + playerName + ")" : name;
    }
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(name);
        aCols.add(playerName);
        aCols.add(description.name);
        aCols.add(edge1.x);
        aCols.add(edge1.y);
        aCols.add(edge1.z);
        aCols.add(edge2.x);
        aCols.add(edge2.y);
        aCols.add(edge2.z);
        String lBlocks = null;
        for(BuildingBlock lBlock : blocks) {
            if (lBlocks == null) {
                lBlocks = lBlock.toCSVValue();
            } else {
                lBlocks = lBlocks + "|" + lBlock.toCSVValue();
            }
        }
        aCols.add(lBlocks);
    }

    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        name = aCols.pop();
        playerName = aCols.pop();
        String lDescName = aCols.pop();
        description = Framework.plugin.getBuildingDescription(lDescName);
        edge1.x = aCols.popInt();
        edge1.y = aCols.popInt();
        edge1.z = aCols.popInt();
        edge2.x = aCols.popInt();
        edge2.y = aCols.popInt();
        edge2.z = aCols.popInt();
        String lBlocks = aCols.pop();
        String lBlockStr[] = lBlocks.split("|");
        for(String lCSVValue : lBlockStr ) {
            BuildingBlock lBlock = new BuildingBlock();
            lBlock.fromCSVValue(description, lCSVValue);
        }
    }
    
    public void update() {
        edge1.x = Integer.MAX_VALUE;
        edge1.y = Integer.MAX_VALUE;
        edge1.z = Integer.MAX_VALUE;
        edge2.x = Integer.MIN_VALUE;
        edge2.y = Integer.MIN_VALUE;
        edge2.z = Integer.MIN_VALUE;
        for(BuildingBlock lBlock : blocks) {
            edge1.x = Math.min(edge1.x, lBlock.position.x);
            edge1.y = Math.min(edge1.y, lBlock.position.y);
            edge1.z = Math.min(edge1.z, lBlock.position.z);
            edge2.x = Math.max(edge2.x, lBlock.position.x);
            edge2.y = Math.max(edge2.y, lBlock.position.y);
            edge2.z = Math.max(edge2.z, lBlock.position.z);
        }
        if (world != null) {
            maxHeight = 0;
            for(int lX = edge1.x; lX <= edge2.x; lX++) {
                for(int lZ = edge1.z; lZ <= edge2.z; lZ++) {
                    int lHeight = world.getHighestBlockYAt(lX, lZ);
                    if (lHeight > maxHeight) {
                        maxHeight = lHeight;
                    }
                }
            }
            if (description.position == BuildingDescription.Position.onGround) {
                edge2.y = maxHeight;
            }
        }
        influenceRadius = (int) Math.round(description.influenceRadiusFactor * (edge2.y - edge1.y));
    }
    
    @Override
    protected void added(DBSet aSet) {
        super.added(aSet);
        update();
    }

    public boolean isInside(BlockPosition aPos) {
        return aPos.x >= edge1.x && aPos.x <= edge2.x
            && aPos.y >= edge1.y && aPos.y <= edge2.y
            && aPos.z >= edge1.z && aPos.z <= edge2.z;
    }
    
    public BuildingBlock getBlock(BlockPosition aPos) {
        for(BuildingBlock lBlock : blocks) {
            if (lBlock.position.equals(aPos)) {
                return lBlock;
            }
        }
        return null;
    }

    public BuildingBlock getBlock(BuildingDescription.BlockDescription aDesc) {
        for(BuildingBlock lBlock : blocks) {
            if (lBlock.description.equals(aDesc)) {
                return lBlock;
            }
        }
        return null;
    }

    public BuildingBlock getBlock(String aBlockName) {
        for(BuildingBlock lBlock : blocks) {
            if (lBlock.description.name.equals(aBlockName)) {
                return lBlock;
            }
        }
        return null;
    }

    public BuildingBlock getNearestBlock(BlockPosition aPos) {
        double lResDist = Double.MAX_VALUE;
        BuildingBlock lResult = null;
        Vector lV1 = aPos.getVector();
        for(BuildingBlock lBlock : blocks) {
            Vector lV2 = lBlock.position.getVector();
            double lDistance = lV1.distance(lV2);
            if (lDistance < lResDist) {
                lResDist = lDistance;
                lResult = lBlock;
            }
        }
        return lResult;
    }

    public BuildingBlock getRedStoneSensibles(BlockPosition aPos) {
        for(BuildingBlock lBlock : blocks) {
            if (lBlock.description.redstoneSensible && lBlock.position.nearly(aPos)) {
                return lBlock;
            }
        }
        return null;
    }

    @Override
    public void cloneFrom(DBRecord aRecord) {
        super.cloneFrom(aRecord);
        if (aRecord instanceof Building) {
            Building lBuilding = (Building)aRecord;
            name = lBuilding.name;
            playerName = lBuilding.playerName;
            description = lBuilding.description;
            edge1.cloneFrom(lBuilding.edge1);
            edge2.cloneFrom(lBuilding.edge2);
            maxHeight = lBuilding.maxHeight;
            influenceRadius = lBuilding.influenceRadius;
            blocks.clear();
            blocks.addAll(lBuilding.blocks);
        }
    }
}
