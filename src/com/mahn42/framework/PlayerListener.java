/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author andre
 */
public class PlayerListener implements Listener {

    static Block lLastBlock;
    
    @EventHandler
    public void playerMove(PlayerMoveEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        Block lTargetBlock = lPlayer.getTargetBlock(null, 100);
        if (!lTargetBlock.equals(lLastBlock)) {
            //lPlayer.sendMessage("Block " + lBlock);
            lLastBlock = lTargetBlock;
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
                    //TODO raise event enter building
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerEnter);
                    Framework.plugin.getServer().getPluginManager().callEvent(lEvent);
                    //lPlayer.sendMessage("you enter building " + lBuilding.getName());
                }
            }
            ArrayList<Building> lInBuildings = new ArrayList<Building>(lPBuilds.inBuildings);
            for(Building lBuilding : lInBuildings) {
                if (!lBuildings.contains(lBuilding)) {
                    lPBuilds.inBuildings.remove(lBuilding);
                    //TODO raise event leave building
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerLeave);
                    Framework.plugin.getServer().getPluginManager().callEvent(lEvent);
                    //lPlayer.sendMessage("you leave building " + lBuilding.getName());
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
            ArrayList<Building> lBuildings;
            BlockPosition lPos = new BlockPosition(lBlock.getLocation());
            lBuildings = Framework.plugin.getBuildingDetector().getBuildings(lPos);
            if (lBuildings.isEmpty()) {
                lBuildings = Framework.plugin.getBuildingDetector().detect(lWorld, lPos, lPos);
                boolean lFound = false;
                for(Building lBuilding : lBuildings) {
                    lBuilding.playerName = lPlayer.getName();
                    if (lBuilding.description.handler != null) {
                        if (lBuilding.description.handler.playerInteract(aEvent, lBuilding)) {
                            lFound = true;
                        }
                    }
                }
                if (!lFound) {
                    lPlayer.sendMessage("No building found!");
                }
            } else {
                for(Building lBuilding : lBuildings) {
                    lPlayer.sendMessage("Here is always the building " + lBuilding.getName());
                }
            }
        }
    }
}
