package com.TETOSOFT.audio;

public class AudioManager {
    public enum SoundType {
        BG_MUSIC(0),
        COIN_PICKUP(1),
        STAR_PICKUP(2),
        PLAYER_JUMP(3),
        PLAYER_DEATH(4),
        CREATURE_DEATH(5),
        LEVEL_ADVANCE(6),
        GAME_OVER(7);
        
        public static final int count;
        
        public int getValue() {
            return value;
        }
        
        static {
          count = values().length;
        }
        
        private final int value;
        
        private SoundType(int value) {
            this.value = value;
        }
    };
    
    private final Sound[] sounds;
    
    private AudioManager() {
        sounds = new Sound[SoundType.count];
        sounds[SoundType.BG_MUSIC.getValue()] = new Sound("Background.mp3", -1);
        sounds[SoundType.COIN_PICKUP.getValue()] = new Sound("Coin_Pickup.wav");
        sounds[SoundType.STAR_PICKUP.getValue()] = new Sound("Star_Pickup.wav");
        sounds[SoundType.PLAYER_JUMP.getValue()] = new Sound("Player_Jump.wav");
        sounds[SoundType.PLAYER_DEATH.getValue()] = new Sound("Player_Death.wav");
        sounds[SoundType.CREATURE_DEATH.getValue()] = new Sound("Creature_Death.wav");
        sounds[SoundType.LEVEL_ADVANCE.getValue()] = new Sound("Level_Advance.wav");
        sounds[SoundType.GAME_OVER.getValue()] = new Sound("Game_Over.wav");
    }
    
    private static class SingletonHolder {
        private final static AudioManager instance = new AudioManager();
    }
    
    public static AudioManager getInstance() {
        return SingletonHolder.instance;
    }
    
    public void play(SoundType type) {
        new Thread() {
            @Override
            public void run() {
                Sound selectedSound = sounds[type.getValue()];
                if (selectedSound.isPlaying())
                    return;

                selectedSound.play();

                while (selectedSound.isPlaying())
                    selectedSound.outputSound();

                selectedSound.reload();
            }
        }.start();
    }
    
    public void stop(SoundType type) {
        sounds[type.getValue()].stop();
    }
    
    public void stopAll() {
        for (SoundType type : SoundType.values())
            sounds[type.getValue()].stop();
    }
    
    public void pause(SoundType type) {
        sounds[type.getValue()].pause();
    }
    
    public void resume(SoundType type) {
        sounds[type.getValue()].resume();
    }
    
    public void changePitch(SoundType type, float factor) {
        sounds[type.getValue()].changePitch(factor);
    }
}
