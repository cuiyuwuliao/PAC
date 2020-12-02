package ghost;

import processing.core.*;
import java.util.*;

/**
* chase target: Four grid spaces ahead of Waka (based on Waka’s current direction)
* scatter target: Top right corner
*/
public class Ambusher extends Ghost{
    public Ambusher(PImage img, int x, int y){
        super(img, x, y);
        this.type = MoveType.ambusher;
    }


    public Dir[] chase(Engine game){
    	int[] targetLoc = game.p1.locID();
    	Grid target;
    	Dir going;
    	if(game.p1.moveLog.size() != 0){
    		going = game.p1.moveLog.get(game.p1.moveLog.size() - 1);
	    	if (going == Dir.up){
	    		targetLoc[1] -= 4;
	    	}else if(going == Dir.down){
	    		targetLoc[1] += 4;
	    	}else if(going == Dir.left){
	    		targetLoc[0] -= 4;
	    	}else if(going == Dir.right){
	    		targetLoc[0] += 4;
	    	}
	    	target = game.map.searchByLoc(game.validifyLoc(targetLoc));
    	}else{
    		target = game.map.searchByLoc(game.p1.locID());
    	}
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);
    }

    public Dir[] scatter(Engine game){
        Grid target = game.map.corners.get(1);
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);

    }




}
