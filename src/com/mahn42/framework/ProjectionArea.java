/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class ProjectionArea {
    public Vector vector = new Vector(1, 1, 1);
    public Vector scale = new Vector(1, 1, 1);
    public BlockRect area = new BlockRect();
    public BlockPosition destination = new BlockPosition();
    
    public ProjectionArea(BlockRect aArea, BlockPosition aDest) {
        area.cloneFrom(aArea);
        destination.cloneFrom(aDest);
    }
    
    public void scanPos(SyncBlockList aList, BlockPosition aSource) {
        //if (area.isBetween(aSource)) {
        BlockPosition lPos = aSource.clone();
        lPos.subtract(area.edge1);
        lPos.multiply(vector);
        lPos.add(destination);
        BlockState aState = aSource.getBlock(aList.world).getState();
        for(int x = 1; x <= scale.getBlockX(); x++) {
            for(int y = 1; y <= scale.getBlockY(); y++) {
                for(int z = 1; z <= scale.getBlockZ(); z++) {
                    BlockPosition lDest = lPos.clone();
                    lDest.add(x - 1, y - 1, z - 1);
                    aList.add(lDest, aState.getType(), aState.getRawData());
                }
            }
        }
        //}
    }
    
    public void scanAll(SyncBlockList aList) {
        for(int x = area.edge1.x; x <= area.edge2.x; x++) {
            for(int y = area.edge1.y; y <= area.edge2.y; y++) {
                for(int z = area.edge1.z; z <= area.edge2.z; z++) {
                    BlockPosition lSrc = new BlockPosition(x, y, z);
                    scanPos(aList, lSrc);
                }
            }
        }
    }
}
