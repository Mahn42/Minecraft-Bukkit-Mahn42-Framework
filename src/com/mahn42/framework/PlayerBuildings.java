/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class PlayerBuildings {
    public String playerName;
    public BlockPosition playerPos;
    public ArrayList<Building> inBuildings = new ArrayList<Building>();
    
    public PlayerBuildings(String aPlayerName) {
        playerName = aPlayerName;
    }
}
