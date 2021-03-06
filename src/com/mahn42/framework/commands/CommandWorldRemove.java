/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldConfiguration;
import com.mahn42.framework.WorldConfigurationDB;
import com.mahn42.framework.WorldPlayerSettings;
import com.mahn42.framework.WorldPlayerSettingsDB;
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
        String worldname = "";
        for (int i = 0; i < aStrings.length; i++) {
            if (worldname.length() == 0)
                worldname = aStrings[i];
            else
                worldname = String.format("%s %s", worldname, aStrings[i]);
        }
        if (worldname.length() > 0) {
            WorldConfigurationDB lDB = Framework.plugin.getWorldConfigurationDB();
            WorldConfiguration lConf =  lDB.getByName(worldname);
            if (lConf == null) {
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&cWorld \"%s\" not found.", worldname));
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
                aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "&aWorld \"%s\" removed.", lConf.name));
            }
        }
        return true;
    }
    
}
