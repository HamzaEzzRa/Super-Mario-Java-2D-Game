package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.graphics.Animation;

/**
    A trap is an object that kills the player on touch.
*/
public class Trap extends Creature {
    // A trap is not killable by default
    public Trap(Animation left, Animation right,
        Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight, false);
    }
}

