/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandDebugSet implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aStrings.length > 0) {
            if (aStrings[0].equalsIgnoreCase("special")) {
                World lWorld = Framework.plugin.getServer().getWorld(aStrings[1]);
                BlockPosition lEdge1 = new BlockPosition();
                BlockPosition lEdge2 = new BlockPosition();
                if (aStrings.length > 3) {
                    lEdge1.fromCSV(aStrings[2], "\\,");
                    lEdge2.fromCSV(aStrings[3], "\\,");
                } else {
                    int lRad = Integer.parseInt(aStrings[2]);
                    BlockPosition lPos = new BlockPosition(((Player)aCommandSender).getLocation());
                    lEdge1 = lPos.clone();
                    lEdge1.x -= lRad;
                    lEdge1.y = 4;
                    lEdge1.z -= lRad;
                    lEdge2 = lPos.clone();
                    lEdge2.x += lRad;
                    lEdge2.y = 31;
                    lEdge2.z += lRad;
                }
                BlockPosition lWHD = lEdge1.getWHD(lEdge2);
                int lAnz = (int) ((lWHD.x) * (lWHD.z) * 0.0034);
                Random lRnd = new Random();
                for (int lIndex = 0; lIndex < lAnz; lIndex++) {
                    int x = lEdge1.x + lRnd.nextInt(lWHD.x);
                    int y = lEdge1.y + lRnd.nextInt(lWHD.y);
                    int z = lEdge1.z + lRnd.nextInt(lWHD.z);
                    BlockState lState;
                    lState = lWorld.getBlockAt(x, y, z).getState();
                    lState.setType(Material.EMERALD_ORE);
                    lState.update(true);
                    x++;
                    lState = lWorld.getBlockAt(x, y, z).getState();
                    lState.setType(Material.EMERALD_ORE);
                    lState.update(true);
                    y++;
                    lState = lWorld.getBlockAt(x, y, z).getState();
                    lState.setType(Material.EMERALD_ORE);
                    lState.update(true);
                    x--;
                    lState = lWorld.getBlockAt(x, y, z).getState();
                    lState.setType(Material.EMERALD_ORE);
                    lState.update(true);
                    if (lRnd.nextBoolean()) {
                        x--; y--;
                        lState = lWorld.getBlockAt(x, y, z).getState();
                        lState.setType(Material.EMERALD_ORE);
                        lState.update(true);
                    }
                }
                /*
                PerlinNoiseGenerator lGen = new PerlinNoiseGenerator(lWorld);
                double lMin = Double.MAX_VALUE;
                double lMax = Double.MIN_VALUE;
                int lCount = 0;
                for(int x = lEdge1.x; x < lEdge2.x; x++) {
                    for(int z = lEdge1.z; z < lEdge2.z; z++) {
                        Biome lBiome = lWorld.getBiome(x, z);
                        //if (lBiome == Biome.EXTREME_HILLS) {
                            int lMaxY = lWorld.getHighestBlockYAt(x, z);
                            int lLine = 0;
                            if (lMaxY > lEdge2.y || lMaxY <= 0) lMaxY = lEdge2.y;
                            //for(int y = lEdge1.y; y < lEdge2.y && y < lMaxY; y++) {
                                double lNoise = lGen.noise(x, z);
                                if (lNoise < lMin) lMin = lNoise;
                                if (lNoise > lMax) lMax = lNoise;
                                if (lNoise > 0.6) {
                                    lLine = (int) (lNoise*4); lCount += lLine; 
                                    int y = lRnd.nextInt(lMaxY);
                                    for(int lY = y; lY < lLine; lY++) {
                                        lWorld.getBlockAt(x, lY, z).setType(Material.EMERALD_ORE);
                                    }
                                }
                                
                                //print(aCommandSender, "(" + x + "," + y + "," + z + ") " + lNoise);
                            //}
                        //}
                    }
                }
                print(aCommandSender, "min = " + lMin + " max = " + lMax);
                print(aCommandSender, "count = " + lCount);
                */
                print(aCommandSender, "count = " + lAnz);
            } else {
                String lName = aStrings[0];
                if (aStrings.length > 1) {
                    boolean lValue = Boolean.parseBoolean(aStrings[1]);
                    Framework.plugin.setDebugSet(lName, lValue);
                }
                print(aCommandSender, lName + " is set to " + Framework.plugin.isDebugSet(lName) );
            }
        } else {
            print(aCommandSender, aCommand.getName() + " <DebugOption> [true|false]");
        }
        return true;
    }
    
    protected void print(CommandSender aCommandSender, String aText) {
        if (aCommandSender instanceof Player) {
            ((Player)aCommandSender).sendMessage(aText);
        } else {
            Framework.plugin.getLogger().info(aText);
        }
    }
}
