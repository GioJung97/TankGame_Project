package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {

    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();
    private final static Map<String, Clip> sounds = new HashMap<>();
    private static final Map<String, Integer> animationInfo = new HashMap<>(){{
        put("bullet", 32);
        put("nuke", 24);
//        put("powerpick", 32);
//        put("puffsmoke", 32);
//        put("rocketflame", 16);
//        put("rockethit", 32);
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
            ResourceManager.sprites.put("bSpeedPU", loadSprite("powerups/bulletSpeed.png"));
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
        return ResourceManager.animations.get(type);
    }

    public static void loadResources(){
        ResourceManager.initSprite();
        ResourceManager.initAnimation();
    }

//    private static Sound loadSound(String path){
//        AudioInputStream ais = AudioSystem.getAudioInputStream(
//                Objects.requireNonNull(ResourceManager.class.getClassLoader().getResources(path))
//        );
//
//        Clip c = AudioSystem.getClip();
//        c.open(ais);
//        Sound s = new Sound(c);
//        s.setVolume(.2f);
//
//        return s;
//    }

//    private static void initSounds(){
//        try{
//            ResourceManager.sounds.put("shoot", loadSound("sounds/bullet_shoot.wav"));
//            ResourceManager.sounds.put("pickup", loadSound("sounds.pickup"));
//            ResourceManager.sounds.put("background", loadSound("sounds/Music.mid"));
//            ResourceManager.sounds.put("bullethit", loadSound("sounds/explosion.wav"));
//        }catch(UnsupportedOperationException e){
//            throw new RuntimeException(e);
//        }
//    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }

    public static void main(String[] args) {
        ResourceManager.loadResources();
        System.out.println();
    }
}
