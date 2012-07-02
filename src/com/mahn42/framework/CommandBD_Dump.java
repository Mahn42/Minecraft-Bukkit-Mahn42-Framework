/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandBD_Dump implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            BuildingDescription lBuildingDescription = Framework.plugin.getBuildingDescription(aStrings[0]);
            lBuildingDescription.dump(Framework.plugin.getLogger());
        }
        return true;
    }
    
}
