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
public class BuildingDB extends DBSetWorld {
    public BuildingDB(World aWorld, File aFile) {
        super(Building.class, aFile, aWorld);
    }
}
