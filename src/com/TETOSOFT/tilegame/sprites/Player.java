package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.audio.AudioManager;

import com.TETOSOFT.graphics.Animation;
import java.lang.reflect.Constructor;

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
    
    private Animation[] idleAnimations = new Animation[4];
    private Animation[] idleRageAnimations = new Animation[4];
    private Animation[] walkingAnimations = new Animation[4];
    private Animation[] walkingRageAnimations = new Animation[4];
    
    // Adding acceleration & friction for a smoother mouvement
    private float acceleration = 0f;
    private static final float FRICTION = 0.025f;
    
    public Player(Animation[] idleAnims, Animation[] idleRageAnims,
        Animation[] walkingAnims, Animation[] walkingRageAnims) {
        super(idleAnims[0], idleAnims[1], idleAnims[2], idleAnims[3]);

        System.arraycopy(idleAnims, 0, idleAnimations, 0, 4);
        System.arraycopy(idleRageAnims, 0, idleRageAnimations, 0, 4);
        System.arraycopy(walkingAnims, 0, walkingAnimations, 0, 4);
        System.arraycopy(walkingRageAnims, 0, walkingRageAnimations, 0, 4);
        
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
                AudioManager.getInstance().play(AudioManager.SoundType
                    .PLAYER_JUMP);
            
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
        
        float oldVelocityX = getVelocityX();
        float newVelocityX = oldVelocityX + acceleration * elapsedTime;
        newVelocityX += -Math.signum(newVelocityX) * FRICTION;
        
        setVelocityX(newVelocityX);
        
        if (state != Creature.STATE_DYING && state != Creature.STATE_DEAD) {
            if (getVelocityX() == 0f) {
                if (oldVelocityX != 0f) {
                    if (getInvincible())
                        setActiveAnim(idleRageAnimations, oldVelocityX < 0f ?
                            idleRageAnimations[0] : idleRageAnimations[1]);
                    else
                        setActiveAnim(idleAnimations, oldVelocityX < 0f ?
                            idleAnimations[0] : idleAnimations[1]);
                }
            }
            else {
                if (getInvincible())
                    setActiveAnim(walkingRageAnimations, getVelocityX() < 0f ?
                        walkingRageAnimations[0] : walkingRageAnimations[1]);
                else
                    setActiveAnim(walkingAnimations, getVelocityX() < 0f ?
                        walkingAnimations[0] : walkingAnimations[1]);
            }
        }
    }
    
    @Override
    public void setVelocityX(float dx) {
        // Stop the friction from continuously moving the player back and forth.
        if (Math.abs(dx) < 0.05f) 
            this.dx = 0f;
        else if (Math.abs(dx) < getMaxSpeed()) // Max speed limit.
            this.dx = dx;
        else
            this.dx = Math.signum(dx) * getMaxSpeed();
    }
    
    public void setAcceleration(float dv) {
        this.acceleration = dv;
    }
    
    @Override
    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
            if (state == STATE_DYING) {
                setAcceleration(0);
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }
    
    @Override
    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Animation[])idleAnimations.clone(),
                (Animation[])idleRageAnimations.clone(),
                (Animation[])walkingAnimations.clone(),
                (Animation[])walkingRageAnimations.clone()
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }
    
    private void setActiveAnim(Animation[] anims, Animation target) {
        if (anims == null || anims.length < 4)
            return;
        
        this.left = anims[0];
        this.right = anims[1];
        this.deadLeft = anims[2];
        this.deadRight = anims[3];
        
        this.anim = target;
    }
}
