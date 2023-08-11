package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class chargeSpeed extends GameObject implements PowerUps{

    float x, y;
    BufferedImage img;
    private Rectangle hitbox;
    private boolean hasCollided = false;

    chargeSpeed(float x, float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    @Override
    public void drawImage(Graphics buffer) {
        if(!this.hasCollided){
            buffer.drawImage(this.img, (int)x, (int)y, null);
        }
    }

    @Override
    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
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
        ((Tank) Tank).chargeSpeedPowerUp();
    }

    @Override
    public void setHasCollided() {
        hasCollided = true;
    }
}
