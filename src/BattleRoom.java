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
/**
 * BattleRoom class: A combat room containing enemies, obstacles, doors and projectiles.
 */
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
    private Boolean haveKey = false;
    private Basket basket;
    private Table table;
    private Key key;
    /**
     * This is the constructor of the BattleRoom object.
     * @param priDoor The primary door of this room.
     * @param secDoor The secondary door of this room.
     * @param treasureBoxes The treasure boxes in this room.
     * @param keyBulletKin The key-carrying enemy.
     * @param walls The list of walls.
     * @param rivers The list of rivers.
     * @param bulletKins The list of BulletKin enemies.
     * @param ashenBulletKins The list of AshenBulletKin enemies.
     * @param basket The destructible basket object.
     * @param table The destructible table object.
     */
    public BattleRoom(Doors priDoor,Doors secDoor,List<TreasureBox> treasureBoxes,KeyBulletKin keyBulletKin,List<Wall> walls,List<River> rivers,List<BulletKin> bulletKins,List<AshenBulletKin> ashenBulletKins,Basket basket, Table table) {
        this.priDoor = priDoor;
        this.secDoor = secDoor;
        this.treasureBoxes = treasureBoxes;
        this.keyBulletKin = keyBulletKin;
        this.walls.addAll(walls);
        this.rivers.addAll(rivers);
        this.bulletKins = bulletKins;
        this.ashenBulletKins = ashenBulletKins;
        this.basket = basket;
        this.table = table;
    }
    /**
     * Get the primary door of this room.
     * @return The primary door.
     */
    public Doors getPriDoors() { return priDoor; }
    /**
     * Get the secondary door of this room.
     * @return The secondary door.
     */
    public Doors getSecDoors() { return secDoor; }
    /**
     * Get the walls in this room.
     * @return The list of walls.
     */
    public List<Wall> getWalls() { return walls; }
    /**
     * Draw the room contents including doors, obstacles, enemies and projectiles.
     * @param input The current keyboard/mouse input.
     */
    public void show(Input input){
        // Draw background and static props
        bg.draw(width/2.0,height/2.0);
        priDoor.show();
        secDoor.show();
        if(basket.isAlive()) basket.show();
        if(table.isAlive()) table.show();
        // Draw interactables and environment
        for(TreasureBox t:treasureBoxes) if(!t.getOpen()) t.show();
        for(Wall w:walls) { w.show(); }
        for(River r : rivers) { r.show(); }
        // Draw projectiles
        for(Bullet b : bullets) { b.show(); }
        for(Fireball f : fireballs) { f.show(); }
        // Draw enemies only when combat is enabled (after gates close)
        if(gateDelay) {
            for(BulletKin bk : bulletKins) { bk.show(); }
            for(AshenBulletKin k : ashenBulletKins) k.show();
            if(keyBulletKin.isAlive()) keyBulletKin.show();
        }
        // Draw dropped key if available
        if(haveKey) key.show();
    }
    /**
     * Test collisions relevant to room transitions and hazards.
     * @param player The player to test.
     * @return 1 enter priDoor, 3 enter secDoor, 4 river hurt, 5 treasure nearby, 6 enemy contact, 0 none.
     */
    public int clashTest(Player player) {
        // Pick up key if the player touches it
        if(haveKey){
            if(key.clash(player) && key.isAlive()){
                player.gainKey(); haveKey = false; key.dead();
            }
        }
        // Gate delay starts after leaving both door triggers once
        if(!priDoor.clash(player) && !secDoor.clash(player)) gateDelay = true;
        // Allow transition only when door is open and gateDelay true
        if(priDoor.clash(player) && priDoor.getOpen() && gateDelay ) return 1;
        if(secDoor.clash(player) && secDoor.getOpen() && gateDelay) return 3;
        // Environmental hazard: rivers
        for(River r : rivers) { if(r.clash(player)) return 4; }
        // Near treasure box
        for(TreasureBox t:treasureBoxes) { if(t.clash(player)) return 5; }
        // Touching any enemy
        for(AshenBulletKin a:ashenBulletKins) { if(a.clash(player) && a.isAlive() ) return 6; }
        for(BulletKin b:bulletKins) { if(b.clash(player) && b.isAlive() ) return 6; }
        if(keyBulletKin.clash(player) && keyBulletKin.isAlive()) return 6;
        return 0;
    }
    /**
     * Get the treasure boxes in this room.
     * @return The list of treasure boxes.
     */
    public List<TreasureBox> getTreasureBoxes() { return treasureBoxes; }
    /**
     * Handle player entry position based on which door is used.
     * @param player The entering player.
     * @param backflag true if entering from secondary door.
     */
    public void entry(Player player,Boolean backflag) {
        // Snap player to the appropriate door spawn point
        if(backflag) {
            player.setterPosx(secDoor.getPosX());
            player.setterPosy(secDoor.getPosY());
        }
        else {
            player.setterPosx(priDoor.getPosX());
            player.setterPosy(priDoor.getPosY());
        }
        // Reset gate state on entry
        gateDelay = false;
    }
    /**
     * Reset room state to initial values.
     */
    public void reset(){
        // Reset doors and enemies
        priDoor.reset();
        secDoor.reset();
        keyBulletKin.reset();
        for(BulletKin bk : bulletKins) { bk.reset(); }
        for(AshenBulletKin k : ashenBulletKins) {k.reset(); }
        // Reset interactables and clear projectiles
        for(TreasureBox t:treasureBoxes) { t.reset(); }
        fireballs.clear();
        bullets.clear();
        // Reset flags and destructibles
        gateDelay = false;
        passed = false; haveKey = false;
        basket.reset(); table.reset();
    }
    /**
     * Let enemies fire fireballs toward the player according to their cooldowns.
     * @param player The player target.
     */
    public void shotFireball(Player player){
        // BulletKin shooting logic with cooldown
        for(BulletKin bk:bulletKins) {
            if(bk.isAlive()){
                if(bk.getCoolDown() == 0){
                    fireballs.add(bk.shot(player));
                    bk.setCoolDown(bk.getShotSpeed());
                }
                else bk.setCoolDown(bk.getCoolDown() - 1);
            }
        }
        // AshenBulletKin shooting logic with cooldown
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
    /**
     * Fire a player bullet toward the mouse cursor.
     * @param player The player who shoots.
     * @param input The current input.
     */
    public void shotBullet(Player player,Input input){
        // Compute normalized direction from player to mouse and scale by bullet speed
        Point playerPos =  new Point(player.getPosX(),player.getPosY());
        Point mousePos = input.getMousePosition();
        double speedX,speedY,disX,disY;
        disX = mousePos.x - player.getPosX();
        disY = mousePos.y - player.getPosY();
        speedX = player.getBulletSpeed() * disX / sqrt(disX * disX + disY * disY);
        speedY = player.getBulletSpeed() * disY / sqrt(disX * disX + disY * disY);
        bullets.add(new Bullet(playerPos,speedX,speedY,player.getHurtPerShot()));
    }
    /**
     * Move dynamic entities (enemies and projectiles) each frame when combat is active.
     */
    public void move(){
        if(!gateDelay) return ;
        keyBulletKin.move();
        for(Fireball fb:fireballs) { fb.move(); }
        for(Bullet bb:bullets) { bb.move(); }
    }
    /**
     * Resolve projectile collisions with doors, walls, enemies, objects and player.
     * @param player The player to test against enemy fireballs.
     */
    public void projectilesClashTest(Player player){
        // --- Enemy fireballs ---
        for(int i = 0; i < fireballs.size(); i++) {
            // Blocked by closed doors
            if(priDoor.clashFireball(fireballs.get(i))){
                fireballs.remove(fireballs.get(i)); continue;
            }
            if(secDoor.clashFireball(fireballs.get(i))){
                fireballs.remove(fireballs.get(i)); continue;
            }
            // Hit player
            if(fireballs.get(i).clash(player)) {
                player.injured(fireballs.get(i).getDamage());
                fireballs.remove(i);
                i--; continue;
            }
            // Hit any wall
            for(int j = 0; j < walls.size(); j++) {
                if(walls.get(j).clashFireball(fireballs.get(i))){
                    fireballs.remove(fireballs.get(i));
                    i--;
                    break;
                }
            }
        }
        // --- Player bullets ---
        for(int i = 0; i < bullets.size(); i++) {
            int flag = 0;
            // Blocked by closed doors
            if(priDoor.clashBullet(bullets.get(i))){
                bullets.remove(bullets.get(i)); continue;
            }
            if(secDoor.clashBullet(bullets.get(i))){
                bullets.remove(bullets.get(i)); continue;
            }
            // Hit key enemy: drop key
            if(keyBulletKin.clashBullet(bullets.get(i)) && keyBulletKin.isAlive()){
                keyBulletKin.dead(); key = new Key(keyBulletKin.getPos()); haveKey = true;
                bullets.remove(bullets.get(i)); continue;
            }
            // Hit destructible props
            if(basket.clashBullet(bullets.get(i)) && basket.isAlive()){
                basket.dead(); player.gainCoin(basket.getCoin());
                bullets.remove(bullets.get(i)); continue;
            }
            if(table.clashBullet(bullets.get(i)) && table.isAlive()){
                table.dead();
                bullets.remove(bullets.get(i)); continue;
            }
            // Hit any wall
            for(int j = 0; j < walls.size(); j++) {
                if(walls.get(j).clashBullet(bullets.get(i))){
                    bullets.remove(bullets.get(i));
                    i--; flag = 1;
                    break;
                }
            }
            if(flag == 1) continue;
            // Hit a BulletKin
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
            // Hit an AshenBulletKin
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
    /**
     * Get whether the room has been cleared.
     * @return true if cleared.
     */
    public Boolean getPassed(){ return passed; }
    /**
     * Check victory condition and open doors when all enemies are defeated.
     * @param player The player to reward with a win count.
     */
    public void winTest(Player player){
        // Count total enemies (including key holder) and how many are dead
        int num = ashenBulletKins.size() + bulletKins.size() + 1;
        int numDead = 0;
        for(BulletKin bk:bulletKins) { if(!bk.isalive) numDead++; }
        for(AshenBulletKin abk:ashenBulletKins) { if(!abk.isalive) numDead++; }
        if(!keyBulletKin.isalive) { numDead++; }
        // If all defeated, open doors and mark room as passed
        if(num == numDead) { priDoor.setterOpen(); secDoor.setterOpen(); player.setWin(player.getWin() + 1); passed = true; }
    }
    /**
     * Remove all projectiles in this room.
     */
    public void clean(){ fireballs.clear(); bullets.clear(); }
    /**
     * Get the basket object.
     * @return The basket.
     */
    public Basket getBasket() { return basket; }
    /**
     * Get the table object.
     * @return The table.
     */
    public Table getTable() { return table; }
    /**
     * Get whether gate delay is active (combat phase).
     * @return true if active.
     */
    public Boolean getGateDelay(){ return gateDelay; }
}
