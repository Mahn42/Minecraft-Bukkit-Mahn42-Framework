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
import org.bukkit.block.Sign;
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
        if (lBlock != null
                && aEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && aEvent.hasItem()
                && lInHand.equals(Material.BOOK)
                && lBlock.getType().equals(Material.SIGN_POST)) {
            Sign lSign = (Sign)lBlock.getState();
            String[] lLines = lSign.getLines();
            //lPlayer.sendMessage(lLines);
            if (lLines.length > 0) {
                String lName = "";
                for(String lLine : lLines) {
                    lName += lLine;
                }
                ArrayList<BuildingDescription.BlockDescription> lDones = new ArrayList<BuildingDescription.BlockDescription>();
                BuildingDescription lDesc = Framework.plugin.getBuildingDescription(lName);
                if (lDesc != null) {
                    lPlayer.sendMessage(lDesc.name);
                    BuildingDescription.BlockDescription lBDesc = lDesc.blocks.get(0);
                    BlockPosition lPos = new BlockPosition(lBlock.getLocation());
                    debugDesc(lDones, lPos, lBDesc, lWorld);
                } else {
                    lPlayer.sendMessage("'" + lName + "' desc unkown");
                }
            }
        }
    }
    
    protected void debugDesc(ArrayList<BuildingDescription.BlockDescription> aDones, BlockPosition aPos, BuildingDescription.BlockDescription aBDesc, World aWorld) {
        if (!aDones.contains(aBDesc)) {
            aDones.add(aBDesc);
            aPos.getBlock(aWorld).setType(aBDesc.materials.get(0).material);
            if (aBDesc.materials.get(0).withData) {
                aPos.getBlock(aWorld).setData(aBDesc.materials.get(0).data);
            }
            for(BuildingDescription.RelatedTo lRel : aBDesc.relatedTo) {
                BlockPosition lRelPos = aPos.clone();
                lRelPos.add(lRel.direction);
                if (!lRel.materials.isEmpty()) {
                    for(BlockPosition lPos : new WorldLineWalk(aPos, lRelPos)) {
                        if (!lPos.equals(aPos) && !lPos.equals(lRelPos)) {
                            lPos.getBlock(aWorld).setType(lRel.materials.get(0).material);
                            if (lRel.materials.get(0).withData) {
                                aPos.getBlock(aWorld).setData(lRel.materials.get(0).data);
                            }
                        }
                    }
                }
                debugDesc(aDones, lRelPos, lRel.description, aWorld);
            }
        }
    }
}
