import com.TETOSOFT.audio.Sound;
import junit.framework.*;

public class AudioTest extends TestCase {
    public void testMP3() throws Exception {
        Sound bg = new Sound("Background-test.mp3");
        assertTrue("MP3 file '" + bg.getName() + "' could not be loaded", bg.play() == true);
        while (bg.isPlaying())
            assertTrue("MP3 file '" + bg.getName() + "' failed to output sound", bg.outputSound() == true);
    }
    
    public void testWAV() throws Exception {
        Sound[] sfx = new Sound[] {
            new Sound("Coin_Pickup.wav", 1),
            new Sound("Star_Pickup.wav", 1),
            new Sound("Player_Jump.wav", 1),
            new Sound("Player_Death.wav", 1),
            new Sound("Creature_Death.wav", 1),
            new Sound("Level_Advance.wav", 1),
            new Sound("Game_Over.wav")
        };
        
        for (Sound s : sfx) {
            assertTrue("WAV file '" + s.getName() + "' could not be loaded", s.play() == true);
            while (s.isPlaying())
                assertTrue("WAV file '" + s.getName() + "' failed to output sound", s.outputSound() == true);
        }
    }
}
