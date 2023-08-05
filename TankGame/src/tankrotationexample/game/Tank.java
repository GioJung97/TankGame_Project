package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.Resources.ResourcePool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Tank extends GameObject{

    private float x;
    private float y;
    private float sX;
    private float sY;
    private float vx;
    private float vy;
    private float angle;
    List<Bullet> ammo = new ArrayList<>();

    private float R = 2;
    private float ROTATIONSPEED = 3.0f;

    long timeSinceLastShot = 0L;
    long coolDown = 2000;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;

    private Rectangle hitbox;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if(this.shootPressed && ((this.timeSinceLastShot + this.coolDown) < System.currentTimeMillis())){
            this.timeSinceLastShot = System.currentTimeMillis();
            this.ammo.add(new Bullet(x, y, ResourceManager.getSprite("bullet"), angle));

        }

        this.ammo.forEach(bullet -> bullet.update());
        this.hitbox.setLocation((int)x, (int)y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
       centerScreen();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        centerScreen();
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 80) {
            x = GameConstants.GAME_WORLD_WIDTH - 80;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    public void centerScreen(){
        this.sX = this.x - GameConstants.GAME_SCREEN_WIDTH / 4.f;
        this.sY = this.y - GameConstants.GAME_SCREEN_HEIGHT / 2.f;

        if(this.sX < 0) this.sX = 0;
        if(this.sY < 0) this.sY = 0;
        if(this.sX > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2){
            this.sX = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2;
        }
        if(this.sY > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT){
            this.sY = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        this.ammo.forEach(b -> b.drawImage(g2d));
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);

        //hit box for tank
//        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

        g2d.setColor(Color.YELLOW);
        //charge gage
        g2d.drawRect((int)x, (int)y-20, 50, 10);
        long currWidth = 50 -((this.timeSinceLastShot + this.coolDown) - System.currentTimeMillis()) / 60;
        if(currWidth > 50){
            currWidth = 50;
        }
        g2d.fillRect((int)x, (int)y-20, (int)currWidth, 10 );



    }

    @Override
    public Rectangle getHitbox() {
        return this.hitbox.getBounds();
    }

    public float getX() {
        return x;
    }

    public float getY(){
        return y;
    }

    public float getsX() {
        return sX;
    }

    public float getsY() {
        return sY;
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;

    }

    public void collides(GameObject with){
        if(with instanceof Bullet){
            //lose life
        }else if(with instanceof Walls){
            //stop
            x -= vx;
            y -=vy;
        }else if (with instanceof PowerUps){
            ((PowerUps) with).applyPowerUp(this);
        }
    }
}
