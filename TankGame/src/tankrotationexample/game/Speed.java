package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject implements PowerUps {

    float x, y;
    BufferedImage img;
    private Rectangle hitbox;
    private boolean removePU = false;
    boolean hasCollided = false;

    public Speed(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitbox() {
        return this.hitbox.getBounds();
    }

    public void drawImage(Graphics buffer) {
        if(!hasCollided){
            buffer.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    @Override
    public void setHasCollided() {
        this.hasCollided = true;
    }

    @Override
    public void collides(GameObject with, GameWorld gw) {
    }

    @Override
    public boolean hasCollided() {
        return hasCollided;
    }

    @Override
    public void applyPowerUp(GameObject Tank) {
        ((Tank) Tank).speedPowerUp();
    }
}
