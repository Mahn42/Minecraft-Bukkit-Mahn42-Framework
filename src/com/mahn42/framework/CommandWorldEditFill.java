/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandWorldEditFill implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player && aStrings.length > 0) {
            Player lPlayer = (Player) aCommandSender;
            World lWorld = lPlayer.getWorld();
            SyncBlockList lList = new SyncBlockList(lWorld);
            BlockPosition lEdge1 = Framework.plugin.getPositionMarker("1");
            BlockPosition lEdge2 = Framework.plugin.getPositionMarker("2");
            BlockArea lArea = new BlockArea(lWorld, lEdge1, lEdge2);
            byte lData = 0;
            if (aStrings.length > 1) {
                lData = Byte.parseByte(aStrings[1]);
            }
            Material lMat;
            lMat = Material.getMaterial(aStrings[0]);
            if (lMat == null) {
                lMat = Material.getMaterial(Integer.parseInt(aStrings[0]));
            }
            lArea.clear(lMat, lData);
            lArea.toList(lList, lEdge1.getMinPos(lEdge2));
            lPlayer.sendMessage("region " + lEdge1 + " - " + lEdge2 + " is filled with " + lMat + " data " + lData);
            lList.execute();
        }
        return true;
    }
}
