package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.audio.AudioManager;

import com.TETOSOFT.graphics.Animation;

/**
    The Player.
*/
public class Player extends Creature {
    private static final float JUMP_SPEED = -0.95f;

    public static final long STAR_DURATION = 6000;
	
    private boolean onGround;
    
    private boolean isInvincible;
    
    private long invincibleTime = 0;
    
    private long elapsedInvincibleTime = 0;
    
    public Player(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight);
        setInvincible(false);
    }

    public boolean getInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean value) {
        isInvincible = value;
        
        if (value) {
            AudioManager.getInstance().changePitch(AudioManager.SoundType.BG_MUSIC, 0.8f);
            invincibleTime += STAR_DURATION;
        }
        else
            AudioManager.getInstance().changePitch(AudioManager.SoundType.BG_MUSIC, 1);
    }

    public void collideHorizontal() {
        setVelocityX(0);
    }

    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }

    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }

    public void wakeUp() {
        // do nothing
    }

    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            if (onGround)
                AudioManager.getInstance().play(AudioManager.SoundType.PLAYER_JUMP);
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }

    public float getMaxSpeed() {
        return 0.5f;
    }
	
    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);

        if (getInvincible()) {
            elapsedInvincibleTime += elapsedTime;
            if (elapsedInvincibleTime >= invincibleTime) {
                setInvincible(false);
                elapsedInvincibleTime = 0;
            }
        }
    }
}
