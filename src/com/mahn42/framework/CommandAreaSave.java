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
                    File lFile = new File(aName);
                    String lExtText = "";
                    if (aStrings.length > 1) {
                        if (lFile.exists()) {
                            lAreaList.load(lFile);
                        }
                        if (aStrings[1].equalsIgnoreCase("add")) {
                            lExtText = " added";
                            lAreaList.addFromWorld(lPlayer.getWorld(), lEdge1, lEdge2);
                        } else {
                            int lIndex = Integer.parseInt(aStrings[1]);
                            if (lIndex < lAreaList.size()) {
                                lExtText = " set at " + lIndex;
                                lAreaList.setFromWorld(lIndex, lPlayer.getWorld(), lEdge1, lEdge2);
                            } else {
                                lExtText = " added";
                                lAreaList.addFromWorld(lPlayer.getWorld(), lEdge1, lEdge2);
                            }
                        }
                    } else {
                        lAreaList.addFromWorld(lPlayer.getWorld(), lEdge1, lEdge2);
                    }
                    lAreaList.save(lFile);
                    lPlayer.sendMessage("area " + lEdge1 + " - " + lEdge2 + " saved to " + aName + lExtText + ".");
                }
            }
        }
        return true;
    }
}
