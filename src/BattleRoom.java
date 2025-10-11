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
    private KeyBulletKin keyBulletKin;
    private final List<Wall> walls = new ArrayList<>();
    private final List<River> rivers = new ArrayList<>();
    private List<BulletKin> bulletKins;
    private List<AshenBulletKin> ashenBulletKins;
    private List<Fireball> fireballs =  new ArrayList<>();
    public BattleRoom(
            Doors priDoor,Doors secDoor,
            List<TreasureBox> treasureBoxes,KeyBulletKin keyBulletKin,
            List<Wall> walls,List<River> rivers,
            List<BulletKin> bulletKins,List<AshenBulletKin> ashenBulletKins) {
        this.priDoor = priDoor;
        this.secDoor = secDoor;
        this.treasureBoxes = treasureBoxes;
        this.keyBulletKin = keyBulletKin;
        this.walls.addAll(walls);
        this.rivers.addAll(rivers);
        this.bulletKins = bulletKins;
        this.ashenBulletKins = ashenBulletKins;
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
        for(TreasureBox t:treasureBoxes) if(!t.getOpen()) t.show();
        for(Wall w:walls) { w.show(); }
        for(River r : rivers) { r.show(); }
        for(Fireball f : fireballs) { f.show(); }
        for(BulletKin bk : bulletKins) { bk.show(); }
        for(AshenBulletKin k : ashenBulletKins) k.show();
        if(gateDelay && keyBulletKin.isAlive()) keyBulletKin.show();
    }
    public int clashTest(Player player) {
        if(!priDoor.clash(player) && !secDoor.clash(player)) gateDelay = true;
        if(priDoor.clash(player) && priDoor.getOpen() && gateDelay ) return 1;
        if(secDoor.clash(player) && secDoor.getOpen() && gateDelay) return 3;
        if(keyBulletKin.clash(player) && keyBulletKin.isAlive()) {
            keyBulletKin.dead(); priDoor.setterOpen(); secDoor.setterOpen();
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
        keyBulletKin.reset();
        for(TreasureBox t:treasureBoxes) { t.reset(); }
        gateDelay = false;
    }
    public void shot(Player player){
        for(BulletKin bk:bulletKins) {
            if(bk.isAlive()){
                if(bk.getCoolDown() == 0){
                    fireballs.add(bk.shot(player));
                    bk.setCoolDown(bk.getShotSpeed());
                }
                else bk.setCoolDown(bk.getCoolDown() - 1);
            }
        }
        for(AshenBulletKin k:ashenBulletKins) {
            if(k.isAlive()){
                if(k.getCoolDown() == 0){
                    fireballs.add(k.shot(player));
                    k.setCoolDown(k.getShotSpeed());
                }
                else  k.setCoolDown(k.getCoolDown() - 1);
            }
        }
    }
    public void move(){
        keyBulletKin.move();
        for(Fireball fb:fireballs) { fb.move(); }
    }
}
