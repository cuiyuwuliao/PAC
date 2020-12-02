package ghost;

import processing.core.*;
import java.util.*;



public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private Manager m;
    public static int fps;
    

    public void setup() {
        fps = 60;
        frameRate(fps);

        //load files
        Config c1 = new Config("config.json");
        // Config c1 = new Config("testConfig/configTest.json");
        textFont(createFont("PressStart2P-Regular.ttf", 32));

        List<PImage> pPics = new ArrayList<PImage>();
        pPics.add(loadImage("src/main/resources/playerUp.png"));
        pPics.add(loadImage("src/main/resources/playerDown.png"));
        pPics.add(loadImage("src/main/resources/playerLeft.png"));
        pPics.add(loadImage("src/main/resources/playerRight.png"));
        pPics.add(loadImage("src/main/resources/playerClosed.png"));
        HashMap<String,PImage> gPics = new HashMap<String,PImage>();
        gPics.put("0",null);
        gPics.put("1",loadImage("horizontal.png"));
        gPics.put("2",loadImage("vertical.png"));
        gPics.put("3",loadImage("upLeft.png"));
        gPics.put("4",loadImage("upRight.png"));
        gPics.put("5",loadImage("downLeft.png"));
        gPics.put("6",loadImage("downRight.png"));
        gPics.put("7",loadImage("fruit.png"));
        gPics.put("p",null);
        gPics.put("g",loadImage("chaser.png"));
        gPics.put("a",loadImage("ambusher.png"));
        gPics.put("c",loadImage("chaser.png"));
        gPics.put("i",loadImage("ignorant.png"));
        gPics.put("w",loadImage("whim.png"));
        gPics.put("8",loadImage("fruit.png"));
        gPics.put("s",loadImage("soda.png"));

        HashMap<String,PImage> otherPics = new HashMap<String,PImage>();
        otherPics.put("frightened",loadImage("frightened.png"));
        otherPics.put("invisible",loadImage("invisible.png"));


        m = new Manager(c1, pPics, gPics,otherPics, fps);

    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }


    public void draw() { 
        imageMode(CENTER);
        background(0, 0, 0);
        m.game.draw(this);

    }

    public void keyPressed(){
        if (keyCode == 32){
            m.game.debugMode();
        }
        if (keyCode == UP){
            m.game.playerInput(Dir.up);       
        }
        if (keyCode == DOWN){
            m.game.playerInput(Dir.down);
        }
        if (keyCode == LEFT){
            m.game.playerInput(Dir.left);
        }
        if (keyCode == RIGHT){
            m.game.playerInput(Dir.right);
        }
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
