package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class bWall extends GameObject implements Walls {

    float x;
    float y;
    BufferedImage img;
    private Rectangle hitbox;

    public bWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    @Override
    public void collides(GameObject ob) {

    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int)x, (int)y, null);

    }
}
