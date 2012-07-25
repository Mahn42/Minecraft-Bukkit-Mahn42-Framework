/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
public class BuildingDetectTask implements Runnable {

    public Player player;
    public PlayerInteractEvent event;
    public World world;
    public BlockPosition position;
    
    @Override
    public void run() {
        ArrayList<Building> lBuildings;
        lBuildings = Framework.plugin.getBuildingDetector().getBuildingsWithNoneShareableBlock(position);
        if (lBuildings.size() == 1) {
            if (lBuildings.get(0).description.handler != null) {
                lBuildings.get(0).description.handler.nextConfiguration(lBuildings.get(0), position, player);
                return;
            }
        } else {
            if (player != null) {
                for(Building lBuilding : lBuildings) {
                    player.sendMessage("Here is always the building " + lBuilding.getName());
                }
            }
        }
        if (lBuildings.isEmpty()) {
            lBuildings = Framework.plugin.getBuildingDetector().detect(world, position, position);
            boolean lFound = false;
            for(Building lBuilding : lBuildings) {
                if (player != null) {
                    lBuilding.playerName = player.getName();
                }
                if (lBuilding.description.handler != null) {
                    if (lBuilding.description.handler.playerInteract(event, lBuilding)) {
                        BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.Create);
                        Framework.plugin.getServer().getPluginManager().callEvent(lEvent);
                        lFound = true;
                        break;
                    }
                }
            }
            if (!lFound) {
                if (player != null) {
                    player.sendMessage("No building found!");
                }
            }
        }
    }
    
}
