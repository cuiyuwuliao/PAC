package ghost;

import processing.core.*;
import java.util.*;
/**
* chase target: Double the vector from Chaser to 2 grid spaces ahead of Waka.
* scatter target: Bottom right corner
*/
public class Whim extends Ghost{

    public Whim(PImage img, int x, int y){
        super(img, x, y);
        this.type = MoveType.whim;
    }

    public Dir[] chase(Engine game){
        Grid target;
        int[] chaserLoc = null;
        for(Movable m : Movable.allMovables){
            if(m.type == MoveType.chaser && m.alive){
                chaserLoc = m.locID();
                break;
            }
        }
        if(chaserLoc == null){
            return scatter(game);
        }
    	int[] p1Loc = game.p1.locID();
    	Dir going;
    	if(game.p1.moveLog.size() != 0){
    		going = game.p1.moveLog.get(game.p1.moveLog.size() - 1);
	    	if (going == Dir.up){
	    		p1Loc[1] -= 2;
	    	}else if(going == Dir.down){
	    		p1Loc[1] += 2;
	    	}else if(going == Dir.left){
	    		p1Loc[0] -= 2;
	    	}else if(going == Dir.right){
	    		p1Loc[0] += 2;
	    	}
            int xDif = p1Loc[0] - chaserLoc[0];
            int yDif = p1Loc[1] - chaserLoc[1];
            p1Loc[0] += xDif;
            p1Loc[1] += yDif;
            target = game.map.searchByLoc(game.validifyLoc(p1Loc));
    	}else{
    		target = game.map.searchByLoc(game.p1.locID());
    	}
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);
    }

    public Dir[] scatter(Engine game){
        Grid target = game.map.corners.get(3);
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);

    }

}
