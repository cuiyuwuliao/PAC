package ghost;

import processing.core.*;
import java.util.*;
import java.lang.Math.*;
/**
* game logic, check and update game status
*/
public class Engine{
    GMap map;
    Player p1;
    int panicLength;
    List<Ghost> gs = new ArrayList<Ghost>();
    int countFrame = 0;
    List<Integer>modeTime;
    boolean chase = false;
    boolean reset = true;
    boolean debugMode = false;
    int modeIndex = 0;
    int playerTimer = 0;
    int screenTimer = 0;
    int screenStatus = 0;

    public Engine(GMap map, Player p1, List<Ghost> gs, List<Integer>modeTime, int panicLength){
        this.map = map;
        this.p1 = p1;
        this.gs = gs;
        this.modeTime = modeTime;
        this.panicLength = panicLength;
        ModeManager.panic(panicLength);
        ModeManager.sodaTime(panicLength);
        for(Movable m : Movable.allMovables){
            m.readGrid(this);
        }        

    }
    /**
    * switch bewteen chase and scatter mode
    */
    public void modeSwitch(){
        if (chase == true){
            chase = false;
        }else{chase = true;}
    }

    /**
    * trigger player's image change every 8 frame
    */
    private void playerAnimation(){
        playerTimer += 1;
        if(playerTimer == 8){
            p1.animation();
            playerTimer = 0;
        }
    }

    /**
    * check and update game 
    */
    public void tick(){
        countFrame += 1;
        checkCollision();
        checkGame();
        ModeManager.tick(this);
        playerAnimation();

        if(reset){
            Ghost.gsInit(this);
            this.reset = false;
        }

        if(countFrame > modeTime.get(modeIndex)){
            modeSwitch();
            this.modeIndex += 1;
            if(modeIndex >= modeTime.size())
                modeIndex = 0;
            if(countFrame > modeTime.get(modeTime.size()-1)){
                countFrame = 0;
            }
        }

        if(p1.x == p1.startingXY[0] && p1.y == p1.startingXY[1]){
            p1.currentGrid = map.searchByLoc(p1.locID());
            p1.tick(this);
        }

        for(Grid gd : map.deadEnds){
            playerUpdate(gd);
            gsCommand(gd, 3);

        }
        for(Grid gd : map.turns){
            playerUpdate(gd);
            gsCommand(gd, 1);
        }
        for(Grid gd : map.intersections){
            playerUpdate(gd);
            gsCommand(gd, 2);
        }

        for(Grid gd : map.fruitGrids){
            if(p1.x == gd.x && p1.y == gd.y){
                gd.step(this);
            }
        }

        for(Grid gd : map.specials){
            if(p1.x == gd.x && p1.y == gd.y){
                gd.step(this);
            }
        }

        for(Movable m : Movable.allMovables){
            m.move();
        }
    }

    /**
    * process keyboard input, command player accordingly
    * @param d a direction
    */
    public void playerInput(Dir d){
        if(p1.firstMove){
            Grid next = map.gridNearBy(p1.currentGrid,d);
            if(next.type != Basic.GridType.wall){
                p1.setGoing(d);
                p1.moveLog.add(d);
                p1.firstMove = false;
            }
            
        }else{
            if (p1.going == null){
                p1.setGoing(d);
                p1.moveLog.add(d);
                return;
            }
            if(Movable.ops(d) == p1.going){
                p1.setGoing(d);
                p1.command = null;
                p1.moveLog.add(d);
                return;
            }
            p1.command = d;  

        }

    }

    /**
    * find closest location in the map
    * @param loc row and col
    * @return closest location in the map
    */
    public int[] validifyLoc(int[] loc){
        if(loc[0] < 0){
            loc[0] = 0;
        }else if(loc[0] > map.size[0] - 1){
            loc[0] = map.size[0] - 1;
        }
        if(loc[1] < 0){
            loc[1] = 0;
        }else if(loc[1] > map.size[1] - 1){
            loc[1] = map.size[1] - 1;
        }
        return loc;
    }

    /**
    * find the closest corner to the given object
    * @param b any map object
    * @return closest corner in the map
    */
    public Grid closeC(Basic b){
        double dis = 10000000;
        int index = -1;
        for(Grid c : map.corners){
            double temp = distance(b.x, b.y, c.x, c.y);
            if (temp <= dis){
                dis = temp;
                index ++;
            }
        }
        return map.corners.get(index);
    }

    /**
    * if ghost in actionable grid, check and update ghost status
    * @param gd actionable grid to be tested
    * @param roadType type of the actionable grid
    */
    private void gsCommand(Grid gd, int roadType){
        for(Ghost g : gs){
            if (g.x == gd.x && g.y == gd.y){
                g.currentGrid = gd;
                g.logic(this, roadType);
            }
        }
    }
    /**
    * if ghost in actionable grid, check and update ghost status
    * @param gd actionable grid to be tested
    * @return true if sucess
    */
    private boolean playerUpdate(Grid gd){
        if (p1.x == gd.x && p1.y == gd.y){
            p1.currentGrid = gd;
            p1.tick(this);
            return true;
        }
        return false;
    }


    /**
    * check collision, valid collision cost a life, then reset all movables
    */
    private void checkCollision(){
        boolean collide = false;
        for(Ghost g : gs){
            if (!g.alive){
                continue;
            }
            double distance = distance(g.x, g.y, p1.x, p1.y);
            if(distance < 16){
                if (!g.beEaten()){
                    collide = true;
                    p1.lives -= 1;
                    break;
                }
            }
        }
        if (collide){
            Ghost.resurrection(this);
            for(Movable m : Movable.allMovables){
                m.reset(this);
            }
            p1.firstMove = true;
            this.reset = true;
        }

    }

    /**
    * switch between debuge mode and normal mode
    */
    public void debugMode(){
        if(debugMode){
            debugMode = false;
        }else{debugMode = true;}
    }

    
    /**
    * draw lines between ghosts and their target
    * @param app app object
    */
    private void debugLine(App app){
        if(debugMode && !ModeManager.gsRandom){
                for(Ghost g : gs){
                    if (g.alive){
                        app.line(g.x, g.y, g.targetXY[0], g.targetXY[1]);
                        app.stroke(255);                     
                    }

                }
            }
    }

    /**
    * draw player's remaining lives
    * @param app app object
    */
    private void drawLives(App app){
        int i = 1;
        int ii = 20;
        while(i <= p1.lives){
            app.image(p1.img.get(3),ii, 550);
            i += 1;
            ii += 30;
        }
    }

    /**
    * display result screen based on screen status
    * @param app app object
    */
    private void endScreen(App app){
        if(screenStatus == 0)
            return;
        app.fill(0);
        app.rect(0, 0, 10000, 10000);
        app.fill(255);
        if(screenStatus == 1){
            app.text("YOU WIN", 110, 200);
        }else if(screenStatus == 2){
            app.text("GAME OVER", 80, 200);
        }
    }


    /**
    * check if game ends, trigger result screen
    */
    public void checkGame(){
        if(screenStatus == 0){
            if(p1.lives == 0){
                p1.alive = false;
                screenStatus = 2;
            }else if(map.fruitRemain == 0)
                screenStatus = 1;
        }
        
        if(screenStatus == 1 || screenStatus == 2)
            screenTimer += 1;
        
        if(screenTimer == 600){
            restart();
        }
    }

    /**
    * reset game
    */
    public void restart(){
        Ghost.resurrection(this);
        for(Grid gd : map.allGrids){
            gd.reset();
        }
        for(Movable m : Movable.allMovables){
            m.reset(this);
        }
        p1.lives = p1.maxLives;
        p1.firstMove = true;
        map.fruitRemain = map.fruitNum;
        this.screenStatus = 0;
        this.countFrame = 0;
        this.modeIndex = 0;
        this.screenTimer = 0;
        ModeManager.reset(this);
        this.chase = false;
        this.chase = false;
        this.reset = true;
    }
    
    /**
    * return distance between two coordinate
    * @param x x of the first coordinate
    * @param y y of the first coordinate
    * @param xx x of the second coordinate
    * @param yy y of the second coordinate
    * @return distance between two coordinate
    */
    public static double distance(int x,int y,int xx,int yy){
        int side1 = Math.abs(xx - x);
        int side2 = Math.abs(yy - y);
        double result = Math.sqrt(side1 * side1 + side2 * side2);
        return result;
    }



    public void draw(App app){
        // System.out.println(chase + " "+ countFrame);
        tick();
        map.draw(app);
        for(Movable m : Movable.allMovables){
            m.draw(app);
        }
        debugLine(app);
        drawLives(app);
        endScreen(app);  
    }
}