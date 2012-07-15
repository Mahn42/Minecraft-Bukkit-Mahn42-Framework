/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

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
                lPlayer.sendMessage("Spawn point was " + new BlockPosition(lSpawn));
                lPlayer.sendMessage("Spawn point is now set to " + new BlockPosition(lLoc));
            } else {
                lPlayer.sendMessage("Could not set. Spawn point is " + new BlockPosition(lSpawn));
            }
        }
        return true;
    }
    
}