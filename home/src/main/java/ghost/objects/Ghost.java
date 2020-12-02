package ghost;

import processing.core.*;
import java.util.*;


/**
 * common logic for all ghosts
 */
public abstract class Ghost extends Movable{
    int[] targetXY = new int[2];
    boolean panic = false;
    boolean invisible = false;

    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Ghost(PImage img, int x, int y){
        super(img, x, y);
    }
    

    /**
     * start all static ghosts based on current mode
     * @param game engine object
     */
    public static void gsInit(Engine game){
        for(Ghost g : game.gs){
            if(g.going == null){
                Dir[] ds = new Dir[4];
                if (game.chase == true){
                    ds = g.chase(game);
                }else{
                    ds = g.scatter(game);
                }
                for(Dir d : ds){
                    Grid nextGrid = game.map.gridNearBy(g.currentGrid,d);
                    if(nextGrid.type != Basic.GridType.wall){
                        g.setGoing(d);
                        break;
                    }
                }
            }
        }
    }
    /**
    * determine the best directions to go to the target(best to worst) 
    * @param x target x coordinate
    * @param y target x coordinate
    * @return the best directions to go to the target(best to worst) 
    */

    public Dir[] gsAI(int x, int y){
        Dir[] result = new Dir[4];
    	int xDis = Math.abs(this.x - x);
        int yDis = Math.abs(this.y - y);
        if(xDis >= yDis){
            if(this.x - x >= 0){
                result[0] = Dir.left;
                result[3] = Dir.right;
            }
            if(this.x - x < 0){
                result[0] = Dir.right;
                result[3] = Dir.left;
            }
            if(this.y - y >= 0){
                result[1] = Dir.up;
                result[2] = Dir.down;
            }
            if(this.y - y < 0){
                result[1] = Dir.down;
                result[2] = Dir.up;
            }
        }
        if(yDis > xDis){
            if(this.y - y >= 0){
                result[0] = Dir.up;
                result[3] = Dir.down;
            }
            if(this.y - y < 0){
                result[0] = Dir.down;
                result[3] = Dir.up;
            }
            if(this.x - x >= 0){
                result[1] = Dir.left;
                result[2] = Dir.right;
            }
            if(this.x - x < 0){
                result[1] = Dir.right;
                result[2] = Dir.left;
            }
            
        }
        return result;
    }

    /**
    * set current target coordinate
    * @param x target x coordinate
    * @param y target x coordinate
    */
    public void setTargetXY(int x, int y){
        this.targetXY[0] = x;
        this.targetXY[1] = y;
    }
    /**
     * determine the optimal directions to the target in chase mode
     * @param game engine object
     * @return optimal directions (from best to worst) 
     */
    public abstract Dir[] chase(Engine game);

    /**
     * determine the optimal directions to the target in scatter mode
     * @param game engine object
     * @return optimal directions (from best to worst) 
     */
    public abstract Dir[] scatter(Engine game);

    /**
     * determine panic mode direction
     * @return random directions
     */
    public Dir[] panic(){
        Dir[] index = {Dir.up, Dir.down, Dir.left, Dir.right};
        List<Dir> list = Arrays.asList(index);
        Collections.shuffle(list);
        return list.toArray(index);
    }

    /**
     * if panicing, set sprite to null, and die
     * @return if eaten return true
     */
    public boolean beEaten(){
        if(panic){
            this.sprite = null;
            this.alive = false;
            return true;
        }else{
            return false;
        }  
    }

    /**
     * revive the ghost, set alive true, show image again, stop panicing
     */
    public void revive(){
        this.alive = true;
        this.sprite = img.get(0);
        this.panic = false;
    }
    
    /**
     * set all ghosts alive
     * @param game engine object
     */
    public static void resurrection(Engine game){
        for(Ghost g : game.gs){
            g.revive();
        }
    }

    /**
     * back to normal mode
     */
    public void toNormal(){
        if(alive){
            this.panic = false;
            this.invisible = false;
            this.sprite = img.get(0);

        }
        
    }



    /**
     * based on current roadType and mode, determine the best directions
     * (2 is intersection, 1 is turn, 3 is deadEnd)
     * try the directions from the best to worst
     * allow turning back only when in deadend
     * @param game engine object
     * @param roadType numerical representation of roadType
     */
    public void logic(Engine game, int roadType){
        Dir[] ds = new Dir[4];
        if(roadType == 2 || roadType == 1 ){
            if(panic || invisible){
                ds = panic();
            }else if(game.chase){
                    ds = chase(game);
                }else{
                    ds = scatter(game);
                }
        }
        else if (roadType == 3){
            setCommand(Movable.ops(this.going));
            tick(game);
            return;

        }   
            Dir currentGoing = this.going;
            setCommand(ds[0]);
            for(Dir d : ds){
                if(currentGoing == Movable.ops(d)){
                    continue;
                }
                setCommand(d);
                if(tick(game)){
                    break;
                } 
            } 
    }


}
