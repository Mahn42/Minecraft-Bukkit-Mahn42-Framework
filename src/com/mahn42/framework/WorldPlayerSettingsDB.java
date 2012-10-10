/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class WorldPlayerSettingsDB extends DBSetWorld<WorldPlayerSettings> {
    public WorldPlayerSettingsDB() {
        super(WorldPlayerSettings.class);
    }

    public WorldPlayerSettingsDB(File aFile, World aWorld) {
        super(WorldPlayerSettings.class, aFile, aWorld);
    }
    
    public WorldPlayerSettings getByName(String aPlayerName) {
        for(WorldPlayerSettings lSet : this) {
            if (lSet.playerName.equals(aPlayerName)) {
                return lSet;
            }
        }
        return null;
    }

    public WorldPlayerSettings getOrCreateByName(String aPlayerName) {
        WorldPlayerSettings lSet = getByName(aPlayerName);
        if (lSet == null) {
            lSet = new WorldPlayerSettings();
            lSet.playerName = aPlayerName;
            addRecord(lSet);
        }
        return lSet;
    }
}
