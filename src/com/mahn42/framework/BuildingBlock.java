/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import com.mahn42.framework.BuildingDescription.BlockDescription;

/**
 *
 * @author andre
 */
public class BuildingBlock {
    public BuildingDescription.BlockDescription description;
    public BlockPosition position;

    public BuildingBlock(BlockDescription aBlockDesc, BlockPosition aBlockPosition) {
        description = aBlockDesc;
        position = aBlockPosition;
    }

    public BuildingBlock() {
    }
    
    public String toCSVValue() {
        return description.name + "," + new Integer(position.x) + "," + new Integer(position.y) + "," + new Integer(position.z);
    }
    
    public void fromCSVValue(BuildingDescription aDesc, String aValue) {
        String[] lParts = aValue.split(",");
        String lDescName = lParts[0];
        description = aDesc.getBlock(lDescName);
        position.x = new Integer(lParts[1]).intValue();
        position.y = new Integer(lParts[2]).intValue();
        position.z = new Integer(lParts[3]).intValue();
    }
}
