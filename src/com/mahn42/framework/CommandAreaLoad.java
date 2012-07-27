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
public class CommandAreaLoad implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player)aCommandSender;
            if (aStrings.length > 0) {
                String aName = aStrings[0];
                BlockAreaList aAreaList = new BlockAreaList();
                aAreaList.load(new File(aName));
                SyncBlockList lList = new SyncBlockList(lPlayer.getWorld());
                int lIndex = 0;
                if (aStrings.length > 1) {
                    lIndex = Integer.parseInt(aStrings[1]);
                }
                aAreaList.toList(lIndex, lList, new BlockPosition(lPlayer.getLocation()));
                lList.execute();
                lPlayer.sendMessage("File " + aName + " loaded.");
            } else {

            }
        } else {
            
        }
        return true;
    }
}
