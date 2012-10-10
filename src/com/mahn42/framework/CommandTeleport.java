/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Nils
 */
public class CommandTeleport implements CommandExecutor {

    //fw_tp [<world>] [<x> <y> <z>]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0 && aCommandSender instanceof Player) {
            Player lPlayer = (Player)aCommandSender;
            World lWorld;
            if (aStrings.length == 1 || aStrings.length == 4) {
                lWorld = Framework.plugin.getServer().getWorld(aStrings[0]);
            } else {
                lWorld = lPlayer.getWorld();
            }
            if (lWorld != null) {
                Location lLocation;
                WorldPlayerSettingsDB lDB = Framework.plugin.getWorldPlayerSettingsDB(lWorld.getName());
                if (lDB != null) {
                    WorldPlayerSettings lSet = lDB.getByName(lPlayer.getName());
                    if (lSet != null) {
                        lLocation = lSet.position.getLocation(lWorld);
                    } else {
                        lLocation = lWorld.getSpawnLocation();
                    }
                } else {
                    lLocation = lWorld.getSpawnLocation();
                }
                if (aStrings.length == 3) {
                    lLocation.setX(Double.parseDouble(aStrings[0]));
                    lLocation.setY(Double.parseDouble(aStrings[1]));
                    lLocation.setZ(Double.parseDouble(aStrings[2]));
                } else if (aStrings.length == 4) {
                    lLocation.setX(Double.parseDouble(aStrings[1]));
                    lLocation.setY(Double.parseDouble(aStrings[2]));
                    lLocation.setZ(Double.parseDouble(aStrings[3]));
                }
                lPlayer.teleport(lLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "you are now in %1$s at %2$s.", lWorld.getName(), new BlockPosition(lLocation)));
            } else {
                
            }
        }
        return true;
    }
}
