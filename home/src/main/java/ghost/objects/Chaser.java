package ghost;

import processing.core.*;
import java.util.*;
/**
* chase target: Waka’s position
* scatter target: Top left corner
*/
public class Chaser extends Ghost{

    public Chaser(PImage img, int x, int y){
        super(img, x, y);
        this.type = MoveType.chaser;
    }

    public Dir[] chase(Engine game){
        setTargetXY(game.p1.x,game.p1.y);
        return gsAI(game.p1.x,game.p1.y);
    }


    public Dir[] scatter(Engine game){
        Grid target = game.map.corners.get(0);
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);

    }




}
