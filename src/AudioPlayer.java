import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioPlayer
{
    private String soundFolder = "TetrisSounds" + File.separator;
    private String lineClearedPath = soundFolder + "se_clear_tetris.wav";
    private String moveSoundPath = soundFolder + "se_move.wav";
    private String rotateSoundPath = soundFolder + "se_rotate.wav";
    private String loseSoundPath = soundFolder + "se_lose.wav";
    private String hardDropSoundPath = soundFolder + "se_hdrop.wav";
    private Clip lineCleared;
    private Clip moveSound;
    private Clip rotateSound;
    private Clip loseSound;
    private Clip hardDropSound;

    public AudioPlayer() // constructor
    {
        try {
            lineCleared = AudioSystem.getClip();
            moveSound = AudioSystem.getClip();
            rotateSound = AudioSystem.getClip();
            loseSound = AudioSystem.getClip();
            hardDropSound = AudioSystem.getClip();
            lineCleared.open(AudioSystem.getAudioInputStream(new File(lineClearedPath).getAbsoluteFile()));
            moveSound.open(AudioSystem.getAudioInputStream(new File(moveSoundPath).getAbsoluteFile()));
            rotateSound.open(AudioSystem.getAudioInputStream(new File(rotateSoundPath).getAbsoluteFile()));
            loseSound.open(AudioSystem.getAudioInputStream(new File(loseSoundPath).getAbsoluteFile()));
            hardDropSound.open(AudioSystem.getAudioInputStream(new File(hardDropSoundPath).getAbsoluteFile()));
        } catch (LineUnavailableException e) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,e);
        } catch (UnsupportedAudioFileException e) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,e);
        }
        catch (IOException e) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void playClearSound()
    {
        lineCleared.setFramePosition(0);
        lineCleared.start();
    }
    public void playMoveSound()
    {
        moveSound.setFramePosition(0);
        moveSound.start();
    }
    public void playLoseSound()
    {
        loseSound.setFramePosition(0);
        loseSound.start();
    }
    public void playRotateSound()
    {
        rotateSound.setFramePosition(0);
        rotateSound.start();
    }

    public void playHardDropSound()
    {
        hardDropSound.setFramePosition(0);
        hardDropSound.start();
    }
}
