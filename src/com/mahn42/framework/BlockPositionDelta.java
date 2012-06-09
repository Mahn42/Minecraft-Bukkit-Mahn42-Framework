/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

/**
 *
 * @author andre
 */
public class BlockPositionDelta {
    
    public int dx, dy, dz;

    public BlockPositionDelta() {
        dx = dy = dz = 0;
    }
    
    public BlockPositionDelta(int aDx, int aDy, int aDz) {
        dx = aDx; dy = aDy; dz = aDz;
    }

    public static final BlockPositionDelta[] HorizontalAndDown = initDeltasHorizontalAndDown();
    public static final BlockPositionDelta[] Horizontal = initDeltasHorizontal();
    public static final BlockPositionDelta[] HorizontalAndUp = initDeltasHorizontalAndUp();
    public static final BlockPositionDelta[] HorizontalAndVertical = initDeltasHorizontalAndVertical();
    public static final BlockPositionDelta[] HorizontalAndVerticalAndDiagonal = initDeltasHorizontalAndVerticalAndDiagonal();
    public static final BlockPositionDelta[] DiagonalHorizontal = initDiagonalHorizontal();

    private static BlockPositionDelta[] initDeltasHorizontalAndDown() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0, 0),
            new BlockPositionDelta( 1, 0, 0),
            new BlockPositionDelta( 0, 0,-1),
            new BlockPositionDelta( 0, 0, 1),
            new BlockPositionDelta( 0,-1, 0)
        };
    }

    private static BlockPositionDelta[] initDeltasHorizontal() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0, 0),
            new BlockPositionDelta( 1, 0, 0),
            new BlockPositionDelta( 0, 0,-1),
            new BlockPositionDelta( 0, 0, 1)
        };
    }

    private static BlockPositionDelta[] initDeltasHorizontalAndUp() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0, 0),
            new BlockPositionDelta( 1, 0, 0),
            new BlockPositionDelta( 0, 0,-1),
            new BlockPositionDelta( 0, 0, 1),
            new BlockPositionDelta( 0, 1, 0)
        };
    }

    private static BlockPositionDelta[] initDeltasHorizontalAndVertical() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0, 0),
            new BlockPositionDelta( 1, 0, 0),
            new BlockPositionDelta( 0, 0,-1),
            new BlockPositionDelta( 0, 0, 1),
            new BlockPositionDelta( 0,-1, 0),
            new BlockPositionDelta( 0, 1, 0)
        };
    }

    private static BlockPositionDelta[] initDeltasHorizontalAndVerticalAndDiagonal() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0, 0),
            new BlockPositionDelta( 1, 0, 0),
            new BlockPositionDelta( 0, 0,-1),
            new BlockPositionDelta( 0, 0, 1),
            new BlockPositionDelta( 0,-1, 0),
            new BlockPositionDelta( 0, 1, 0),
            new BlockPositionDelta(-1, 1, 0),
            new BlockPositionDelta( 1, 1, 0),
            new BlockPositionDelta( 0, 1,-1),
            new BlockPositionDelta( 0, 1, 1),
            new BlockPositionDelta(-1,-1, 0),
            new BlockPositionDelta( 1,-1, 0),
            new BlockPositionDelta( 0,-1,-1),
            new BlockPositionDelta( 0,-1, 1)
        };
    }

    private static BlockPositionDelta[] initDiagonalHorizontal() {
        return new BlockPositionDelta[] {
            new BlockPositionDelta(-1, 0,-1),
            new BlockPositionDelta( 1, 0,-1),
            new BlockPositionDelta(-1, 0, 1),
            new BlockPositionDelta( 1, 0, 1)
        };
    }
}
