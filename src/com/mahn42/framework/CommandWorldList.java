/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author andre
 */
public class CommandWorldList implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        WorldConfigurationDB lDB = Framework.plugin.getWorldConfigurationDB();
        for(WorldConfiguration lConf : lDB) {
            aCommandSender.sendMessage(
                    Framework.plugin.getText(
                    aCommandSender,
                    "%1$s %5$s %6$s %7$s structures=%2$b animals=%3$b monster=%4$b",
                    lConf.name, lConf.generateStructures, lConf.spawnAnimals, lConf.spawnMonsters, lConf.type, lConf.environment, lConf.gameMode));
        }
        return true;
    }
    
}
