package ghost;

import processing.core.*;
import java.util.*;

/**
* sort and store grids based on their type and relative location
*/
public class GMap{
    int[] size;
    int fruitNum;
    int fruitRemain;
    List<Grid> allGrids = new ArrayList<Grid>();
    HashMap<int[],Grid> gDic = new HashMap<int[],Grid>();
    List<Grid> wallGrids = new ArrayList<Grid>();
    List<Grid> intersections = new ArrayList<Grid>();
    List<Grid> turns = new ArrayList<Grid>();
    List<Grid> deadEnds = new ArrayList<Grid>();
    List<Grid> corners = new ArrayList<Grid>();
    List<Grid> fruitGrids = new ArrayList<Grid>();
    List<Grid> specials = new ArrayList<Grid>();
    Grid playerStartPos;

    /**
    * @param ls a list of all grids
    */
    public GMap(List<Grid> ls){
        this.allGrids = ls;
        buildDic();
        readMap();
        findCorners();
    }
    /**
    * @param ls a list of all grids
    * @param size size of the map
    */

    public GMap(List<Grid> ls, int[] size){
        this.allGrids = ls;
        this.size = size;
        buildDic();
        readMap();
        findCorners();
    }
    
    /**
    * store grids and their display form in hashmap
    */
    private void buildDic(){
        for(Grid g : this.allGrids){
            gDic.put(g.locID(),g);
        }

    }

    /**
    * find corners of the map, map must be square
    */
    private void findCorners(){
        for(Grid g : this.allGrids){
            if(g.type == Basic.GridType.wall){
                this.corners.add(g);
                break;
            }
        }
        Collections.reverse(this.allGrids);
        for(Grid g : this.allGrids){
            if(g.type == Basic.GridType.wall){
                this.corners.add(g);
                break;
            } 
        }
        if(corners.size() != 2)
            return;
        int[] corner1 = corners.get(0).locID();
        int[] corner4 = corners.get(1).locID();
        int[] corner2 = {corner4[0], corner1[1]};
        int[] corner3 = {corner1[0], corner4[1]};
        this.corners.set(1, searchByLoc(corner2));
        this.corners.add(searchByLoc(corner3));
        this.corners.add(searchByLoc(corner4));

    }

    /**
    * sort and store grids based on their type
    */

    private void readMap(){
        for(Grid g : this.allGrids){
            if (g.type == Basic.GridType.wall){
                this.wallGrids.add(g);
            }
            if (g.type == Basic.GridType.fruit || g.type == Basic.GridType.sFruit){
                this.fruitGrids.add(g);
                this.fruitNum += 1;
                this.fruitRemain += 1;
            }
            if (g.type == Basic.GridType.specialItem){
                this.specials.add(g);
            }
            int t = roadType(g);
            if (t == 2){
                intersections.add(g);
            }
            if (t == 1){
                turns.add(g);
            }
            if (t == 3){
                deadEnds.add(g);
            }
        }
    }

    /**
    * @param g a grid 
    * @return the type of the grid 0:regular, 1:turn, 2:intersection 3:deadEnd 4:wall 
    */

    private int roadType(Grid g){
        if(g.getType() == Basic.GridType.wall)
            return 4;
        List<Grid> nearbys = new ArrayList<Grid>();
        List<Dir> dirs = new ArrayList<Dir>();
        int wallCount = 0;
        int[] loc = new int[2];
        Dir[] dir = {Dir.up, Dir.down, Dir.left, Dir.right};
        for(Dir d : dir){
            Grid nearby = gridNearBy(g,d);
            if(nearby != null){
                if(nearby.getType() != Basic.GridType.wall){
                    nearbys.add(nearby);
                    dirs.add(d);
                }else{wallCount += 1;}
            }
        }
        if (wallCount == 3)
            return 3;
        
        if (nearbys.size() > 2){
            return 2;
        }

        boolean isTurn = false;
        if(dirs.contains(Dir.up) || dirs.contains(Dir.down)){
            if(dirs.contains(Dir.left) || dirs.contains(Dir.right)){
                isTurn = true;
            }
        }
        if(dirs.contains(Dir.left) || dirs.contains(Dir.right)){
            if(dirs.contains(Dir.up) || dirs.contains(Dir.down)){
                isTurn =true;
            }
        }
        if(isTurn){
            return 1;
        }
        return 0;
    }

    /**
    * @param loc row and col
    * @return a grid of given loc location(row and col)
    */

    public Grid searchByLoc(int[] loc){
        for(Map.Entry e : gDic.entrySet()){
            int[] mapKey = (int[]) e.getKey();
            if(Arrays.equals(mapKey, loc)){
                return (Grid)e.getValue();
            }
        }
        return null;
    }

    /**
    * @param g a grid 
    * @param d direction to check
    * @return nearby grid
    */
    public Grid gridNearBy(Grid g, Dir d){
        if (d == Dir.up){
            int[] loc = {g.locID()[0], g.locID()[1] - 1};
            return searchByLoc(loc);
        } 
        if (d == Dir.down){
            int[] loc = {g.locID()[0], g.locID()[1] + 1};  
            return searchByLoc(loc);
        }
        if (d == Dir.left){
            int[] loc = {g.locID()[0] - 1, g.locID()[1]};
            return searchByLoc(loc);
        }
        if (d == Dir.right){
            int[] loc = {g.locID()[0] + 1, g.locID()[1]};
            return searchByLoc(loc);
        }
        return null;
    }



    public void draw(PApplet app){
        for(Grid g : this.allGrids){
            g.draw(app);
        }

    }




}