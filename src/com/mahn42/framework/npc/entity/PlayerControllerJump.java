/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

/**
 *
 * @author andre
 */
public class PlayerControllerJump {
    private final EntityPlayerNPC a;
    private boolean b;

    public PlayerControllerJump(EntityPlayerNPC entityinsentient) {
        this.a = entityinsentient;
    }

    public void a() {
        this.b = true;
    }

    public void b() {
        this.a.f(this.b);
        this.b = false;
    }
}