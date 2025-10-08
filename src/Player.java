import bagel.*;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.*;

public class Player {
    private final Image playerLeft = new Image("res/player_left.png");
    private final Image playerRight = new Image("res/player_right.png");
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private int coin,speed;
    private double health,hurtPerFrame;
    private Rectangle bounds;
    private int win = 0;
    Messege messegeCoin,messegeHealth;
    Point playerPos;
    public Player (Point playerPos,int coin,int health,int speed,Properties GAME_PROPS,Properties MESSAGE_PROPS) {
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
        hurtPerFrame = Double.parseDouble(GAME_PROPS.getProperty("riverDamagePerFrame"));
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
    }
    //The move logic: when player move, use this method
    public void move(Input input, List<Wall> walls){
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
    public void setWin(int win){
        this.win = win;
    }
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
    public void getTreasure(TreasureBox tmp){
        coin += tmp.getNum();
        messegeCoin.setNum(coin);
    }
    public double getHealth(){
        return health;
    }
    public void reset(){
        coin = 0;
        health = 100.0;
        messegeCoin.setNum(coin);
        messegeHealth.setNum(health);
        win = 0;
    }
}
