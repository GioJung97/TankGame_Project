package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

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

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        if(!hasCollided){
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() * 2 / 2.0,  this.img.getHeight() * 2 / 2.0);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
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
            ResourceManager.getSound("bullethit").playSound();
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
