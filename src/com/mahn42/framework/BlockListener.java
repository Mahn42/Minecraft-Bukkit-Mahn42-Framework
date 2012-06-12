/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 *
 * @author andre
 */
public class BlockListener implements Listener {
    
    @EventHandler
    public void breakBlock(BlockBreakEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        //TODO check if block is part of a building
        // if so then delete building record
    }

    @EventHandler
    public void redstoneBlock(BlockRedstoneEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        World lWorld = lBlock.getWorld();
        BlockPosition lPos = new BlockPosition(lBlock.getLocation());
        ArrayList<BuildingHandler> lHandlers = Framework.plugin.getBuildingDetector().getHandlers();
        ArrayList<Building> lBuildings; // = new ArrayList<Building>();
        boolean lFound = false;
        for (BuildingHandler lHandler : lHandlers) {
            BuildingDB lDB = lHandler.getDB(lWorld);
            lBuildings = lDB.getRedStoneSensibles(lPos);
            for(Building lBuilding : lBuildings) {
                Framework.plugin.getLogger().info("Cur: " + aEvent.getNewCurrent());
                Framework.plugin.getLogger().info("B:" + lBuilding.toCSV());
                Framework.plugin.getLogger().info("BB:" + lBuilding.getRedStoneSensibles(lPos));
                if (lBuilding.description.handler != null) {
                    lFound = lBuilding.description.handler.redstoneChanged(aEvent, lBuilding);
                }
            }
        }
    }
}
