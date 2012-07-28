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
public class CommandAreaDelete implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            File lFile = new File(aStrings[0]);
            if (lFile.exists()) {
                Framework.plugin.getLogger().info("delete area file " + aStrings[0] + "!");
                lFile.delete();
            }
        }
        return true;
    }
    
}
