/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
public class PlayerListener implements Listener {

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
