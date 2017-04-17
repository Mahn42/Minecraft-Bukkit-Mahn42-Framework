/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
        WorldConfiguration lConfFrom = Framework.plugin.getWorldConfigurationDB().getByName(aEvent.getFrom().getName());
        if (lConf != null) {
            if (lConf.gameMode != lPlayer.getGameMode()) {
                //Logger.getLogger("xx").info("set gamemode = " + lConf.gameMode);
                lPlayer.setGameMode(lConf.gameMode);
            }
            WorldPlayerInventoryDB lInvDB = Framework.plugin.getWorldPlayerInventoryDB();
            if (lInvDB != null) {
                WorldPlayerInventory lInv = lInvDB.getOrCreate(lConfFrom.inventoryName, lPlayer.getName());
                lInv.setFromInventory(lPlayer.getInventory());
                lInv.exp = lPlayer.getExp();
                lInv.level = lPlayer.getLevel();
                //Logger.getLogger("xx").info("set inventory for player " + lInv.playerName + " inv items " + lPlayer.getInventory().getSize() + " in world " + lConfFrom.name);
            }
            if (!lConf.noInventory && !lConf.inventoryName.equalsIgnoreCase(lConfFrom.inventoryName)) {
                WorldPlayerInventory lInv = lInvDB.getOrCreate(lConf.inventoryName, lPlayer.getName());
                lInv.setToInventory(lPlayer.getInventory());
                lPlayer.setLevel(lInv.level);
                lPlayer.setExp(lInv.exp);
                //Logger.getLogger("xx").info("get inventory for player " + lInv.playerName + " inv items " + lPlayer.getInventory().getSize() + " in world " + lConf.name);
            }
        }
    }
    
    HashMap<String, BlockPosition> fPlayerBlockPosList = new HashMap<String, BlockPosition>();
    
    static Block lLastBlock;
    
    @EventHandler
    public void playerMove(PlayerMoveEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();

        if (Framework.plugin.isDebugSet("targetblock")) {
            Block lTargetBlock = lPlayer.getTargetBlock((Set<Material>) null, 100);
            if (!lTargetBlock.equals(lLastBlock)) {
                    lPlayer.sendMessage("Block " + lTargetBlock);
                lLastBlock = lTargetBlock;
            }
        }
        
        BlockPosition lPPos = new BlockPosition(lPlayer.getLocation());

        WorldPlayerSettingsDB lPSDB = Framework.plugin.getWorldPlayerSettingsDB(lPlayer.getWorld().getName());
        WorldPlayerSettings lSet = lPSDB.getOrCreateByName(lPlayer.getName());
        if (!lSet.position.equals(lPPos)) {
            PlayerPositionChangedEvent lEvent = new PlayerPositionChangedEvent(lPlayer, lSet.position, lPPos);
            lSet.position.cloneFrom(lPPos);
            lEvent.raise();
        }
    }
    
    @EventHandler
    public void playerPositionChanged(PlayerPositionChangedEvent aEvent) {
        Player lPlayer = aEvent.getPlayer();
        PlayerBuildings lPBuilds = Framework.plugin.getPlayerBuildings(lPlayer);
        if (!aEvent.getTo().equals(lPBuilds.playerPos)) {
            lPBuilds.playerPos = aEvent.getTo();
            ArrayList<Building> lBuildings;
            lBuildings = Framework.plugin.getBuildingDetector().getBuildings(lPlayer.getWorld(), lPBuilds.playerPos);
            for(Building lBuilding : lBuildings) {
                if (!lPBuilds.inBuildings.contains(lBuilding)) {
                    lPBuilds.inBuildings.add(lBuilding);
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerEnter, aEvent.getPlayer());
                    lEvent.raise();
                }
            }
            ArrayList<Building> lInBuildings = new ArrayList<Building>(lPBuilds.inBuildings);
            for(Building lBuilding : lInBuildings) {
                if (!lBuildings.contains(lBuilding)) {
                    lPBuilds.inBuildings.remove(lBuilding);
                    BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.PlayerLeave, aEvent.getPlayer());
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
                && lInHand != null
                && aEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && aEvent.hasItem()) {
            //if (lInHand.equals(Material.BOOK)) {
                BuildingDetectTask lTask = new BuildingDetectTask();
                lTask.player = lPlayer;
                lTask.world = lWorld;
                lTask.event = aEvent;
                lTask.inHand = lInHand;
                lTask.position = new BlockPosition(lBlock.getLocation());
                Framework.plugin.getServer().getScheduler().runTaskAsynchronously(Framework.plugin, lTask);
            //}
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
    
    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent aEvent) {
    }
}
