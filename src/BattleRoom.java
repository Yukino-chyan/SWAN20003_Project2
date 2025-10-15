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
import bagel.util.Vector2;

import static java.lang.Math.sqrt;

public class BattleRoom implements Room {
    private Boolean gateDelay = false;
    private Doors priDoor,secDoor;
    private List<TreasureBox> treasureBoxes;
    private KeyBulletKin keyBulletKin;
    private final List<Wall> walls = new ArrayList<>();
    private final List<River> rivers = new ArrayList<>();
    private List<BulletKin> bulletKins;
    private List<AshenBulletKin> ashenBulletKins;
    private List<Fireball> fireballs = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private Boolean passed = false;
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
        for(Bullet b : bullets) { b.show(); }
        for(Fireball f : fireballs) { f.show(); }
        for(BulletKin bk : bulletKins) { bk.show(); }
        for(AshenBulletKin k : ashenBulletKins) k.show();
        if(gateDelay && keyBulletKin.isAlive()) keyBulletKin.show();
    }
    public int clashTest(Player player) {
        if(!priDoor.clash(player) && !secDoor.clash(player)) gateDelay = true;
        if(priDoor.clash(player) && priDoor.getOpen() && gateDelay ) return 1;
        if(secDoor.clash(player) && secDoor.getOpen() && gateDelay) return 3;
        for(River r : rivers) { if(r.clash(player)) return 4; }
        for(TreasureBox t:treasureBoxes) { if(t.clash(player)) return 5; }
        for(AshenBulletKin a:ashenBulletKins) { if(a.clash(player) && a.isAlive() ) return 6; }
        for(BulletKin b:bulletKins) { if(b.clash(player) && b.isAlive() ) return 6; }
        if(keyBulletKin.clash(player) && keyBulletKin.isAlive()) return 6;
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
        for(BulletKin bk : bulletKins) { bk.reset(); }
        for(AshenBulletKin k : ashenBulletKins) {k.reset(); }
        for(TreasureBox t:treasureBoxes) { t.reset(); }
        fireballs.clear();
        bullets.clear();
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
    public void move(){
        keyBulletKin.move();
        for(Fireball fb:fireballs) { fb.move(); }
        for(Bullet bb:bullets) { bb.move(); }
    }
    public void fireballClashTest(Player player){
        for(int i = 0; i < fireballs.size(); i++) {
            if(priDoor.clashFireball(fireballs.get(i))){
                fireballs.remove(fireballs.get(i)); continue;
            }
            if(secDoor.clashFireball(fireballs.get(i))){
                fireballs.remove(fireballs.get(i)); continue;
            }
            if(fireballs.get(i).clash(player)) {
                player.injured(fireballs.get(i).getDamage());
                fireballs.remove(i);
                i--; continue;
            }
            for(int j = 0; j < walls.size(); j++) {
                if(walls.get(j).clashFireball(fireballs.get(i))){
                    fireballs.remove(fireballs.get(i));
                    i--;
                    break;
                }
            }
        }
        for(int i = 0; i < bullets.size(); i++) {
            int flag = 0;
            if(priDoor.clashBullet(bullets.get(i))){
                bullets.remove(i); continue;
            }
            if(secDoor.clashBullet(bullets.get(i))){
                bullets.remove(i); continue;
            }
            if(keyBulletKin.clashBullet(bullets.get(i)) && keyBulletKin.isAlive()){
                keyBulletKin.dead(); player.gainKey();
                bullets.remove(i); continue;
            }
            for(int j = 0; j < walls.size(); j++) {
                if(walls.get(j).clashBullet(bullets.get(i))){
                    bullets.remove(bullets.get(i));
                    i--; flag = 1;
                    break;
                }
            }
            if(flag == 1) continue;
            for(int j = 0 ; j < bulletKins.size(); j++) {
                if(!bulletKins.get(j).isalive) continue;
                if(bulletKins.get(j).clashBullet(bullets.get(i))){
                    bulletKins.get(j).injured(bullets.get(i).getDamage(),player);
                    bullets.remove(bullets.get(i));
                    i--; flag = 1;
                    break;
                }
            }
            if(flag == 1) continue;
            for(int j = 0 ; j < ashenBulletKins.size(); j++) {
                if(!ashenBulletKins.get(j).isalive) continue;
                if(ashenBulletKins.get(j).clashBullet(bullets.get(i))){
                    ashenBulletKins.get(j).injured(bullets.get(i).getDamage(),player);
                    bullets.remove(bullets.get(i));
                    i--; flag = 1;
                    break;
                }
            }
            if(flag == 1) continue;
        }
    }
    public Boolean getPassed(){ return passed; }
    public void winTest(Player player){
        int num = ashenBulletKins.size() + bulletKins.size() + 1;
        int numDead = 0;
        for(BulletKin bk:bulletKins) { if(!bk.isalive) numDead++; }
        for(AshenBulletKin abk:ashenBulletKins) { if(!abk.isalive) numDead++; }
        if(!keyBulletKin.isalive) { numDead++; }
        if(num == numDead) { priDoor.setterOpen(); secDoor.setterOpen(); player.setWin(player.getWin() + 1); passed = true; }
    }
    public void clean(){ bullets.clear(); }
}
