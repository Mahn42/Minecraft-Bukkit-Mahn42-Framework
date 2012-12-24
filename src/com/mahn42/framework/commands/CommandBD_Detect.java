/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandBD_Detect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            String lName = aStrings[0];
            if (aCommandSender instanceof Player) {
                Player lPlayer = (Player) aCommandSender;
                World lWorld = lPlayer.getWorld();
                List<BuildingDescription> lDescs = Framework.plugin.getBuildingDescriptionByMatch(lName);
                for(BuildingDescription lDesc : lDescs) {
                    Block lBlock = lPlayer.getTargetBlock(null, 100);
                    Building lBuilding = lDesc.matchDescription(lWorld, null, lBlock.getX(), lBlock.getY(), lBlock.getZ());
                    if (lBuilding != null) {
                        lPlayer.sendMessage("match ok with " + lDesc.name + ".");
                    } else {
                        lPlayer.sendMessage("no match with " + lDesc.name + ".");
                    }
                }
            }
        }
        return true;
    }

}
