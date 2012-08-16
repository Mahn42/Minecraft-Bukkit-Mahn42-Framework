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
    public boolean sendPlayerMessage(String aFromPlayer, String aToPlayerName, String aMessage);
    public boolean sendGroupMessage(String aFromPlayer, String aToGroupName, String aMessage);
    public boolean recallPlayerMessages(String aFromPlayer);
    public boolean recallPlayerMessages(String aFromPlayer, String aToPlayerName);
    public boolean recallGroupMessages(String aFromPlayer, String aToGroupName);
}
