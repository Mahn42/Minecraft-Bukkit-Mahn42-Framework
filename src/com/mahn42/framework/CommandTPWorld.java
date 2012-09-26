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
public class CommandTPWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0 && aCommandSender instanceof Player) {
            String lName = "";
            for (String lstr : aStrings) {
                if (lName.length() == 0) {
                    lName = lstr;
                }
                else {
                    lName = lName + " " + lstr;
                }
            }
            World lWorld = Framework.plugin.getServer().getWorld(lName);
            if (lWorld != null) {
                Player lPlayer = (Player)aCommandSender;
                Location lLocation = lWorld.getSpawnLocation();
                lPlayer.teleport(lLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
            else {
                aCommandSender.sendMessage(ChatColor.RED + "World " + lName + " is unknown!");
            }
        }
        return true;
    }
}
