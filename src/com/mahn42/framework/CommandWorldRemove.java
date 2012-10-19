/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandWorldRemove implements CommandExecutor{

    //fw_world_remove <worldname>
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            WorldConfigurationDB lDB = Framework.plugin.getWorldConfigurationDB();
            WorldConfiguration lConf =  lDB.getByName(aStrings[0]);
            if (lConf == null) {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cWorld %s not found.", aStrings[0]));
                return true;
            }
            Framework.plugin.getWorldConfigurationDB().remove(lConf);
            WorldPlayerSettingsDB lPSetDB = Framework.plugin.getWorldPlayerSettingsDB(lConf.name);
            ArrayList<WorldPlayerSettings> lSets = new ArrayList<WorldPlayerSettings>();
            for(WorldPlayerSettings lSet : lPSetDB) {
                lSets.add(lSet);
            }
            for(WorldPlayerSettings lSet : lSets) {
                lPSetDB.remove(lSet);
            }
            World lWorld = Framework.plugin.getServer().getWorld(lConf.name);
            if (lWorld != null) {
                Framework.plugin.getServer().unloadWorld(lWorld, true);
            }
            aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aWorld %s removed.", lConf.name));
        }
        return true;
    }
    
}
