package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Health extends GameObject implements PowerUps {

    float x, y;
    BufferedImage img;
    private Rectangle hitbox;
    private boolean hasCollided = false;

    public Health(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    public void drawImage(Graphics buffer) {
        if(!this.hasCollided){
            buffer.drawImage(this.img, (int)x, (int)y, null);
        }
    }

    @Override
    public void collides(GameObject with) {
    }

    @Override
    public boolean hasCollided() {
        return hasCollided;
    }

    @Override
    public void setHasCollided() {
        hasCollided = true;
    }

    @Override
    public void applyPowerUp(GameObject Tank) {
        ((Tank) Tank).lifePowerUp();
    }
}
