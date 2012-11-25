/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.BlockAreaList;
import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandBD_CreateFromArea implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 1) {
            BlockAreaList lList = new BlockAreaList();
            String aFilename = aStrings[1];
            if (!new File(aFilename).exists()) {
                aFilename = aFilename + ".frm";
            }
            if (lList.load(new File(aFilename))) {
                BuildingDescription lDesc = lList.get(0).createDescription(aStrings[0]);
                Framework.plugin.getBuildingDetector().addDescription(lDesc);
            } else {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cFile %s does not exists!", aStrings[0]));
            }
            
        }
        return true;
    }    
}
