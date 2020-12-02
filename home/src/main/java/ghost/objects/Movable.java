package ghost;

import processing.core.*;
import java.util.*;

/**
* base class for ghosts and player
*/
public abstract class Movable extends Basic{
    int v;
    Dir going;
    Dir command;
    Grid currentGrid;
    Grid nextGrid;
    boolean alive = true;
    int[] startingXY = new int[2];
    static List<Movable> allMovables = new ArrayList<Movable>();
    List<Dir> moveLog = new ArrayList<Dir>();
    MoveType type;
    static final Dir[] dirs = {Dir.up, Dir.down, Dir.left, Dir.right};


    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Movable(PImage img, int x, int y){
        super(img, x, y);
        this.sprite = img;
        this.startingXY[0] = x;
        this.startingXY[1] = y;
        allMovables.add(this);
    }
    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Movable(List<PImage> img, int x, int y){
        super(img, x, y);
        this.sprite = img.get(0);
        this.startingXY[0] = x;
        this.startingXY[1] = y;
        allMovables.add(this);
    }

    /**
    * set speed, minimum speed is 0
    * @param speed speed to be set
    */
    public void setSpeed(int speed){
        if(speed < 0){
            this.v = 0;
            return;
        }
        this.v = speed;
    }

    /**
    * move one pixcel to the direction curenttly going
    */
    public void move(){
        if(going == Dir.up){this.y -= v;}
        if(going == Dir.down){this.y += v;}
        if(going == Dir.left){this.x -= v;}
        if(going == Dir.right){this.x += v;}
    }

    /**
    * set direction curenttly going
    * @param d a direction
    */

    public void setGoing(Dir d){
        this.going = d;
    }

    /**
    * set command to be executed at intersection 
    * @param d a direction
    */
    public void setCommand(Dir d){
        this.command = d;
    }

    
    /**
    * check current direction is valid, if not, set to null, execute command
    * @param game engine object
    * @return return true if command executed
    */
    public boolean tick(Engine game){
        readGrid(game);
        if(this.nextGrid != null){
            if (this.nextGrid.getType() == GridType.wall){
                setGoing(null);
            }
        }
        if(exeCommand(game)){
            this.moveLog.add(command);
            this.command = null;
            return true;
        }
        return false;
    }

    /**
    * execute command, set command to null
    * @param game engine object
    * @return if executed return true
    */
    private boolean exeCommand(Engine game){
        // System.out.println(this.x +","+this.y+this.command);
        if (this.command == null){
            return false;
        }
        Grid targetGrid = game.map.gridNearBy(currentGrid,this.command);
        if (targetGrid.type != GridType.wall){
            setGoing(this.command);
            return true;
        }
        return false;
    }
    /**
    * reset movable's location target, command, moving direction, move log
    * @param game engine object
    */

    public void reset(Engine game){
        this.x = startingXY[0];
        this.y = startingXY[1];
        this.command = null;
        this.going = null;
        this.currentGrid = null;
        this.moveLog = new ArrayList<Dir>();
        readGrid(game);
    }

    /**
    * update current grid and next grid based on current location
    * @param game engine object
    */

    public void readGrid(Engine game){
        if(currentGrid == null){
            int[] currentLoc = locID();
            HashMap<int[],Grid> gDic = game.map.gDic;
            for(Map.Entry e : gDic.entrySet()){
                int[] mapKey = (int[]) e.getKey();
                if(Arrays.equals(currentLoc,mapKey))
                    this.currentGrid = (Grid) e.getValue();
                    this.nextGrid = game.map.gridNearBy(currentGrid,going);
            }
        }
        this.nextGrid = game.map.gridNearBy(currentGrid,going);

    }

    /**
    * @param d a direction
    * @return opposite direction
    */
    public static Dir ops(Dir d){
        if(d == Dir.up){return Dir.down;}
        if(d == Dir.left){return Dir.right;}
        if(d == Dir.down){return Dir.up;}
        if(d == Dir.right){return Dir.left;}
        return null;
    }

    public void draw(App app){
        if (sprite != null)
            app.image(sprite,x + 8,y);
    }


}
