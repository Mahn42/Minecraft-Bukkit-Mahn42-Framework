/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldConfiguration;
import com.mahn42.framework.WorldConfigurationDB;
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
        aCommandSender.sendMessage("NAME                |TYPE |ENV  |MODE |STR|ANI|MON|ENC|INVENTORY           |N#|C#");
        for(WorldConfiguration lConf : lDB) {
            aCommandSender.sendMessage(String.format(
                    "%1$20s|%5$5s|%6$5s|%7$5s| %2$1s | %3$1s | %4$1s | %8$1s |%11$10s|%9$d|%10$d",
                    (lConf.name + "                    ").substring(0, 20),
                    lConf.generateStructures?"X":"-",
                    lConf.spawnAnimals?"X":"-",
                    lConf.spawnMonsters?"X":"-",
                    (lConf.type.toString() + "     ").substring(0, 5),
                    (lConf.environment.toString() + "     ").substring(0, 5),
                    (lConf.gameMode.toString() + "     ").substring(0, 5),
                    lConf.entitySpawnCheck?"X":"-",
                    lConf.naturalEntityTypes.size(),
                    lConf.customEntityTypes.size(),
                    (lConf.inventoryName + "                    ").substring(0, 20)
                    )
                    );
        }
        return true;
    }
    
}
