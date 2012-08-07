/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.List;

/**
 *
 * @author andre
 */
public interface PlayerManager {
    
    public interface SocialPoint {
        public String getName();
        public int getAmount();
    }
    
    public interface SocialPointHistory {
        public String getName();
        public int getAmount();
        public String getReason();
        public String getChargePlayerName();
    }
    
    public void increaseSocialPoint(String aPlayerName, String aName, int aAmount, String aReason, String aChargePlayerName);
    public List<SocialPoint> getSocialPoints(String aPlayerName);
    public SocialPoint getSocialPoint(String aPlayerName, String aName);
    public List<SocialPointHistory> getSocialPointHistory(String aPlayerName, String aName);
    
}
