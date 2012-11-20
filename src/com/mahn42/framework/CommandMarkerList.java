/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.List;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandMarkerList implements CommandExecutor {
    
    // fw_marker_list <name> [<worldname>]
    // fw_marker_list <size> [<worldname>]
    // fw_marker_list <from> - <to> [<worldname>]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        World lWorld = null;
        BlockPosition lPos = null;
        if (aCommandSender instanceof Player) {
            lWorld = ((Player)aCommandSender).getWorld();
            lPos = new BlockPosition(((Player)aCommandSender).getLocation());
        }
        if (aStrings.length > 0) {
            String lName = null;
            BlockRect lArea = null;
            if (aStrings.length > 2) {
                if (aStrings.length > 3) {
                    lWorld = Framework.plugin.getServer().getWorld(aStrings[3]);
                }
                lArea = new BlockRect();
                lArea.fromCSV(aStrings[0] + " - " + aStrings[2], ",");
            } else if (aStrings.length == 2) {
                lWorld = Framework.plugin.getServer().getWorld(aStrings[1]);
                if (lPos != null) {
                    int lSize;
                    try {
                        lSize = Integer.parseInt(aStrings[0]);
                        lArea = new BlockRect(lPos, lPos);
                        lArea.edge1.add(lSize, lSize, lSize);
                        lArea.edge2.add(-lSize, -lSize, -lSize);
                    } catch(Exception e) {
                        lName = aStrings[0];
                    }
                } else {
                    lName = aStrings[0];
                }
                
            } else if (aStrings.length == 1) {
                if (lPos != null) {
                    int lSize;
                    try {
                        lSize = Integer.parseInt(aStrings[0]);
                        lArea = new BlockRect(lPos, lPos);
                        lArea.edge1.add(lSize, lSize, lSize);
                        lArea.edge2.add(-lSize, -lSize, -lSize);
                    } catch(Exception e) {
                        lName = aStrings[0];
                    }
                } else {
                    lName = aStrings[0];
                }
            }
            List<IMarker> lMarkers;
            if (lName != null) {
                lMarkers = Framework.plugin.findMarkers(lWorld, lName);
            } else {
                lMarkers = Framework.plugin.findMarkers(lWorld, lArea);
            }
            if (lMarkers.isEmpty()) {
                aCommandSender.sendMessage("no markers found!");
            } else {
                for(IMarker lMarker : lMarkers) {
                    aCommandSender.sendMessage(lMarker.getName() + " " + lMarker.getPosition());
                }
            }
        }
        return true;
    }
    
}
