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
public class RestrictedRegion {
    public BlockPosition lowerEdge;
    public BlockPosition upperEdge;
    public ArrayList<Material> allowedMaterials = new ArrayList<Material>();
    public String playerName = null;
    
    public RestrictedRegion(BlockPosition aLowerEdge, BlockPosition aUpperEdge) {
        lowerEdge = aLowerEdge.clone();
        upperEdge = aUpperEdge.clone();
    }

    public RestrictedRegion(BlockPosition aLowerEdge, BlockPosition aUpperEdge, ArrayList<Material> aAllowedMaterials) {
        lowerEdge = aLowerEdge.clone();
        upperEdge = aUpperEdge.clone();
        allowedMaterials.addAll(aAllowedMaterials);
    }
    
    public boolean allowed(Block aBlock, String aPlayerName) {
        return allowed(new BlockPosition(aBlock.getLocation()), aBlock.getType(), aPlayerName);
    }

    public boolean allowed(BlockPosition aPos, Material aMaterial, String aPlayerName) {
        return allowedMaterials.contains(aMaterial)
                && ((playerName == null ) || (playerName.equals(aPlayerName)))
                && (aPos.x >= lowerEdge.x && aPos.x <= upperEdge.x)
                && (aPos.y >= lowerEdge.y && aPos.y <= upperEdge.y)
                && (aPos.z >= lowerEdge.z && aPos.z <= upperEdge.z);
    }

    public boolean contains(BlockPosition aPos) {
        return (aPos.x >= lowerEdge.x && aPos.x <= upperEdge.x)
                && (aPos.y >= lowerEdge.y && aPos.y <= upperEdge.y)
                && (aPos.z >= lowerEdge.z && aPos.z <= upperEdge.z);
    }
}
