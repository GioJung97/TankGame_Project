package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
    private float x, y;
    private int currentFrame = 0;
    private final List<BufferedImage> frames;
    private long timeSinceLastUpdate;
    private long delay = 40;
    private boolean isRunning = false;


    public Animation(float x, float y, List<BufferedImage> frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        timeSinceLastUpdate = 0;
        isRunning = true;
        currentFrame = 0;
    }


    public void update(){
        if(this.timeSinceLastUpdate + this.delay < System.currentTimeMillis()){
            this.timeSinceLastUpdate = System.currentTimeMillis();
            this.currentFrame ++;
            this.currentFrame = (this.currentFrame + 1) % this.frames.size();

//            if(this.currentFrame == this.frames.size()){
//                isRunning = false;
//            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void drawImage(Graphics2D g){
        if(isRunning){
            g.drawImage(this.frames.get(this.currentFrame), (int)x, (int)y, null);
        }
    }
}
