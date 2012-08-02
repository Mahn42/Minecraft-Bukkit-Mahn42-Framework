/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

/**
 *
 * @author andre
 */
public interface Messenger {
    public void sendPlayerMessage(String aFromPlayer, String aToPlayerName, String aMessage);
    public void sendGroupMessage(String aFromPlayer, String aToGroupName, String aMessage);
    public void recallPlayerMessages(String aFromPlayer, String aToPlayerName);
    public void recallGroupMessages(String aFromPlayer, String aToGroupName);
}
