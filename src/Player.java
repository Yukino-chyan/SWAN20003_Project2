import bagel.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.*;

public class Player {
    private Image playerLeft;
    private Image playerRight;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private int coin,speed;
    private double health,hurtPerFrame;
    private Rectangle bounds;
    private int win = 0;
    private int key = 0;
    private int weaponLevel = 0;
    private int bulletFreq,coolDown;
    public double bulletSpeed;
    private int killCoin,destroyBasketCoin;
    private double hurtPerShoot;
    private double initialX,initialY;
    private Messege messegeCoin,messegeHealth,messegeKey,messegeWeaponLevel;
    private Point playerPos;
    private Boolean hasChosen = false;
    public Player (
            Point playerPos,int coin,int health,int speed,
            Properties GAME_PROPS,Properties MESSAGE_PROPS,Image playerLeft,Image playerRight,
            double hurtPerFrame,int killCoin) {
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
        this.playerPos = playerPos;
        this.coin = coin;
        this.health = health;
        this.speed = speed;
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        messegeCoin = new Messege(
                MESSAGE_PROPS.getProperty("coinDisplay"),
                new Font(GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))),
                IOUtils.parseCoords(GAME_PROPS.getProperty("coinStat")), coin, false
        );
        messegeHealth = new Messege(
                MESSAGE_PROPS.getProperty("healthDisplay"),
                new Font(GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))),
                IOUtils.parseCoords(GAME_PROPS.getProperty("healthStat")), health, false
        );
        messegeKey = new Messege(
                MESSAGE_PROPS.getProperty("keyDisplay"),
                new Font(GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))),
                IOUtils.parseCoords(GAME_PROPS.getProperty("keyStat")), key, false
        );
        messegeWeaponLevel = new Messege(
                MESSAGE_PROPS.getProperty("weaponDisplay"),
                new Font(GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))),
                IOUtils.parseCoords(GAME_PROPS.getProperty("weaponStat")), weaponLevel, false
        );
        this.hurtPerFrame = hurtPerFrame;
        this.killCoin = killCoin;
        this.initialX = playerPos.x;
        this.initialY = playerPos.y;
        this.bulletFreq = Integer.parseInt(GAME_PROPS.getProperty("bulletFreq"));
        this.coolDown = bulletFreq;
        this.bulletSpeed = Double.parseDouble(GAME_PROPS.getProperty("bulletSpeed"));
        this.hurtPerShoot = Double.parseDouble(GAME_PROPS.getProperty("weaponStandardDamage"));
        this.destroyBasketCoin = Integer.parseInt(GAME_PROPS.getProperty("basketCoin"));
    }
    public void show(Input input){
        if(input.getMouseX()>playerPos.x){
            playerRight.draw(playerPos.x,playerPos.y);
        }
        else if(input.getMouseX()<playerPos.x){
            playerLeft.draw(playerPos.x,playerPos.y);
        }
        messegeCoin.show();
        messegeHealth.show();
        messegeKey.show();
        messegeWeaponLevel.show();
    }
    //The move logic: when player move, use this method
    public void move(Input input, List<Wall> walls,Basket tmpBasket,Table tmpTable,Doors priDoor, Doors secDoor,Boolean gateDelay){
        double dx = 0, dy = 0;
        if (input.isDown(Keys.W)) dy -= speed;
        if (input.isDown(Keys.S)) dy += speed;
        if (input.isDown(Keys.A)) dx -= speed;
        if (input.isDown(Keys.D)) dx += speed;
        if (dx != 0){
            double old = playerPos.x;
            setterPosx(old + dx);
            setterBounds(getPlayerPos(input));
            for (Wall wall : walls){
                if (wall.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                    break;
                }
            }
            if(tmpBasket.isAlive()){
                if(tmpBasket.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(tmpTable.isAlive()){
                if(tmpTable.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!priDoor.getOpen() && gateDelay){
                if(priDoor.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!secDoor.getOpen() && gateDelay){
                if(secDoor.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            clampX(input);
        }
        if (dy != 0){
            double old = playerPos.y;
            setterPosy(old + dy);
            setterBounds(getPlayerPos(input));
            for (Wall wall : walls){
                if (wall.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                    break;
                }
            }
            if(tmpBasket.isAlive()) {
                if(tmpBasket.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(tmpTable.isAlive()) {
                if(tmpTable.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!priDoor.getOpen() && gateDelay){
                if(priDoor.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!secDoor.getOpen() && gateDelay){
                if(secDoor.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            clampY(input);
        }
    }
    public void move(Input input,Doors priDoor, Doors secDoor,Boolean gateDelay){
        double dx = 0, dy = 0;
        if (input.isDown(Keys.W)) dy -= speed;
        if (input.isDown(Keys.S)) dy += speed;
        if (input.isDown(Keys.A)) dx -= speed;
        if (input.isDown(Keys.D)) dx += speed;
        if (dx != 0){
            double old = playerPos.x;
            setterPosx(old + dx);
            setterBounds(getPlayerPos(input));
            if(!priDoor.getOpen() && gateDelay){
                if(priDoor.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!secDoor.getOpen() && gateDelay){
                if(secDoor.clash(this)){
                    setterPosx(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            clampX(input);
        }
        if (dy != 0){
            double old = playerPos.y;
            setterPosy(old + dy);
            setterBounds(getPlayerPos(input));
            if(!priDoor.getOpen() && gateDelay){
                if(priDoor.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            if(!secDoor.getOpen() && gateDelay){
                if(secDoor.clash(this)){
                    setterPosy(old);
                    setterBounds(getPlayerPos(input));
                }
            }
            clampY(input);
        }
    }
    //when move in x-axis, this ensure that players do not walk off the screen.
    private void clampX(Input input) {
        Rectangle b = getterBounds();
        double halfW = (b.right() - b.left()) / 2.0;
        if (b.left() < 0) {
            setterPosx(halfW);
            setterBounds(getPlayerPos(input));
        }
        else if (b.right() > Window.getWidth()) {
            setterPosx(Window.getWidth() - halfW);
            setterBounds(getPlayerPos(input));
        }
    }
    //when move in y-axis, this ensure that players do not walk off the screen.
    private void clampY(Input input) {
        Rectangle b = getterBounds();
        double halfH = (b.bottom() - b.top()) / 2.0;
        if (b.top() < 0) {
            setterPosy(halfH);
            setterBounds(getPlayerPos(input));
        } else if (b.bottom() > Window.getHeight()) {
            setterPosy(Window.getHeight() - halfH);
            setterBounds(getPlayerPos(input));
        }
    }
    public void setterPosx (double x){
        playerPos = new Point(x,playerPos.y);
    }
    public void setterPosy (double y){
        playerPos = new Point(playerPos.x,y);
    }
    public void setterBounds(Rectangle bounds){
        this.bounds = bounds;
    }
    public Rectangle getterBounds(){
        return bounds;
    }
    public int getWin(){
        return win;
    }
    public void setWin(int win){ this.win = win; }
    public Rectangle getPlayerPos(Input input){
        if(input.getMouseX()>playerPos.x){
            return playerLeft.getBoundingBoxAt(playerPos);
        }
        else return playerRight.getBoundingBoxAt(playerPos);
    }
    public void injured(){
        health -= hurtPerFrame;
        messegeHealth.setNum(health);
    }
    public void injured(double num){
        health -= num;
        messegeHealth.setNum(health);
    }
    public void getKill(int num){
        coin = coin + num + killCoin;
        messegeCoin.setNum(coin);
    }
    public void getTreasure(TreasureBox tmp){
        coin += tmp.getNum();
        messegeCoin.setNum(coin);
    }
    public double getPosX(){
        return playerPos.x;
    }
    public double getPosY(){
        return playerPos.y;
    }
    public double getHealth(){
        return health;
    }
    public void reset(){
        coin = 0;
        health = 100.0;
        messegeCoin.setNum(coin);
        messegeHealth.setNum(health);
        win = 0; key = 0; messegeKey.setNum(key);
        setterPosx(initialX);
        setterPosy(initialY);
        playerLeft = new Image("res/player_left.png");
        playerRight = new Image("res/player_right.png");
        hasChosen = false;
        weaponLevel = 0; messegeWeaponLevel.setNum(weaponLevel);
    }
    public void setMarine(){
        hasChosen = true;
        playerLeft = new Image("res/marine_left.png");
        playerRight = new Image("res/marine_right.png");
        hurtPerFrame = 0;
        killCoin = 0;
    }
    public void setRobot() {
        hasChosen = true;
        playerLeft = new Image("res/robot_left.png");
        playerRight = new Image("res/robot_right.png");
        hurtPerFrame = Double.parseDouble(GAME_PROPS.getProperty("riverDamagePerFrame"));
        killCoin = Integer.parseInt(GAME_PROPS.getProperty("robotExtraCoin"));
    }
    public int getKey(){ return key; }
    public void gainKey(){ key++; messegeKey.setNum(key); }
    public void setKey(int key){ this.key = key; messegeKey.setNum(key); }
    public void shotCoolDown(){
        if(coolDown != 0) coolDown -- ;
    }
    public double getHurtPerShot(){
        return hurtPerShoot;
    }
    public double getBulletSpeed(){
        return bulletSpeed;
    }
    public double getHurtPerFrame(){ return hurtPerFrame; }
    public int getCoin() { return coin; }
    public void useCoin(int coin){ this.coin -= coin; messegeCoin.setNum(this.coin); }
    public void gainHealth(double health){ this.health += health; messegeHealth.setNum(this.health); }
    public void gainCoin (int Coin) { this.coin += Coin; messegeCoin.setNum(this.coin); }
    public int getWeaponLevel(){ return weaponLevel; }
    public void setHurtPerShoot(double hurtPerShoot){
        weaponLevel ++; this.hurtPerShoot = hurtPerShoot;
        messegeWeaponLevel.setNum(weaponLevel);
    }
    public void shot(Room room, Input input){
        if(coolDown != 0) return ;
        else coolDown = bulletFreq;
        room.shotBullet(this,input);
    }
    public Boolean getHasChosen(){ return hasChosen; }
}
