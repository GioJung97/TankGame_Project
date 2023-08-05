package tankrotationexample.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.plaf.metal.MetalIconFactory;

public class Sound {
    private Clip sound;

    public Sound(Clip c){
        this.sound = c;
    }

    public void stopSound(){
        if(this.sound.isRunning()){
            this.stopSound();
        }
    }

    public void playSound(){
        this.sound.setFramePosition(0);
        this.sound.start();
    }

    public void setLooping(){
        this.sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float level){
        FloatControl volume = (FloatControl)  this.sound.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(20.0f * (float)Math.log10(level));
    }
}
