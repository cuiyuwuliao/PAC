package ghost;

import processing.core.*;
import java.util.*;
/**
* Saver store which mode the game is currently in and countFrame
* Saver can load the game to the saved time stamp
*/
class Saver{
    int[] timeSave = {0,0};
    boolean chaseSave = false;
    /**
    * save modeIndex, countFrame,and mode from game
    * @param game Engine object
    */
    public void save(Engine game){
        this.timeSave[0] = game.modeIndex;
        this.timeSave[1] = game.countFrame;
        this.chaseSave = game.chase;
    }
    /**
    * load modeIndex, countFrame and mode 
    * @param game Engine object
    */
    public void load(Engine game){
        game.modeIndex = timeSave[0];
        game.countFrame = timeSave[1];
        game.chase = chaseSave;
    }
}

/**
* ModeManager manage special modes for the game
* automatically switch off when time's up
* ModeManager can't be instantiated 
*/
public abstract class ModeManager{
    public enum ModeType{
        panic,sodaTime
    }
    ModeType type;
    boolean status = false;
    int timer = 0;
    int length;
    Saver save = new Saver();;
    public static boolean gsRandom = false;
    public static List<ModeManager> modeList = new ArrayList<ModeManager>();

    /**
    * @param length duration of the mode
    */
    public ModeManager(int length){
        this.length = length;
    }

    /**
    * check and update mode status
    * @param game Engine object
    */
    public static void tick(Engine game){
        for(ModeManager m : modeList){
            if(m.status){
                if(m.timer == m.length){
                    m.switchStatus(game);
                    continue;
                }
                m.timer += 1;
            }
        }
    }

    /**
    * mode logic(e.g command ghosts)
    * @param game Engine object
    */
    public abstract void logic(Engine game);

    /**
    * mode logic(e.g command ghosts)
    * @param game Engine object
    * @param t the mode to be triggered
    */
    public static void trigger(Engine game, ModeType t){
        for(ModeManager m : modeList){
            if(m.type == t){
                if(m.status){
                    m.save.load(game);
                }
                reset(game);
                m.switchStatus(game);
            }
        }
    }

    /**
    * switch bewteen on and off
    * @param game Engine object
    */
    private void switchStatus(Engine game){
        if(this.status){
            this.status = false;
            logic(game);
        }else{
            this.status = true;
            logic(game);
        }
    }

    /**
    * check if a mode is instantated
    * @param t the mode to be checked
    * @return true if instantiated
    */
    public static boolean contains(ModeType t){
        if(modeList.size() == 0)
            return false;
        for(ModeManager m : modeList){
            if(m.type == t){
                return true;
            }
        }
        return false;
    }

    /**
    * instantiate a panic mode(note: only one can be instantiated)
    * @param length the duration of the mode
    */
    public static void panic(int length){
        if(contains(ModeType.panic)){
            return;
        }
        modeList.add(new Panic(length));
    }

    /**
    * instantiate a soda mode(note: only one can be instantiated)
    * @param length the duration of the mode
    */
    public static void sodaTime(int length){
        if(contains(ModeType.sodaTime)){
            return;
        }
        modeList.add(new SodaTime(length));
    }

    /**
    * rest all mode to off-mode
    * @param game Engine object
    */
    public static void reset(Engine game){
        for(ModeManager m : modeList){
            m.status = false;
            m.timer = 0;
            m.save = new Saver();
            for(Ghost gs: game.gs){
                gs.toNormal();
            }
        }
    }

}

class Panic extends ModeManager{
    public Panic(int length){
        super(length);
        this.type = ModeType.panic;
    }

    public void logic(Engine game){
        if(status){
            gsRandom = true;
            for(Ghost g : game.gs){
                if(g.alive){
                    g.sprite = g.img.get(1);
                    g.panic = true;
                } 
            }
            this.save.save(game);
        }else{
            gsRandom = false;
            for(Ghost g : game.gs){
                if (g.alive){
                    g.sprite = g.img.get(0);
                    g.panic = false;
                }
            }
            this.save.load(game);
            this.timer = 0;

        }
    }
}

class SodaTime extends ModeManager{
    public SodaTime(int length){
        super(length);
        this.type = ModeType.sodaTime;
    }

    public void logic(Engine game){
        if(status){
            gsRandom = true;
            for(Ghost g : game.gs){
                if(g.alive){
                    g.sprite = g.img.get(2);
                    g.invisible = true;
                }
                
            }
            this.save.save(game);
        }else{
            gsRandom = false;
            for(Ghost g : game.gs){
                if (g.alive){
                    g.sprite = g.img.get(0);
                    g.invisible = false;
                }
            }
            this.save.load(game);
            this.timer = 0;

        }
    }
}

