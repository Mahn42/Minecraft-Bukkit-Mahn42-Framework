/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandAreaSave implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player)aCommandSender;
            if (aStrings.length > 0) {
                String aName = aStrings[0];
                BlockPosition lEdge1 = Framework.plugin.getPositionMarker("1");
                BlockPosition lEdge2 = Framework.plugin.getPositionMarker("2");
                if (lEdge1 != null && lEdge2 != null) {
                    BlockAreaList lAreaList = new BlockAreaList();
                    lAreaList.addFromWorld(lPlayer.getWorld(), lEdge1, lEdge2);
                    lAreaList.save(new File(aName));
                    lPlayer.sendMessage("area " + lEdge1 + " - " + lEdge2 + " saved to " + aName);
                }
            }
        }
        return true;
    }
}
