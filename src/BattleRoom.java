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
import bagel.util.Rectangle;

public class BattleRoom implements Room {
    private Boolean gateDelay = false;
    private Doors priDoor,secDoor;
    private List<TreasureBox> treasureBoxes;
    private KeyBulletKin enemy;
    private final List<Wall> walls = new ArrayList<>();
    private final List<River> rivers = new ArrayList<>();
    BattleRoom(Doors priDoor,Doors secDoor,List<TreasureBox> treasureBoxes,KeyBulletKin enemy,List<Wall> walls,List<River> rivers) {
        this.priDoor = priDoor;
        this.secDoor = secDoor;
        this.treasureBoxes = treasureBoxes;
        this.enemy = enemy;
        this.walls.addAll(walls);
        this.rivers.addAll(rivers);
    }
    public Doors getPriDoors() { return priDoor; }
    public Doors getSecDoors() { return secDoor; }
    public List<Wall> getWalls() {
        return walls;
    }
    public void show(Input input){
        bg.draw(width/2.0,height/2.0);
        priDoor.show();
        secDoor.show();
        if(gateDelay && enemy.isAlive()) enemy.show();
        for(TreasureBox t:treasureBoxes) if(!t.getOpen()) t.show();
        for(Wall w:walls) { w.show(); }
        for(River r : rivers) { r.show(); }
    }
    public int clashTest(Player player) {
        if(!priDoor.clash(player) && !secDoor.clash(player)) gateDelay = true;
        if(priDoor.clash(player) && priDoor.getOpen() && gateDelay ) return 1;
        if(secDoor.clash(player) && secDoor.getOpen() && gateDelay) return 3;
        if(enemy.clash(player) && enemy.isAlive()) {
            enemy.dead(); priDoor.setterOpen(); secDoor.setterOpen();
            player.setWin(player.getWin()+1);
        }
        for(River r : rivers) { if(r.clash(player)) return 4; }
        for(TreasureBox t:treasureBoxes) { if(t.clash(player)) return 5; }
        return 0;
    }
    public List<TreasureBox> getTreasureBoxes() {
        return treasureBoxes;
    }
    public void entry(Player player,Boolean backflag) {
        if(backflag) {
            player.setterPosx(secDoor.getPosX());
            player.setterPosy(secDoor.getPosY());
        }
        else {
            player.setterPosx(priDoor.getPosX());
            player.setterPosy(priDoor.getPosY());
        }
        gateDelay = false;
    }
    public void reset(){
        priDoor.reset();
        secDoor.reset();
        enemy.reset();
        for(TreasureBox t:treasureBoxes) { t.reset(); }
        gateDelay = false;
    }
    public void move(){
        enemy.move();
    }
}
