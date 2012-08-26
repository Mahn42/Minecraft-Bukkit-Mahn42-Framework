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
                lReader.close();
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
        add(new BlockArea(aWorld, aEdge1, aEdge2));
    }
    
    public void setFromWorld(int aIndex, World aWorld, BlockPosition aEdge1, BlockPosition aEdge2) {
        this.set(aIndex, new BlockArea(aWorld, aEdge1, aEdge2));
    }
    
    public void toList(int aIndex, SyncBlockList aList, BlockPosition aEdge1) {
        toList(aIndex, aList, aEdge1, false, false, false, false, BlockArea.BlockAreaPlaceMode.full);
    }
    
    public void toListMixed(int aIndex, SyncBlockList aList, BlockPosition aEdge1) {
        toList(aIndex, aList, aEdge1, false, false, false, false, BlockArea.BlockAreaPlaceMode.mixed);
    }
    
    public void toList(int aIndex, SyncBlockList aList, BlockPosition aEdge1, boolean aMirrorX, boolean aMirrorZ, boolean aMirrorY, boolean aSwapXZ, BlockArea.BlockAreaPlaceMode aMode) {
        get(aIndex).toList(aList, aEdge1, aMirrorX, aMirrorZ, aMirrorY, aSwapXZ, aMode);
    }
}
