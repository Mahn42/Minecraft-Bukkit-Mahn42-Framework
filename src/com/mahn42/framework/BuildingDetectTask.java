/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Material;
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
    public Material inHand;

    @Override
    public void run() {
        ArrayList<Building> lBuildings;
        lBuildings = Framework.plugin.getBuildingDetector().getBuildingsWithNoneShareableBlock(world, position);
        if (lBuildings.size() == 1) {
            if (Framework.plugin.checkBuildingPermission(player, lBuildings.get(0))) {
                if (lBuildings.get(0).description.handler != null) {
                    if (inHand != null && inHand.equals(Material.BOOK)) {
                        lBuildings.get(0).description.handler.nextConfiguration(lBuildings.get(0), position, player);
                    } else {
                        lBuildings.get(0).description.handler.playerInteractWith(lBuildings.get(0), position, player, inHand);
                    }
                    return;
                }
            } else {
                player.sendMessage(Framework.plugin.getText(player, "&cYou have no permission for this kind of building!"));
            }
        } else if (lBuildings.size() > 1) {
            if (inHand != null && inHand.equals(Material.BOOK)) {
                if (player != null) {
                    for (Building lBuilding : lBuildings) {
                        player.sendMessage(Framework.plugin.getText(player, "Here is always the building %s!", lBuilding.getName()));
                    }
                } else {
                    for (Building lBuilding : lBuildings) {
                        Framework.plugin.getLogger().info("Here is always the building " + lBuilding.getName());
                    }
                }
            }
        }
        if (inHand != null && inHand.equals(Material.BOOK)
                && lBuildings.isEmpty()) {
            lBuildings = Framework.plugin.getBuildingDetector().detect(world, position, position);
            boolean lFound = false;
            for (Building lBuilding : lBuildings) {
                if (player != null) {
                    lBuilding.playerName = player.getName();
                }
                //Framework.plugin.getLogger().info("1");
                if (lBuilding.description.handler != null) {
                    //Framework.plugin.getLogger().info("2");
                    boolean lOK = false;
                    if (Framework.plugin.checkBuildingPermission(player, lBuilding)) {
                        if (event != null) {
                            //Framework.plugin.getLogger().info("3");
                            lOK = lBuilding.description.handler.playerInteract(event, lBuilding);
                        } else {
                            //Framework.plugin.getLogger().info("4 " + lBuilding.description.handler.getClass().getName());
                            lOK = lBuilding.description.handler.insert(lBuilding) != null;
                        }
                    } else {
                        player.sendMessage(Framework.plugin.getText(player, "&cYou have no permission for this kind of building!"));
                    }
                    if (lOK) {
                        //Framework.plugin.getLogger().info("5");
                        BuildingEvent lEvent = new BuildingEvent(lBuilding, BuildingEvent.BuildingAction.Create);
                        lEvent.raise();
                        lFound = true;
                        break;
                    }
                } else {
                    Framework.plugin.getLogger().info("Building with no handler!");
                }
            }
            if (!lFound) {
                if (player != null) {
                    player.sendMessage(Framework.plugin.getText(player, "&cNo building found!"));
                } else {
                    Framework.plugin.getLogger().info("No building found! (" + lBuildings.size() + ")");
                }
            }
        }
    }
}
