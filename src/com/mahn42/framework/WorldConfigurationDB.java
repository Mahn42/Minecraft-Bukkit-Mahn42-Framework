/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;

/**
 *
 * @author andre
 */
public class WorldConfigurationDB extends DBSet<WorldConfiguration> {
    public WorldConfigurationDB(File aFile) {
        super(WorldConfiguration.class, aFile);
    }
    
    public WorldConfigurationDB() {
        super(WorldConfiguration.class, new File(Framework.plugin.getDataFolder().getPath() + File.separatorChar + "worlds.csv"));
    }

    public WorldConfiguration getByName(String aName) {
        for(WorldConfiguration lConf : this) {
            if(lConf.name.equals(aName)) {
                return lConf;
            }
        }
        return null;
    }
}
