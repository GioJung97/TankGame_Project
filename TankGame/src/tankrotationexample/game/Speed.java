package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject implements PowerUps{

    float x, y;
    BufferedImage img;
    private Rectangle hitbox;
    private boolean removePU = false;

    public Speed(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int)x, (int)y, null);

    }

    @Override
    public void collides(GameObject ob) {

    }

    @Override
    public void applyPowerUp(GameObject Tank) {

    }

    @Override
    public boolean isRemovePU() {
        return removePU = true;
    }
}
