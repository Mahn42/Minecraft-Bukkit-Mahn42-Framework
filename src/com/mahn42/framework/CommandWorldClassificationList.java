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
        aCommandSender.sendMessage("NAME                |TYPE |ENV  |MODE |STR|ANI|MON|ENC|INVENTORY           |N#|C#");
        for(WorldClassification lWC : lList) {
            aCommandSender.sendMessage(String.format(
                    "%1$20s|%5$5s|%6$5s|%7$5s| %2$1s | %3$1s | %4$1s | %8$1s |%11$20s|%9$d|%10$d",
                    (lWC.name + "                    ").substring(0, 20),
                    lWC.generateStructures?"X":"-",
                    lWC.spawnAnimals?"X":"-",
                    lWC.spawnMonsters?"X":"-",
                    (lWC.type.toString() + "     ").substring(0, 5),
                    (lWC.environment.toString() + "     ").substring(0, 5),
                    (lWC.gameMode.toString() + "     ").substring(0, 5),
                    lWC.entitySpawnCheck?"X":"-",
                    lWC.naturalEntityTypes.size(),
                    lWC.customEntityTypes.size(),
                    (lWC.inventoryName + "                    ").substring(0, 20)
                    )
                    );
/*            aCommandSender.sendMessage(
                    Framework.plugin.getText(
                    aCommandSender,
                    "%1$s %5$s %6$s %7$s structures=%2$b animals=%3$b monster=%4$b entityC=%8$b %9$d %10$d",
                    lWC.name, lWC.generateStructures, lWC.spawnAnimals, lWC.spawnMonsters, lWC.type, lWC.environment, lWC.gameMode, lWC.entitySpawnCheck, lWC.naturalEntityTypes.size(), lWC.customEntityTypes.size()));
                    * */
        }
        return true;
    }
    
}
