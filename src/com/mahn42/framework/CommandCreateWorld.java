/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Nils
 */
public class CommandCreateWorld implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 1) {
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
        }
        return true;
    }

}
