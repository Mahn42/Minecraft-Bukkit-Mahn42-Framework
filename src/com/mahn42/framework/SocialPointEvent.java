/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

/**
 *
 * @author andre
 */
public class SocialPointEvent extends PlayerManagerEvent {
    
    private PlayerManager.SocialPoint socialPoint;
    private PlayerManager.SocialPointHistory socialPointHistory;
    
    public SocialPointEvent(PlayerManager aPlayerManager, PlayerManager.SocialPoint aSocialPoint, PlayerManager.SocialPointHistory aHistory) {
        super(aPlayerManager);
        socialPoint = aSocialPoint; 
        socialPointHistory = aHistory;
    }
    
    public PlayerManager.SocialPoint getSocialPoint() {
        return socialPoint;
    }

    public PlayerManager.SocialPointHistory getSocialPointHistory() {
        return socialPointHistory;
    }
}
