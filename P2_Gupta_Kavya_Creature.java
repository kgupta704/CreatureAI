import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a creature that moves toward treats and eats them.
 * 
 * @author Kavya Gupta 
 * @version 3/7/2023
 */
public class P2_Gupta_Kavya_Creature extends Actor {
    int speed = 2;
    Treat target = null;
    boolean wander = false;
    int wanderDirec;

    public void act() {
        if(isTouching(Wall.class)) {
            System.out.println("bad start");
            //setLocation(startX, startY);
        }
        int startX = getX();
        int startY = getY();
        if(closestReachTreat() != null) {
            wander = false;

            if(target == null) {
                target = closestReachTreat();
            } else {
                turnTowards(target.getX(), target.getY());
                move(speed);
                setRotation(0);
            }

            Treat possibleTreat = (Treat)getOneIntersectingObject(Treat.class);
            if(possibleTreat != null) {
                getWorld().removeObject(possibleTreat);
                target = null;
            }
        } else {
            int randNum = Greenfoot.getRandomNumber(100);
            if(!wander || randNum == 0) {
                wanderDirec = returnDirection();
            }
            
            if(!directionIsClear(wanderDirec)) { 
                wanderDirec = returnDirection();
            }
            setRotation(wanderDirec);
            move(1);
            setRotation(0);
            wander = true;
            //if(getOneIntersectingObject(Wall.class) != null) {
                //dontTouchWall(wanderDirec);
            //}
        }
        setRotation(0);
        if(isTouching(Wall.class)) {
            System.out.println("bad");
            //setLocation(startX, startY);
        }
    }

    public void dontTouchWall(int startRot) {
        setRotation(0);
        int[] rotations = new int[]{270, 0, 90, 180};
        for(int i = 0; i < rotations.length; i++) {
            if (!directionIsClear(rotations[i])) {
                setRotation(rotations[i]);
                move(-1);
                setRotation(0);
            }
        }
        setRotation(startRot);

    }

    public int returnDirection() {
        ArrayList<Integer> openDirections = new ArrayList<>();
        int[] rotations = new int[]{270, 0, 90, 180};
        for(int i = 0; i < rotations.length; i++) {
            if (directionIsClear(rotations[i]))
                openDirections.add(rotations[i]);
        }

        int direction = openDirections.get(Greenfoot.getRandomNumber(openDirections.size()));
        return direction;
    }

    public int numMovesToReach(Treat t) {
        int startX = getX();
        int startY = getY();
        int steps = 0;
        int startRot = getRotation();

        while(getOneIntersectingObject(Treat.class) != t && getOneIntersectingObject(Wall.class) == null) {
            turnTowards(t.getX(), t.getY());
            move(2);
            setRotation(startRot);
            steps++;
        }

        if(getOneIntersectingObject(Wall.class) != null) {
            setLocation(startX, startY);
            return -1;
        } else {
            setLocation(startX, startY);
            return steps;
        }

    }

    public Treat closestReachTreat() {
        Treat closest = null;
        List<Treat> treats = getWorld().getObjects(Treat.class);
        int steps;
        if(treats.isEmpty()) 
            steps = -1;
        else 
            steps = numMovesToReach(treats.get(0));

        if(steps != -1) 
            closest = treats.get(0);

        for(int i=0; i<treats.size(); i++) {
            int moves = numMovesToReach(treats.get(i));
            if(moves != -1) {
                if(steps == -1 || moves < steps) {
                    steps = moves;
                    closest = treats.get(i);
                }
            }
        }

        return closest;
    }

    public boolean directionIsClear(int rotation) {
        //System.out.println(rotation);
        int startRot = getRotation();
        setRotation(0);
        int direction = -1;
        if(rotation == 270) direction = 0;
        else if(rotation == 90) direction = 2;
        else if(rotation == 0) direction = 1;
        else if(rotation == 180) direction = 3;
        
        int w = getImage().getWidth();
        int h = getImage().getHeight();

        // 0 for north, 1 for east, 2 for south, 3 for west, 4 for north/south
        int[] dx = new int[]{-w/2, w/2+1, -w/2, -(w/2+1), w/2};
        int[] dy = new int[]{-(h/2+1), -h/2, h/2+1, -h/2, h/2};

        setRotation(startRot);
        
        boolean clear = true;
        int counter = 0;
        
        //north or south
        if(direction==0 || direction==2) {
            for(int i = dx[direction]; i<= dx[4]; i++) {
                i+= counter;
                clear = getOneObjectAtOffset(i, dy[direction], Wall.class) == null;
                if(!clear) {
                    break;
                }
            }
        } else {
            //east or west
            for(int i = dy[direction]; i<= dy[4]; i++) {
                i+= counter;
                clear = getOneObjectAtOffset(dx[direction], i, Wall.class) == null;
                if(!clear) {
                    break;
                }
            }
        }

        return clear;

    }
}



