/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandSetPosMarker implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player)aCommandSender;
            if (aStrings.length > 0) {
                String aName = aStrings[0];
                BlockPosition lPos = new BlockPosition(lPlayer.getLocation());
                Framework.plugin.setPositionMarker(aName, lPos);
                lPlayer.sendMessage("mark " + aName + " at " + lPos);
            } else {

            }
        } else {
            
        }
        return true;
    }
}
