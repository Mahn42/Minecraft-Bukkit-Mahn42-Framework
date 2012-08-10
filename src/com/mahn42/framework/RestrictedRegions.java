/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class RestrictedRegions extends ArrayList<RestrictedRegion> {
    public boolean allowed(Block aBlock, String aPlayerName) {
        return allowed(new BlockPosition(aBlock.getLocation()), aBlock.getType(), aPlayerName);
    }

    public boolean allowed(BlockPosition aPos, Material aMaterial, String aPlayerName) {
        for(RestrictedRegion lRegion : this) {
            if (!lRegion.allowed(aPos, aMaterial, aPlayerName)) {
                return false;
            }
        }
        return true;
    }
    public RestrictedRegion get(BlockPosition aLowerEdge, BlockPosition aUpperEdge) {
        for(RestrictedRegion lRegion : this) {
            if (lRegion.lowerEdge.equals(aLowerEdge) && lRegion.upperEdge.equals(aUpperEdge)) {
                return lRegion;
            }
        }
        return null;
    }
    public RestrictedRegions get(BlockPosition aPos) {
        RestrictedRegions lRegions = new RestrictedRegions();
        for(RestrictedRegion lRegion : this) {
            if (lRegion.contains(aPos)) {
                lRegions.add(lRegion);
            }
        }
        return lRegions;
    }
}
