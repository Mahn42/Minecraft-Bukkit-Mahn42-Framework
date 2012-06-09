/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.Iterator;

/**
 *
 * @author andre
 */
public class BlockPositionWalkAround implements Iterable<BlockPosition> {

    protected BlockPosition fPos = null;
    protected BlockPositionDelta[] fDeltas = null;
    
    public BlockPositionWalkAround (BlockPosition aPos, BlockPositionDelta[] aDeltas) {
        fPos = aPos;
        fDeltas = aDeltas;
    }

    protected class WalkAround implements Iterator<BlockPosition> {

        protected int fIndex = 0;
        protected BlockPosition fPos = null;
        protected BlockPositionDelta[] fDeltas = null;
        
        public WalkAround(BlockPosition aPos, BlockPositionDelta[] aDeltas) {
            fPos = aPos;
            fDeltas = aDeltas;
        }
        
        @Override
        public boolean hasNext() {
            return fIndex < fDeltas.length;
        }

        @Override
        public BlockPosition next() {
            BlockPosition lPos = fPos.clone();
            lPos.add(fDeltas[fIndex]);
            fIndex++;
            return lPos;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    @Override
    public Iterator<BlockPosition> iterator() {
        return new WalkAround(fPos, fDeltas);
    }
    
}
