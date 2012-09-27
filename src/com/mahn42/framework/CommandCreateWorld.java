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
public class CommandCreateWorld implements CommandExecutor{

    //fw_createworld <worldname> <worldconfname> 
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            WorldConfiguration lConf = new WorldConfiguration();
            lConf.name = aStrings[0];
            if (aStrings.length > 1) {
                //TODO get worldconftype
            }
            Framework.plugin.getWorldConfigurationDB().addRecord(lConf);
            World lWorld = lConf.getCreator().createWorld();
            lWorld.save();
            aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aWorld %s created.", lWorld.getName()));
            /*
            Boolean lCreate = false;
            String lType = aStrings[0];
            String lName = aStrings[1];
            WorldCreator lWC = new WorldCreator(lName);
            if (lType.equalsIgnoreCase("flat")) {
                lWC.seed(42);
                lWC.generateStructures(false);
                lWC.type(WorldType.FLAT);
                lCreate = true;
            }
            if (lCreate) {
                World lWorld = lWC.createWorld();
                lWorld.save();
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aWorld %s created.", lWorld.getName()));
            }
            else {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cWorld %s not created.", lName));
            }
            */
        }
        return true;
    }

}
