/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class ProjectionAreas extends ArrayList<ProjectionArea> {
    public World world = null;
    protected ArrayList<BlockPosition> fScanList = new ArrayList<BlockPosition>();
    
    public ProjectionAreas(World aWorld) {
        world = aWorld;
    }
    
    public void scanPos(SyncBlockList aList, BlockPosition aPos) {
        for(ProjectionArea lArea : this) {
            if (lArea.area.isBetween(aPos)) {
                lArea.scanPos(aList, aPos);
            }
        }
    }

    public void addForScan(BlockPosition aPosition) {
        synchronized(fScanList) {
            fScanList.add(aPosition);
        }
    }
    
    public void run() {
        ArrayList<BlockPosition> lList;
        synchronized(fScanList) {
            lList = fScanList;
            fScanList = new ArrayList<BlockPosition>();
        }
        if (!lList.isEmpty()) {
            SyncBlockList lSync = new SyncBlockList(world);
            for(BlockPosition lPos : lList) {
                scanPos(lSync, lPos);
            }
            lList.clear();
            lSync.execute();
        }
    }
}
