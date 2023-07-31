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
    List<GameObject> gobjs = new ArrayList<>(800);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update();
                this.t2.update();// update tank
                //check collisions
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
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
                );

        //9 -> unbreakable && not in a collisions
        //3 -> unbreakable
        //2 -> breakable
        //4 -> shield
        //5 -> health
        //6 -> speed
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


        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(500, 500, 0, 0, (short) 0, ResourceManager.getSprite("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);
        this.lf.getJf().addKeyListener(tc2);

    }

    public void renderFloor(Graphics g){
        for(int i = 0; i< GameConstants.GAME_WORLD_WIDTH; i+=320){
            for(int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240){
                g.drawImage(ResourceManager.getSprite("floor"), i, j, null);
            }
        }
    }

    static double scaleFactor = 0.2;
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
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        renderSplitScreen(g2);
        renderMiniMap(g2);
    }
}
