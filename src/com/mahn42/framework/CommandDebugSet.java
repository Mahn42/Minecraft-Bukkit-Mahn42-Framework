/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandDebugSet implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            String lName = aStrings[0];
            if (aStrings.length > 1) {
                boolean lValue = Boolean.parseBoolean(aStrings[1]);
                Framework.plugin.setDebugSet(lName, lValue);
            }
            print(aCommandSender, lName + " is set to " + Framework.plugin.isDebugSet(lName) );
        } else {
            print(aCommandSender, aCommand.getName() + " <DebugOption> [true|false]");
        }
        return true;
    }
    
    protected void print(CommandSender aCommandSender, String aText) {
        if (aCommandSender instanceof Player) {
            ((Player)aCommandSender).sendMessage(aText);
        } else {
            Framework.plugin.getLogger().info(aText);
        }
    }
}
