/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandChunkRegenerate implements CommandExecutor {
    
    // fw_chunk_regenerate [radius]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player player = (Player) aCommandSender;
            int radius = 0;
            if (aStrings.length > 0) {
                radius = Integer.parseInt(aStrings[0]);
            }
            Chunk chunkAt = player.getWorld().getChunkAt(player.getLocation());
            for(int x = -radius;x<=radius;x++) {
                for(int z = -radius;z<=radius;z++) {
                    player.getWorld().regenerateChunk(chunkAt.getX() + x, chunkAt.getZ() + z);
                    player.sendMessage("regenerate chunk " + (chunkAt.getX() + x) + " " + chunkAt.getZ() + z);
                }
            }
        }
        return true;
    }
    
}
