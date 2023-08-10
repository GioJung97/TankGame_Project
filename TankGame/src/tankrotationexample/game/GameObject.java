package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;

public abstract class GameObject {

    public static GameObject newInstance(String gameObjectType, float x, float y) throws UnsupportedOperationException{
        return switch(gameObjectType){
            case "9", "3" -> new Wall(x, y, ResourceManager.getSprite("wall"));
            case "1" -> new bWall(x, y, ResourceManager.getSprite("bWall2"));
            case "2" -> new bWall(x, y, ResourceManager.getSprite("bWall1"));
            case "4" -> new Shield(x, y, ResourceManager.getSprite("shieldPU"));
            case "5" -> new Health(x, y, ResourceManager.getSprite("healthPU"));
            case "6" -> new Speed(x, y, ResourceManager.getSprite("speedPU"));
            case "7" -> new chargeSpeed(x, y, ResourceManager.getSprite("chargeSpeedPU"));
            default -> throw new UnsupportedOperationException("%s Invalid Game Object Type".formatted(gameObjectType));
        };
    }

    public abstract void drawImage(Graphics g);

    public abstract Rectangle getHitbox();

    public abstract void collides(GameObject with, GameWorld gw);

    public abstract boolean hasCollided();
}
