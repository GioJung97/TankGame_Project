package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject implements Walls{
    float x;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    float y;
    BufferedImage img;
    private Rectangle hitbox;

    Wall(float x, float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with, GameWorld gw) {

    }

    @Override
    public boolean hasCollided() {
        return false;
    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int)x, (int)y, null);

    }

    @Override
    public void setHasCollided() {

    }
}
