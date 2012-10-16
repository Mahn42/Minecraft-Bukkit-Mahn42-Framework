/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Nils
 */
public class CommandWorldCreate implements CommandExecutor{

    //fw_world_create <worldname> [<worldclassname>]
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            WorldConfiguration lConf = new WorldConfiguration();
            lConf.name = aStrings[0];
            if (aStrings.length > 1) {
                WorldClassification lWC = Framework.plugin.getWorldClassification(aStrings[1]);
                if (lWC == null) {
                    aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aClassification %s is not registered.", aStrings[1]));
                    return true;
                }
                lConf.updateFromClassification(lWC);
            }
            Framework.plugin.getWorldConfigurationDB().addRecord(lConf);
            World lWorld = lConf.getCreator().createWorld();
            lWorld.save();
            aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aWorld %s created.", lWorld.getName()));
        }
        return true;
    }

}
