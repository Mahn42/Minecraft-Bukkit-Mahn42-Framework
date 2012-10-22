/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandWorldRegenerate implements CommandExecutor{

    //fw_world_regen [<x> <z> <radius> <worldname>] | [<radius> [<worldname>]]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        int lRad = 100;
        BlockPosition lPos = null;
        World lWorld = null;
        if (aCommandSender instanceof Player) {
            lWorld = ((Player)aCommandSender).getWorld();
            lPos = new BlockPosition(((Player)aCommandSender).getLocation());
            if (aStrings.length > 0) {
                lRad = Integer.parseInt(aStrings[0]);
                if (aStrings.length > 1) {
                    lWorld = Framework.plugin.getServer().getWorld(aStrings[1]);
                }
            }
        } else {
            if (aStrings.length > 3) {
                lPos = new BlockPosition(Integer.parseInt(aStrings[0]), 64, Integer.parseInt(aStrings[2]));
                lRad = Integer.parseInt(aStrings[2]);
                lWorld = Framework.plugin.getServer().getWorld(aStrings[3]);
            } else {
                aCommandSender.sendMessage("position, radius and world must be given!");
                return true;
            }
        }
        if (lWorld != null) {
            for(int x=-lRad;x<=lRad;x++) {
                for(int z=-lRad;z<=lRad;z++) {
                    lWorld.regenerateChunk(lPos.x + x, lPos.z + z);
                }
            }
        } else {
            aCommandSender.sendMessage("no given world!");
        }
        return true;
    }    
}
