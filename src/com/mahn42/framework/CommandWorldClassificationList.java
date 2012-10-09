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
public class CommandWorldClassificationList implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        ArrayList<WorldClassification> lList = Framework.plugin.getWorldClassifications();
        for(WorldClassification lWC : lList) {
            aCommandSender.sendMessage(
                    Framework.plugin.getText(
                    aCommandSender,
                    "%1$s %5$s %6$s %7$s structures=%2$b animals=%3$b monster=%4$b entityC=%8$b %9$d %10$d",
                    lWC.name, lWC.generateStructures, lWC.spawnAnimals, lWC.spawnMonsters, lWC.type, lWC.environment, lWC.gameMode, lWC.entitySpawnCheck, lWC.naturalEntityTypes.size(), lWC.customEntityTypes.size()));
        }
        return true;
    }
    
}
