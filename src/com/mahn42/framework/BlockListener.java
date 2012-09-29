/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author andre
 */
public class BlockListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void explode(EntityExplodeEvent aEvent) {
        if (!aEvent.isCancelled()) {
            List<Block> lBlocks = aEvent.blockList();
            for(Block lBlock : lBlocks) {
                BlockBreakEvent lBreak = new BlockBreakEvent(lBlock, null);
                breakBlock(lBreak);
                if (lBreak.isCancelled()) {
                    aEvent.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void placeBlock(BlockPlaceEvent aEvent) {
        if (!aEvent.isCancelled()) {
            Player lPlayer = aEvent.getPlayer();
            Block lBlock = aEvent.getBlock();
            World lWorld = lBlock.getWorld();
            RestrictedRegions lRegions = Framework.plugin.getRestrictedRegions(lWorld, false);
            if (lRegions != null && lPlayer != null && !lRegions.allowed(lBlock, lPlayer.getName())) {
                aEvent.setCancelled(true);
            } else {

            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void breakBlock(BlockBreakEvent aEvent) {
        if (!aEvent.isCancelled()) {
            Player lPlayer = aEvent.getPlayer();
            Block lBlock = aEvent.getBlock();
            World lWorld = lBlock.getWorld();
            RestrictedRegions lRegions = Framework.plugin.getRestrictedRegions(lWorld, false);
            if (lRegions != null && lPlayer != null && !lRegions.allowed(lBlock, lPlayer.getName())) {
                aEvent.setCancelled(true);
            } else {
                ArrayList<BuildingHandler> lHandlers = Framework.plugin.getBuildingDetector().getHandlers();
                boolean lFound;
                for (BuildingHandler lHandler : lHandlers) {
                    BuildingDB lDB = lHandler.getDB(lWorld);
                    ArrayList<Building> lBuildings = lDB.getBuildingsWithBlock(new BlockPosition(lBlock.getLocation()));
                    for(Building lBuilding : lBuildings) {
                        if (lBuilding.description.handler != null) {
                            lFound = lBuilding.description.handler.breakBlock(aEvent, lBuilding);
                            if (lFound) {
                                BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.Destroy);
                                lEvent.raise();
                                if (lPlayer != null) {
                                    lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "Building %s is destroyed!", lBuilding.getName()));
                                }
                                if (lPlayer == null || !lPlayer.getName().equals(lBuilding.playerName)) {
                                    Framework.plugin.getMessenger().sendPlayerMessage(null, lBuilding.playerName, Framework.plugin.getText(lPlayer, "Your building %s is destroyed!", lBuilding.getName()));
                                }
                            }
                        }
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
            if (lDB != null) {
                ArrayList<Building> lBuildings = lDB.getRedStoneSensibles(lPos);
                for(Building lBuilding : lBuildings) {
                    if (lBuilding.description.handler != null) {
                        lFound = lBuilding.description.handler.redstoneChanged(aEvent, lBuilding);
                    }
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
