/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldLineWalk;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandBD_Create implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            String lName = aStrings[0];
            if (aCommandSender instanceof Player) {
                Player lPlayer = (Player) aCommandSender;
                World lWorld = lPlayer.getWorld();
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
                        BlockPosition lPos = new BlockPosition(lPlayer.getLocation());
                        lPos.add(0, 0, 3);
                        debugDesc(lDones, lPos, lBDesc, lWorld);
                    } else {
                        lPlayer.sendMessage("'" + lName + "' has no detect sensible block!");
                    }
                } else {
                    lPlayer.sendMessage("'" + lName + "' desc unkown");
                }
            }
        }
        return true;
    }
    
    protected void debugDesc(ArrayList<BuildingDescription.BlockDescription> aDones, BlockPosition aPos, BuildingDescription.BlockDescription aBDesc, World aWorld) {
        if (aBDesc == null) {
            Framework.plugin.getLogger().info("no block description!");
            return;
        }
        if (!aDones.contains(aBDesc)) {
            aDones.add(aBDesc);
            if (aBDesc.materials.isEmpty()) {
                Framework.plugin.getLogger().info("Block " + aBDesc.name + " has no materiual!");
                return;
            }
            BlockState lState = aPos.getBlock(aWorld).getState();
            if (lState == null) {
                Framework.plugin.getLogger().info("Block " + aBDesc.name + " no BlockState! " + aPos);
                return;
            }
            if (aBDesc.materials.get(0).entityType != EntityType.UNKNOWN) {
                Framework.plugin.getLogger().info("Entity spawn not supported! " + aBDesc.name + " at " + aPos + " " + aBDesc.materials.get(0).entityType);
            } else {
                lState.setType(aBDesc.materials.get(0).material);
                if (aBDesc.materials.get(0).withData) {
                    lState.setRawData(aBDesc.materials.get(0).data);
                }
            }
            try {
                lState.update(true);
            } catch (Exception ex) {
                Framework.plugin.getLogger().throwing(getClass().getName(), null, ex);
            }
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
