package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.game.GameObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    private List<GameObject> gobjs = new ArrayList<>(800);
    List<Animation> anims = new ArrayList<>();
    Sound bg = ResourceManager.getSound("background");

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            bg.setLooping();
            bg.playSound();
            while (true) {
                this.tick++;
                this.t1.update(this);
                this.t2.update(this);
                this.anims.forEach(animation -> animation.update());
                this.checkCollision();
                this.gobjs.removeIf(gameObject -> gameObject.hasCollided());
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);

                //end game
                if(t1.getLifeCount() == 0 || t2.getLifeCount() == 0){
                    resetGame();
                    lf.setFrame("end");
                    break;
                }
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollision() {
        for (int i = 0; i < this.gobjs.size(); i++) {
            GameObject ob1 = this.gobjs.get(i);
            if(ob1 instanceof Walls || ob1 instanceof PowerUps) continue;
            for (int j = 0; j < this.gobjs.size(); j++) {
                if(i == j) continue;
                GameObject ob2 = this.gobjs.get(j);
                if(ob1.getHitbox().intersects(ob2.getHitbox())){
//                    System.out.println("Hit");
                    ob1.collides(ob2, this);
                }
            }
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        gobjs.clear();
        anims.clear();
        InitializeGame();
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);


        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                    ResourceManager.class.getClassLoader().getResourceAsStream("maps/TankGameMap.csv"))
//                  ResourceManager.class.getClassLoader().getResourceAsStream("maps/TestMap.csv"))
                );

        //9 -> unbreakable && not in a collisions
        //3 -> unbreakable
        //2 -> breakable
        //4 -> shield
        //5 -> health
        //6 -> speed
        //7 -> bSpeed
        //0 -> nothing

        try (BufferedReader mapReader = new BufferedReader(isr)){
            int row = 0;
            String [] gameItems;
            while(mapReader.ready()){
                gameItems = mapReader.readLine().strip().split(",");
                for(int col = 0; col < gameItems.length; col++){
                    String gameObjectType = gameItems[col];
                    if("0".equals(gameObjectType)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObjectType, col * 30, row * 30));
                }
                row ++;
            }
        }catch (IOException e){
            throw new RuntimeException();
        }


        t1 = new Tank(120, 120, 0, 0, (short) 90, ResourceManager.getSprite("tank1"), 3, 0);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(1750, 1270, 0, 0, (short) 270, ResourceManager.getSprite("tank2"), 3, 1);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);
        this.lf.getJf().addKeyListener(tc2);

        this.gobjs.add(t1);
        this.gobjs.add(t2);
    }

    public void renderFloor(Graphics g){
        for(int i = 0; i< GameConstants.GAME_WORLD_WIDTH; i+=320){
            for(int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240){
                g.drawImage(ResourceManager.getSprite("floor"), i, j, null);
            }
        }
    }

    static double scaleFactor = 0.1;
    public void renderMiniMap(Graphics2D g){
        BufferedImage miniM = this.world.getSubimage(0, 0,
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT);

        var miniMX = GameConstants.GAME_SCREEN_WIDTH/2 - (GameConstants.GAME_WORLD_WIDTH * scaleFactor) / 2;
        var miniMY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT * scaleFactor) - 35;
        AffineTransform miniMapTransform = AffineTransform.getTranslateInstance(miniMX, miniMY);
        miniMapTransform.scale(scaleFactor, scaleFactor);
        g.drawImage(miniM, miniMapTransform, null);
    }

    public void renderSplitScreen(Graphics2D g){
        BufferedImage lh = this.world.getSubimage((int) this.t1.getsX(), (int) this.t1.getsY(),
                GameConstants.GAME_SCREEN_WIDTH/2,
                GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = this.world.getSubimage((int) this.t2.getsX(), (int) this.t2.getsY(),
                GameConstants.GAME_SCREEN_WIDTH/2,
                GameConstants.GAME_SCREEN_HEIGHT);

        g.drawImage(lh, 0, 0, null);
        g.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH/2 + 4, 0, null);

        this.t1.centerScreen();
        this.t2.centerScreen();
    }

    public void lifeScreen(Graphics2D g){
        int gap1 = 0;
        int gap2 = 0;
        for (int i = 0; i < t1.getLifeCount(); i++) {
            g.drawImage(t1.getLifeImg(), 25 + gap1, 25, null);
            gap1 += 40;
        }
        for (int i = 0; i < t2.getLifeCount(); i++) {
            g.drawImage(t2.getLifeImg(), GameConstants.GAME_SCREEN_WIDTH/2 + 560 + gap2, 850, null);
            gap2 -= 40;
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.anims.forEach(animation -> animation.drawImage(buffer));
        renderSplitScreen(g2);
        renderMiniMap(g2);
        lifeScreen(g2);
    }

    public void addGameObject(Bullet obj) {
        this.gobjs.add(obj);
    }
}
