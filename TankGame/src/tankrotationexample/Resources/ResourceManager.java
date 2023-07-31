package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animation = new HashMap<>();
    private final static Map<String, Clip> sounds = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException{
        return ImageIO.read(
                Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
    }

    private static void initSprite(){
        try {
            ResourceManager.sprites.put("tank1", loadSprite("tank/tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprite("tank/tank2.png"));
            ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1", loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2", loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
            ResourceManager.sprites.put("healthPU", loadSprite("powerups/health.png"));
            ResourceManager.sprites.put("shieldPU", loadSprite("powerups/shield.png"));
            ResourceManager.sprites.put("speedPU", loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("wall", loadSprite("walls/unbreak.jpg"));
            ResourceManager.sprites.put("bWall1", loadSprite("walls/break1.jpg"));
            ResourceManager.sprites.put("bWall2", loadSprite("walls/break2.jpg"));
            ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void loadResources(){
        ResourceManager.initSprite();
    }


    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }
}
