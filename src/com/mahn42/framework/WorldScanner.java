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
        ArrayList<BlockPosition> lRes = new ArrayList<BlockPosition>();
        BlockPosition lPos = aPos;
        for (int x = -aRadius; x <= aRadius; x++) {
            for (int y = -aRadius; y <= aRadius; y++) {
                for (int z = -aRadius; z <= aRadius; z++) {
                    if (lPos.getBlockAt(aWorld, x, y, z).getType().equals(aMaterial)) {
                        BlockPosition lP = lPos.clone();
                        lP.add(x, y, z);
                        lRes.add(lP);
                    }
                }
            }
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
        ArrayList<BlockPosition> lRes2 = new ArrayList<BlockPosition>();
        for (int i = 1; i <= 3; i++) {
            lRes2.clear();
            for (BlockPosition lP : lRes) {
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        BlockPosition lPP = lP.clone();
                        lPP.add(x, 0, z);
                        lBlock = lPP.getBlock(aWorld);
                        if (lBlock.getType().equals(Material.LEAVES)
                                && (lBlock.getData() & 0x4) == 0) {
                            lCountLeaves++;
                        } else if (lBlock.getType().equals(Material.LOG)) {
                            lRes2.add(lPP);
                        }
                    }
                }
            }
            for(BlockPosition lP : lRes2) {
                if (!lRes.contains(lP)) {
                    lRes.add(lP);
                }
            }
        }
        if (lCountLeaves < 3) {
            lRes.clear();
        }
        return lRes;
    }
}