package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ResourceManager {

    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private static final Map<String, Integer> animationInfo = new HashMap<>(){{
        put("bullethit", 24);
        put("bulletshoot", 24);
        put("shieldpick", 32);
        put("chargepick", 10);
        put("healthpick", 10);
        put("speedpick", 16);
    }};

    private static BufferedImage loadSprite(String path) throws IOException{
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager
                                .class
                                .getClassLoader()
                                .getResource(path),
                        "%s image is missing".formatted(path)));
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
            ResourceManager.sprites.put("chargeSpeedPU", loadSprite("powerups/chargeSpeed.png"));
            ResourceManager.sprites.put("wall", loadSprite("walls/unbreak.jpg"));
            ResourceManager.sprites.put("bWall1", loadSprite("walls/break1.jpg"));
            ResourceManager.sprites.put("bWall2", loadSprite("walls/break2.jpg"));
            ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("life", loadSprite("Life/life.png"));
            ResourceManager.sprites.put("shieldEffect", loadSprite("shield/shield.png"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void initAnimation(){
        String baseName = "animations/%s/%s_%04d.png";

        ResourceManager.animationInfo.forEach((animationName, frameCount) -> {
            List <BufferedImage> frames = new ArrayList<>();
            try{
                for(int i=0; i<frameCount; i++){
                    String spritePath = baseName.formatted(animationName, animationName, i);
                    frames.add(loadSprite(spritePath));
                }
                ResourceManager.animations.put(animationName, frames);
            }catch(IOException e){
                System.out.println(e);
                throw new RuntimeException(e);
            }
        });
    }

    public static List<BufferedImage> getAnimation(String type){
        if(!ResourceManager.animations.containsKey(type)){
            throw new RuntimeException("%s is missing from Animation Files.".formatted(type));
        }
        return ResourceManager.animations.get(type);
    }

    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL resourceURL = ResourceManager.class.getClassLoader().getResource(path);

        if (resourceURL == null) {
            throw new FileNotFoundException("%s is missing from Sound Files.".formatted(path));
        }

        AudioInputStream ais = AudioSystem.getAudioInputStream(resourceURL);

        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(.1f);

        return s;
    }

    private static void initSounds(){
        try{
            ResourceManager.sounds.put("bullet_shoot", loadSound("sounds/bullet_shoot.wav"));
            ResourceManager.sounds.put("pickup", loadSound("sounds/pickup.wav"));
            ResourceManager.sounds.put("background", loadSound("sounds/Music.mid"));
            ResourceManager.sounds.put("bullethit", loadSound("sounds/explosion.wav"));
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static Sound getSound (String type){
        if(!ResourceManager.sounds.containsKey(type)){
            throw new RuntimeException("%s is missing from Sound files.".formatted(type));
        }
        return ResourceManager.sounds.get(type);
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }

    public static void loadResources(){
        ResourceManager.initSprite();
        ResourceManager.initAnimation();
        ResourceManager.initSounds();
    }
}
