/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author andre
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void playerChangedWorld(PlayerChangedWorldEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        World lWorld = lPlayer.getWorld();
        WorldConfiguration lConf = Framework.plugin.getWorldConfigurationDB().getByName(lWorld.getName());
        if (lConf != null) {
            if (lConf.gameMode != lPlayer.getGameMode()) {
                lPlayer.setGameMode(lConf.gameMode);
            }
            if (lConf.ownInventory) {
                lPlayer.getInventory().clear();
                //TODO save inventory in aEvent.getFrom() world
                // load inventory for current world
            }
        }
    }
    
    static Block lLastBlock;
    
    @EventHandler
    public void playerMove(PlayerMoveEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        if (Framework.plugin.isDebugSet("targetblock")) {
            Block lTargetBlock = lPlayer.getTargetBlock(null, 100);
            if (!lTargetBlock.equals(lLastBlock)) {
                    lPlayer.sendMessage("Block " + lTargetBlock);
                lLastBlock = lTargetBlock;
            }
        }
        
        PlayerBuildings lPBuilds = Framework.plugin.getPlayerBuildings(lPlayer);
        BlockPosition lPPos = new BlockPosition(lPlayer.getLocation());
        if (!lPPos.equals(lPBuilds.playerPos)) {
            lPBuilds.playerPos = lPPos;
            ArrayList<Building> lBuildings;
            lBuildings = Framework.plugin.getBuildingDetector().getBuildings(lPBuilds.playerPos);
            for(Building lBuilding : lBuildings) {
                if (!lPBuilds.inBuildings.contains(lBuilding)) {
                    lPBuilds.inBuildings.add(lBuilding);
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerEnter);
                    lEvent.raise();
                }
            }
            ArrayList<Building> lInBuildings = new ArrayList<Building>(lPBuilds.inBuildings);
            for(Building lBuilding : lInBuildings) {
                if (!lBuildings.contains(lBuilding)) {
                    lPBuilds.inBuildings.remove(lBuilding);
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerLeave);
                    lEvent.raise();
                }
            }
        }
    }
    
    @EventHandler
    public void playerInteract(PlayerInteractEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        World lWorld = lPlayer.getWorld();
        Block lBlock = aEvent.getClickedBlock();
        Material lInHand = null;
        if (aEvent.hasItem()) {
          lInHand = aEvent.getItem().getType();
        }
        if (lBlock != null
                && aEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && aEvent.hasItem()
                && lInHand.equals(Material.BOOK)) {
            BuildingDetectTask lTask = new BuildingDetectTask();
            lTask.player = lPlayer;
            lTask.world = lWorld;
            lTask.event = aEvent;
            lTask.position = new BlockPosition(lBlock.getLocation());
            Framework.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(Framework.plugin, lTask);
        }
        /*
        if (lBlock != null
                && aEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && aEvent.hasItem()
                && lInHand.equals(Material.EMERALD)) {
            //TODO protect building
        }
        */
    }
}
