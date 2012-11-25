/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.Framework;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandSetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player) aCommandSender;
            Location lLoc = lPlayer.getLocation();
            Location lSpawn = lPlayer.getWorld().getSpawnLocation();
            if (lPlayer.getWorld().setSpawnLocation(lLoc.getBlockX(),lLoc.getBlockY(),lLoc.getBlockZ())) {
                lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "Spawn point was %s.", new BlockPosition(lSpawn)));
                lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "Spawn point is now set to %s.", new BlockPosition(lLoc)));
            } else {
                lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "Could not set. Spawn point is %s.", new BlockPosition(lSpawn)));
            }
        }
        return true;
    }
    
}
