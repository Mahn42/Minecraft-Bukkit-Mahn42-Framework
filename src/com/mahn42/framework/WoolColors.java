/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author andre
 */
public class WoolColors {
    public final static byte white = 0;
    public final static byte orange = 1;
    public final static byte magenta = 2;
    public final static byte light_blue = 3;
    public final static byte yellow = 4;
    public final static byte lime = 5;
    public final static byte pink = 6;
    public final static byte gray = 7;
    public final static byte light_gray = 8;
    public final static byte cyan = 9;
    public final static byte purple = 10;
    public final static byte blue = 11;
    public final static byte brown = 12;
    public final static byte green = 13;
    public final static byte red = 14;
    public final static byte black = 15;
    
    public static HashMap<String, Byte> strings = new HashMap<String, Byte>();
    
    {
        strings.put("White", white);
        strings.put("Orange", orange);
        strings.put("Magenta", magenta);
        strings.put("Light_blue", light_blue);
        strings.put("Yellow", yellow);
        strings.put("Lime", lime);
        strings.put("Pink", pink);
        strings.put("Gray", gray);
        strings.put("Light_gray", light_gray);
        strings.put("Cyan", cyan);
        strings.put("Purple", purple);
        strings.put("Blue", blue);
        strings.put("Brown", brown);
        strings.put("Green", green);
        strings.put("Red", red);
        strings.put("Black", black);
    }
    
    public String toString(byte aColor) {
        for(Entry<String, Byte> lEntry : strings.entrySet()) {
            if (lEntry.getValue() == aColor) {
                return lEntry.getKey();
            }
        }
        return null;
    }

    public byte fromString(String aColor) {
        return strings.get(aColor);
    }

    public byte parseString(String aColor, byte aDefault) {
        for(Entry<String, Byte> lEntry : strings.entrySet()) {
            if (lEntry.getKey().equalsIgnoreCase(aColor)) {
                return lEntry.getValue();
            }
        }
        return aDefault;
    }
}
