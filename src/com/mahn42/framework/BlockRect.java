/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

/**
 *
 * @author andre
 */
public class BlockRect {
    BlockPosition edge1 = new BlockPosition();
    BlockPosition edge2 = new BlockPosition();
    
    public BlockRect() {
    }

    public BlockRect(BlockPosition aEdge1, BlockPosition aEdge2) {
        edge1 = aEdge1.getMinPos(aEdge2);
        edge2 = aEdge1.getMaxPos(aEdge2);
    }
    
    public boolean isBetween(BlockPosition aPos) {
        return aPos.x >= edge1.x && aPos.x <= edge2.x
                && aPos.y >= edge1.y && aPos.y <= edge2.y
                && aPos.z >= edge1.z && aPos.z <= edge2.z;
    }
    
    public void cloneFrom(BlockRect aRect) {
        edge1.cloneFrom(aRect.edge1);
        edge2.cloneFrom(aRect.edge2);
    }
    
    public BlockRect clone() {
        BlockRect lRect = new BlockRect();
        lRect.cloneFrom(this);
        return lRect;
    }
    
    public BlockPosition[] getEdges() {
        BlockPosition[] lEdges = new BlockPosition[8];
        lEdges[0] = edge1.clone();
        lEdges[1] = edge2.clone();
        lEdges[2] = new BlockPosition(edge1.x, edge1.y, edge2.z);
        lEdges[3] = new BlockPosition(edge1.x, edge2.y, edge1.z);
        lEdges[4] = new BlockPosition(edge2.x, edge1.y, edge1.z);
        lEdges[5] = new BlockPosition(edge1.x, edge2.y, edge2.z);
        lEdges[6] = new BlockPosition(edge2.x, edge1.y, edge2.z);
        lEdges[7] = new BlockPosition(edge2.x, edge2.y, edge1.z);
        return lEdges;
    }
    
    public boolean isIntersected(BlockRect aRect) {
        BlockPosition[] lEdges = getEdges();
        for(BlockPosition lEdge : lEdges) {
            if (aRect.isBetween(lEdge)) {
                return true;
            }
        }
        lEdges = aRect.getEdges();
        for(BlockPosition lEdge : lEdges) {
            if (isBetween(lEdge)) {
                return true;
            }
        }
        return false;
    }
    
    public void fromCSV(String aText, String aSep) {
        String[] lParts = aText.replaceAll("\\(", "").replaceAll("\\)", "").split(" - ");
        if (lParts.length > 1) {
            edge1.fromCSV(lParts[0], aSep);
            edge2.fromCSV(lParts[1], aSep);
        } else if (lParts.length > 0) {
            edge1.fromCSV(lParts[0], aSep);
            edge2.cloneFrom(edge1);
        }
    }
    
    @Override
    public String toString() {
        return edge1.toString() + " - " + edge2.toString();
    }
    
    @Override
    public boolean equals(Object aObject) {
        if (aObject instanceof BlockRect) {
            return edge1.equals(((BlockRect)aObject).edge1) && edge2.equals(((BlockRect)aObject).edge2);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.edge1 != null ? this.edge1.hashCode() : 0);
        hash = 89 * hash + (this.edge2 != null ? this.edge2.hashCode() : 0);
        return hash;
    }

}
