/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author andre
 */
public class BlockListener implements Listener {
    
    @EventHandler
    public void breakBlock(BlockBreakEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        Block lBlock = aEvent.getBlock();
        World lWorld = lBlock.getWorld();
        ArrayList<BuildingHandler> lHandlers = Framework.plugin.getBuildingDetector().getHandlers();
        boolean lFound;
        for (BuildingHandler lHandler : lHandlers) {
            BuildingDB lDB = lHandler.getDB(lWorld);
            ArrayList<Building> lBuildings = lDB.getBuildingsWithBlock(new BlockPosition(lBlock.getLocation()));
            for(Building lBuilding : lBuildings) {
                if (lBuilding.description.handler != null) {
                    lFound = lBuilding.description.handler.breakBlock(aEvent, lBuilding);
                    if (lFound) {
                        lPlayer.sendMessage("Building " + lBuilding.getName() + " is destroyed!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void redstoneBlock(BlockRedstoneEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        World lWorld = lBlock.getWorld();
        BlockPosition lPos = new BlockPosition(lBlock.getLocation());
        ArrayList<BuildingHandler> lHandlers = Framework.plugin.getBuildingDetector().getHandlers();
        boolean lFound = false;
        for (BuildingHandler lHandler : lHandlers) {
            BuildingDB lDB = lHandler.getDB(lWorld);
            ArrayList<Building> lBuildings = lDB.getRedStoneSensibles(lPos);
            for(Building lBuilding : lBuildings) {
                if (lBuilding.description.handler != null) {
                    lFound = lBuilding.description.handler.redstoneChanged(aEvent, lBuilding);
                }
            }
        }
    }

    @EventHandler
    public void signChanged(SignChangeEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        World lWorld = lBlock.getWorld();
        BlockPosition lPos = new BlockPosition(lBlock.getLocation());
        ArrayList<BuildingHandler> lHandlers = Framework.plugin.getBuildingDetector().getHandlers();
        boolean lFound = false;
        for (BuildingHandler lHandler : lHandlers) {
            BuildingDB lDB = lHandler.getDB(lWorld);
            ArrayList<Building> lBuildings = lDB.getSignSensibles(lPos);
            for(Building lBuilding : lBuildings) {
                if (lBuilding.description.handler != null) {
                    lFound = lBuilding.description.handler.signChanged(aEvent, lBuilding);
                }
            }
            lBuildings = lDB.getNameSensibles(lPos);
            for(Building lBuilding : lBuildings) {
                if (lBuilding.description.handler != null) {
                    lFound = lBuilding.description.handler.nameChanged(aEvent, lBuilding);
                }
            }
        }
    }
}
