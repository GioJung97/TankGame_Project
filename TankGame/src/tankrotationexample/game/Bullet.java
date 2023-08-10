package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.Resources.ResourcePool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 3;
    private BufferedImage img;
    private Rectangle hitbox;
    boolean hasCollided = false;
    private int ID;

    Bullet(float x, float y, BufferedImage img, float angle, int ID) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
        this.ID = ID;
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    void setX(float x) { this.x = x;}

    void setY(float y) { this.y = y;}

    public int getID() {
        return ID;
    }

    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx * 2;
        y += vy * 2;
        this.hitbox.setLocation((int)x, (int)y);
    }

//    private void checkBorder() {
//        if (x < 30) {
//            x = 30;
//        }
//        if (x >= GameConstants.GAME_WORLD_WIDTH - 80) {
//            x = GameConstants.GAME_WORLD_WIDTH - 80;
//        }
//        if (y < 30) {
//            y = 30;
//        }
//        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
//            y = GameConstants.GAME_WORLD_HEIGHT - 80;
//        }
//    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        if(!hasCollided){
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() * 2 / 2.0,  this.img.getHeight() * 2 / 2.0);
//            rotation.scale(3,3);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
//      g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
//        g2d.drawRect((int)x,(int)y,this.img.getWidth() * 3, this.img.getHeight() * 3);
        }
    }

    public float getX() {
        return x;
    }

    public float getY(){
        return y;
    }

    @Override
    public void collides(GameObject with, GameWorld gw){
        if(!(with instanceof PowerUps)){
            gw.anims.add(new Animation(x, y, ResourceManager.getAnimation("bullethit")));
            if(with instanceof Walls){
                setHasCollided();
                ((Walls) with).setHasCollided();
            }else if(with instanceof Tank){
                if(((Tank) with).getID() != this.ID){
                    setHasCollided();
                }
            }
        }
    }

    @Override
    public boolean hasCollided() {
        return hasCollided;
    }

    public void setHasCollided(){
        this.hasCollided = true;
    }
}
