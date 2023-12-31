package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;

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
    private int lifeCount = 3;
    private int ID;
    private boolean shieldEffect = false;
    List<Bullet> ammo = new ArrayList<>();

    private float R = 2;
    private float ROTATIONSPEED = 3.0f;

    long timeSinceLastShot = 0L;
    long coolDown = 3000;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;

    private Rectangle hitbox;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, int lifeCount, int ID) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
        this.lifeCount = lifeCount;
        this.ID = ID;
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

    void update(GameWorld gw) {
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
            var b = new Bullet(this.bLocationX(), this.bLocationY(), ResourceManager.getSprite("bullet"), angle, this.ID);
            this.ammo.add(b);
            gw.addGameObject(b);
            gw.anims.add(new Animation(x, y, ResourceManager.getAnimation("bulletshoot")));
            ResourceManager.getSound("bullet_shoot").playSound();
        }

        this.ammo.forEach(bullet -> bullet.update());
        this.hitbox.setLocation((int)x, (int)y);
    }

    private int bLocationX(){
        float safeX = this.x + Math.round((35 + R) * Math.cos(Math.toRadians(angle)));
        return (int) safeX;
    }

    private int bLocationY(){
        float safeY = this.y + Math.round((35 + R) * Math.sin(Math.toRadians(angle)));
        return (int)safeY;
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

        //shield img
        if(shieldEffect){
            g2d.drawImage(ResourceManager.getSprite("shieldEffect"), (int)this.x - 27, (int)this.y - 25, null);
        }

        //tank img
        g2d.drawImage(this.img, rotation, null);
        this.ammo.forEach(b -> b.drawImage(g2d));

        g2d.setColor(Color.YELLOW);
        //bullet cool time (charge) img
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

    public BufferedImage getLifeImg(){
        return ResourceManager.getSprite("life");
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

    public int getLifeCount (){
        return this.lifeCount;
    }

    public int getID(){
        return this.ID;
    }

    public boolean isShieldEffect() {
        return shieldEffect;
    }

    public void lifePowerUp(){
        if(this.lifeCount < 5){
            this.lifeCount ++;
        }
    }

    public void speedPowerUp(){
        if(this.R < 5){
            this.R++;
        }
    }

    public void chargeSpeedPowerUp(){
        if(this.coolDown > 500){
            this.coolDown -= 500;
        }
    }

    public void shieldEffectOn(){
        this.shieldEffect = true;
    }

    public void shieldEffectOff(){
        this.shieldEffect = false;
    }

    public float getAngle(){
        return angle;
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }

    public void collides(GameObject with, GameWorld gw){
        if(with instanceof Bullet){
            if(((Bullet) with).getID() != this.ID){
                if(this.shieldEffect == true){
                    shieldEffectOff();
                }else{
                    this.lifeCount --;
                }
            }
        }else if(with instanceof Walls){
            if (this.UpPressed) {
                x -= vx;
                y -=vy;
            }else if (this.DownPressed) {
                x += vx;
                y += vy;
            }
        }else if (with instanceof PowerUps){
            if(with instanceof Health){
                gw.anims.add(new Animation(x + 20, y - 50, ResourceManager.getAnimation("healthpick")));
            }else if (with instanceof Shield){
                gw.anims.add(new Animation(x - 40, y - 50, ResourceManager.getAnimation("shieldpick")));
            }else if (with instanceof Speed){
                gw.anims.add(new Animation(x, y, ResourceManager.getAnimation("speedpick")));
            }else { //with instance of chargeSpeed
                gw.anims.add(new Animation(x, y, ResourceManager.getAnimation("chargepick")));
            }
            ((PowerUps) with).setHasCollided();
            ((PowerUps) with).applyPowerUp(this);
            ResourceManager.getSound("pickup").playSound();
        }
    }

    @Override
    public boolean hasCollided() {
        return false;
    }
}
