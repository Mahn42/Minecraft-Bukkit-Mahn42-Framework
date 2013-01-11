/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandBD_List implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        List<BuildingDescription> descriptions = Framework.plugin.getBuildingDetector().getDescriptions();
        for (BuildingDescription lDesc : descriptions) {
            if (aStrings.length == 0 || lDesc.name.matches(aStrings[0])) {
                aCommandSender.sendMessage(lDesc.name + "  " + lDesc.typeName);
            }
        }
        return true;
    }
}
