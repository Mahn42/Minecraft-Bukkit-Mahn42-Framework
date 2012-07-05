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
        List<Block> aLineOfSight = lPlayer.getLastTwoTargetBlocks(null, 100);
        if (aLineOfSight.size() > 0) {
            Block lBlock = aLineOfSight.get(aLineOfSight.size()-1);
            if (!lBlock.equals(lLastBlock)) {
                //lPlayer.sendMessage("Block " + lBlock);
                lLastBlock = lBlock;
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
                    BuildingDescription.BlockDescription lBDesc = null;
                    for (BuildingDescription.BlockDescription lBD : lDesc.blocks) {
                        if (lBD.detectSensible) {
                            lBDesc = lBD;
                            break;
                        }
                    }
                    if (lBDesc != null) {
                        BlockPosition lPos = new BlockPosition(lBlock.getLocation());
                        debugDesc(lDones, lPos, lBDesc, lWorld);
                    } else {
                        lPlayer.sendMessage("'" + lName + "' has no detect seinsible block!");
                    }
                } else {
                    lPlayer.sendMessage("'" + lName + "' desc unkown");
                }
            }
        }
    }
    
    protected void debugDesc(ArrayList<BuildingDescription.BlockDescription> aDones, BlockPosition aPos, BuildingDescription.BlockDescription aBDesc, World aWorld) {
        if (!aDones.contains(aBDesc)) {
            aDones.add(aBDesc);
            BlockState lState = aPos.getBlock(aWorld).getState();
            lState.setType(aBDesc.materials.get(0).material);
            if (aBDesc.materials.get(0).withData) {
                lState.setRawData(aBDesc.materials.get(0).data);
            }
            lState.update(true);
            for(BuildingDescription.RelatedTo lRel : aBDesc.relatedTo) {
                BlockPosition lRelPos;
                switch(lRel.position) {
                    case Vector:
                        lRelPos = aPos.clone();
                        lRelPos.add(lRel.direction);
                        if (!lRel.materials.isEmpty()) {
                            for(BlockPosition lPos : new WorldLineWalk(aPos, lRelPos)) {
                                if (!lPos.equals(aPos) && !lPos.equals(lRelPos)) {
                                    lState = lPos.getBlock(aWorld).getState();
                                    lState.setType(lRel.materials.get(0).material);
                                    if (lRel.materials.get(0).withData) {
                                        lState.setRawData(lRel.materials.get(0).data);
                                    }
                                    lState.update(true);
                                }
                            }
                        }
                        debugDesc(aDones, lRelPos, lRel.description, aWorld);
                        break;
                    case AreaXZ:
                    case AreaYX:
                    case AreaYZ:
                        lRelPos = aPos.clone();
                        lRelPos.add(lRel.direction);
                        if (!lRel.materials.isEmpty()) {
                            BlockPosition lPos1 = aPos.clone();
                            BlockPosition lPos2 = lRelPos.clone();
                            int lCount = 0;
                            switch (lRel.position) {
                                case AreaYX:
                                    lCount = lPos2.z - lPos1.z;
                                    lPos2.z = lPos1.z;
                                    break;
                                case AreaYZ:
                                    lCount = lPos2.x - lPos1.x;
                                    lPos2.x = lPos1.x;
                                    break;
                                case AreaXZ:
                                    lCount = lPos2.y - lPos1.y;
                                    lPos2.y = lPos1.y;
                                    break;
                            }
                            for(int lStep = 0; lStep <= Math.abs(lCount); lStep++) {
                                for(BlockPosition lPos : new WorldLineWalk(lPos1, lPos2)) {
                                    if (!lPos.equals(aPos) && !lPos.equals(lRelPos)) {
                                        lState = lPos.getBlock(aWorld).getState();
                                        lState.setType(lRel.materials.get(0).material);
                                        if (lRel.materials.get(0).withData) {
                                            lState.setRawData(lRel.materials.get(0).data);
                                        }
                                        lState.update(true);
                                    }
                                }
                                switch (lRel.position) {
                                    case AreaYX:
                                        lPos1.z += lCount >= 0 ? 1 : -1;
                                        lPos2.z += lCount >= 0 ? 1 : -1;
                                        break;
                                    case AreaYZ:
                                        lPos1.x += lCount >= 0 ? 1 : -1;
                                        lPos2.x += lCount >= 0 ? 1 : -1;
                                        break;
                                    case AreaXZ:
                                        lPos1.y += lCount >= 0 ? 1 : -1;
                                        lPos2.y += lCount >= 0 ? 1 : -1;
                                        break;
                                }
                            }
                        }
                        debugDesc(aDones, lRelPos, lRel.description, aWorld);
                        break;
                    case Nearby:
                        lRelPos = aPos.clone();
                        lRelPos.add(lRel.direction);
                        debugDesc(aDones, lRelPos, lRel.description, aWorld);
                        break;
                }
            }
        }
    }
}
