/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
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
                BuildingDescription lDesc = Framework.plugin.getBuildingDescription(lName);
                Block lBlock = lPlayer.getTargetBlock(null, 100);
                Building lBuilding = lDesc.matchDescription(lWorld, lBlock.getX(), lBlock.getY(), lBlock.getZ());
                if (lBuilding != null) {
                    lPlayer.sendMessage("match ok.");
                } else {
                    lPlayer.sendMessage("no match.");
                }
            }
        }
        return true;
    }

}
