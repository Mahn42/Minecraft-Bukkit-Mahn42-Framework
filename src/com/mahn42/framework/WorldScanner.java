/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class WorldScanner {

    public static List<BlockPosition> findBlocks(World aWorld, BlockPosition aPos, Material aMaterial, int aRadius) {
        return findBlocks(aWorld, aPos, aMaterial, aRadius, false);
    }

    public static List<BlockPosition> findBlocks(World aWorld, BlockPosition aPos, Material aMaterial, int aRadius, boolean aSortByDistance) {
        return findBlocks(aWorld, aPos, aMaterial, (byte) 0, false, aRadius, aSortByDistance);
    }

    public static List<BlockPosition> findBlocks(World aWorld, BlockPosition aPos, Material aMaterial, byte aData, boolean aCheckData, int aRadius, boolean aSortByDistance) {
        return findBlocks(aWorld, aPos, aMaterial, aData, (byte)0xFF, aCheckData, aRadius, aSortByDistance);
    }
    
    public static List<BlockPosition> findBlocks(World aWorld, BlockPosition aPos, Material aMaterial, byte aData, byte aMask, boolean aCheckData, int aRadius, boolean aSortByDistance) {
        ArrayList<BlockPosition> lRes;
        ArrayList<BlockPosition> lFounds = new ArrayList<BlockPosition>();
        BlockPosition lPos = aPos;
        for (int x = -aRadius; x <= aRadius; x++) {
            for (int y = -aRadius; y <= aRadius; y++) {
                for (int z = -aRadius; z <= aRadius; z++) {
                    Block blockAt = lPos.getBlockAt(aWorld, x, y, z);
                    if (blockAt.getType().equals(aMaterial)) {
                        if (!aCheckData || (blockAt.getData() & aMask) == aData) {
                            BlockPosition lP = lPos.clone();
                            lP.add(x, y, z);
                            lFounds.add(lP);
                        }
                    }
                }
            }
        }
        if (aSortByDistance) {
            lRes = new ArrayList<BlockPosition>();
            while (!lFounds.isEmpty()) {
                double lDist = Double.MAX_VALUE;
                BlockPosition lPP = null;
                for (BlockPosition lP : lFounds) {
                    double distance = lP.distance(aPos);
                    if (distance < lDist) {
                        lPP = lP;
                    }
                }
                lRes.add(lPP);
                lFounds.remove(lPP);
            }
        } else {
            lRes = lFounds;
        }
        return lRes;
    }

    public static List<BlockPosition> getTreePoss(World aWorld, BlockPosition aPos) {
        ArrayList<BlockPosition> lRes = new ArrayList<BlockPosition>();
        BlockPosition lPos = aPos.clone();
        Block lBlock = lPos.getBlock(aWorld);
        while (lBlock.getType().equals(Material.LOG)) {
            lRes.add(lPos.clone());
            lPos.y--;
            lBlock = lPos.getBlock(aWorld);
        }
        lPos = aPos.clone();
        lPos.y++;
        lBlock = lPos.getBlock(aWorld);
        while (lBlock.getType().equals(Material.LOG)) {
            lRes.add(lPos.clone());
            lPos.y++;
            lBlock = lPos.getBlock(aWorld);
        }
        int lCountLeaves = 0;
        for (BlockPosition lP : lRes) {
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    if (!(x == 0 && z == 0)) {
                        BlockPosition lPP = lP.clone();
                        lPP.add(x, 0, z);
                        lBlock = lPP.getBlock(aWorld);
                        if (lBlock.getType().equals(Material.LEAVES)) {
                            if ((lBlock.getData() & 0x4) == 0) {
                                lCountLeaves++;
                            }
                        }
                    }
                }
            }
        }
        if (lCountLeaves >= lRes.size()) {
            ArrayList<BlockPosition> lRes2 = new ArrayList<BlockPosition>();
            lRes2.addAll(lRes);
            getLogBlocks(aWorld, lRes, lRes2);
        } else {
            lRes.clear();
        }
        return lRes;
    }

    //returns leaves around
    protected static int getLogBlocks(World aWorld, List<BlockPosition> aLogsIn, List<BlockPosition> aLogsNew) {
        int lCountLeaves = 0;
        ArrayList<BlockPosition> lRes = new ArrayList<BlockPosition>();
        for (BlockPosition lP : aLogsNew) {
            for (int x = -3; x <= 3; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -3; z <= 3; z++) {
                        if (!(x == 0 && y == 0 && z == 0)) {
                            BlockPosition lPP = lP.clone();
                            lPP.add(x, y, z);
                            Block lBlock = lPP.getBlock(aWorld);
                            if (lBlock.getType().equals(Material.LEAVES)) {
                                if ((lBlock.getData() & 0x4) == 0) {
                                    lCountLeaves++;
                                }
                            } else if (lBlock.getType().equals(Material.LOG)) {
                                if (!aLogsIn.contains(lPP) && !aLogsNew.contains(lPP) && !lRes.contains(lPP)) {
                                    lRes.add(lPP);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!lRes.isEmpty()) {
            aLogsIn.addAll(lRes);
            lCountLeaves += getLogBlocks(aWorld, aLogsIn, lRes);
        }
        return lCountLeaves;
    }
}
