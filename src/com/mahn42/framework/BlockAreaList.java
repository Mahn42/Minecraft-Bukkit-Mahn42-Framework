/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class BlockAreaList extends ArrayList<BlockArea> {
    
    public void load(File aFile) {
        clear();
        if (aFile.exists()) {
            try {
                BufferedReader lReader = new BufferedReader(new FileReader(aFile));
                String lLine;
                int lLineNum = 1;
                String lHeader = lReader.readLine();
                while ((lLine = lReader.readLine()) != null) {
                    BlockArea lRecord = new BlockArea();
                    boolean lOK = false;
                    try {
                        lRecord.fromFileString(lLine);
                        lOK = true;
                    } catch (Exception ex) {
                        Logger.getLogger(DBSet.class.getName()).log(Level.SEVERE, "line " + lLineNum, ex);
                    }
                    lLineNum++;
                    if (lOK) {
                        add(lRecord);
                    }
                }
            } catch (IOException ex) {
                Framework.plugin.getLogger().log(Level.SEVERE, null, ex);
            }            
        }
    }
    
    public void save(File aFile) {
        if (aFile.exists()) {
            aFile.delete();
        }
        try {
            BufferedWriter lWriter = new BufferedWriter(new FileWriter(aFile));
            lWriter.write("BLOCKAREA:1.0");
            lWriter.newLine();
            for (BlockArea lRecord : this) {
                lWriter.write(lRecord.toFileString());
                lWriter.newLine();
            }
            lWriter.close();
        } catch (IOException ex) {
            Framework.plugin.getLogger().log(Level.SEVERE, null, ex);
        }
    }
    
    public void addFromWorld(World aWorld, BlockPosition aEdge1, BlockPosition aEdge2) {
        int lWidth = Math.abs(aEdge1.x - aEdge2.x) + 1;
        int lHeight = Math.abs(aEdge1.y - aEdge2.y) + 1;
        int lDepth = Math.abs(aEdge1.z - aEdge2.z) + 1;
        BlockArea aArea = new BlockArea(lWidth, lHeight, lDepth);
        BlockPosition lPos = new BlockPosition(Math.min(aEdge1.x, aEdge2.x), Math.min(aEdge1.y, aEdge2.y), Math.min(aEdge1.z, aEdge2.x));
        Framework.plugin.getLogger().info("pos = " + lPos);
        aArea.fromWorld(aWorld, lPos);
        add(aArea);
    }
    
    public void toList(int aIndex, SyncBlockList aList, BlockPosition aEdge1) {
        toList(aIndex, aList, aEdge1, false, false, false, false);
    }
    
    public void toList(int aIndex, SyncBlockList aList, BlockPosition aEdge1, boolean aMirrorX, boolean aMirrorZ, boolean aMirrorY, boolean aSwapXZ) {
        get(aIndex).toList(aList, aEdge1, aMirrorX, aMirrorZ, aMirrorY, aSwapXZ);
    }
}
