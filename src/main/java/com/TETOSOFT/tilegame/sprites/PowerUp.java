package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.graphics.Animation;
import com.TETOSOFT.graphics.Sprite;
import java.lang.reflect.Constructor;

/**
    A PowerUp instance is a Sprite that the player can pick up.
*/
public abstract class PowerUp extends Sprite {

    public PowerUp(Animation anim) {
        super(anim);
    }

    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Animation)anim.clone()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }

    /**
        A Coin PowerUp. Gives the player coins.
    */
    public static class Coin extends PowerUp {
        public Coin(Animation anim) {
            super(anim);
        }
    }

    /**
        A Star PowerUp. Makes the player temporary invincible.
    */
    public static class Star extends PowerUp {
        public Star(Animation anim) {
            super(anim);
        }
    }

    /**
        A Goal PowerUp. Advances to the next map.
    */
    public static class Goal extends PowerUp {
        public Goal(Animation anim) {
            super(anim);
        }
    }
}
