import bagel.Input;
import bagel.Keys;
import bagel.Image;
import bagel.Font;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import bagel.Window;

import static java.lang.Math.sqrt;

public class EndRoom implements Room {
    private Boolean gateDelay = false;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private Doors doorToB;
    private Restart_Area restartArea;
    private Messege congratesMessege,gameoverMessege;
    boolean winFlag = false;
    private List<Bullet> bullets =  new ArrayList<>();
    public EndRoom(Doors doorToB,Point restartPos,Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.doorToB = doorToB;
        this.restartArea = new Restart_Area(restartPos);
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        congratesMessege = new Messege(
                MESSAGE_PROPS.getProperty("gameEnd.won"),
                new Font (GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))),
                new Point (Window.getWidth()/2.0,Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true
        );
        gameoverMessege = new Messege(
                MESSAGE_PROPS.getProperty("gameEnd.lost"),
                new Font (GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))),
                new Point (Window.getWidth()/2.0,Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true
        );
    }
    public void show(Input input) {
        bg.draw(width/2,height/2);
        restartArea.show();
        doorToB.show();
        if(!winFlag) gameoverMessege.show();
        else congratesMessege.show();
        for(Bullet bullet : bullets) { bullet.show(); }
    }
    public void setWinFlag(boolean winFlag) {
        this.winFlag = winFlag;
        if(winFlag) doorToB.setterOpen();
    }
    public Doors getPriDoors() { return doorToB; }
    public Doors getSecDoors() { return doorToB; }

    public int clashTest(Player player) {
        if(!doorToB.clash(player)) { gateDelay = true; }
        if(doorToB.clash(player) && gateDelay) return 1;
        if(restartArea.clash(player)) return 2;
        return 0;
    }
    public void entry(Player player,Boolean backflag) {
        player.setterPosx(doorToB.getPosX());
        player.setterPosy(doorToB.getPosY());
        gateDelay = false;
    }
    public void reset() {
        doorToB.reset();
        gateDelay = false;
    }
    public void clean(){ bullets.clear(); }
    public void shotBullet(Player player,Input input){
        Point playerPos =  new Point(player.getPosX(),player.getPosY());
        Point mousePos = input.getMousePosition();
        double speedX,speedY,disX,disY;
        disX = mousePos.x - player.getPosX();
        disY = mousePos.y - player.getPosY();
        speedX = player.getBulletSpeed() * disX / sqrt(disX * disX + disY * disY);
        speedY = player.getBulletSpeed() * disY / sqrt(disX * disX + disY * disY);
        bullets.add(new Bullet(playerPos,speedX,speedY,player.getHurtPerShot()));
    }
    public void move(){ for(Bullet bb:bullets) { bb.move(); } }
}
