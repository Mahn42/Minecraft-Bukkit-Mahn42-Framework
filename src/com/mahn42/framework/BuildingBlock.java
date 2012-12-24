/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import com.mahn42.framework.BuildingDescription.BlockDescription;
import org.bukkit.Material;

/**
 *
 * @author andre
 */
public class BuildingBlock {
    public BuildingDescription.BlockDescription description;
    public BlockPosition position = new BlockPosition();
    public Material material = null;

    public BuildingBlock(BlockDescription aBlockDesc, BlockPosition aBlockPosition, Material aMaterial) {
        description = aBlockDesc;
        position = aBlockPosition;
        material = aMaterial;
    }

    public BuildingBlock() {
    }
    
    /*
    public String toCSVValue() {
        return description.name + "," + new Integer(position.x) + "," + new Integer(position.y) + "," + new Integer(position.z);
    }
    */
    
    public void toCSVValue(StringBuilder aBuilder) {
        aBuilder.append(description.name);
        aBuilder.append(",");
        aBuilder.append(Integer.toString(position.x));
        aBuilder.append(",");
        aBuilder.append(Integer.toString(position.y));
        aBuilder.append(",");
        aBuilder.append(Integer.toString(position.z));
        aBuilder.append(",");
        if (material != null) {
            aBuilder.append(material.toString());
        }
    }
    
    public void fromCSVValue(BuildingDescription aDesc, String aValue) {
        String[] lParts = aValue.split("\\,");
        String lDescName = lParts[0];
        description = aDesc.getBlock(lDescName);
        position.x = new Integer(lParts[1]).intValue();
        position.y = new Integer(lParts[2]).intValue();
        position.z = new Integer(lParts[3]).intValue();
        if (lParts.length > 4) {
            if (!lParts[4].isEmpty()) {
                material = Material.valueOf(lParts[4]);
            }
        }
    }
    
    @Override
    public String toString() {
        return description.toString() + " at " + position.toString() + (material == null ? "" : " with " + material.toString() );
    }
}
