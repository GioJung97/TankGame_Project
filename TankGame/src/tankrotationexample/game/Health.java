package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Health extends GameObject implements PowerUps {

    float x, y;
    BufferedImage img;
    private Rectangle hitbox;
    private boolean removePU = false;
    int healCount = 3;

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
        buffer.drawImage(this.img, (int)x, (int)y, null);

    }

    @Override
    public void collides(GameObject with) {
        if(with instanceof Tank){
            removePU = true;
        }
    }

    @Override
    public void applyPowerUp(GameObject Tank) {

    }

    public boolean isRemovePU(){
        return this.removePU;
    }
}
