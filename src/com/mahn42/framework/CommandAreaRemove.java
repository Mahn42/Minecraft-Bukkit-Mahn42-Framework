/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandAreaRemove implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 1) {
            File lFile = new File(aStrings[0]);
            if (lFile.exists()) {
                BlockAreaList lAreaList = new BlockAreaList();
                lAreaList.load(lFile);
                int lIndex = Integer.parseInt(aStrings[1]);
                Framework.plugin.getLogger().info("remove part " + lIndex + " of area file " + aStrings[0] + "!");
                lAreaList.remove(lIndex);
                lAreaList.save(lFile);
            }
        }
        return true;
    }
    
}
