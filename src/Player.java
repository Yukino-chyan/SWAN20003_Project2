import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.List;
import java.util.Properties;
import static java.lang.Math.*;
/**
 * Player class: The player character in the game.
 */
public class Player {
    private Image playerLeft;
    private Image playerRight;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private int coin, speed;
    private double health, hurtPerFrame;
    private Rectangle bounds;
    private int win = 0;
    private int key = 0;
    private int weaponLevel = 0;
    private int bulletFreq, coolDown;
    public double bulletSpeed;
    private int killCoin, destroyBasketCoin;
    private double hurtPerShoot;
    private double initialX, initialY;
    private Messege messegeCoin, messegeHealth, messegeKey, messegeWeaponLevel;
    private Point playerPos;
    private Boolean hasChosen = false;
    /**
     * This is the constructor of the Player object.
     * @param playerPos The initial position of the player.
     * @param coin The initial coin amount.
     * @param health The initial health value.
     * @param speed The movement speed of the player.
     * @param GAME_PROPS The game properties.
     * @param MESSAGE_PROPS The message properties.
     * @param playerLeft The image used when facing left.
     * @param playerRight The image used when facing right.
     * @param hurtPerFrame The health lost per frame in hazardous areas.
     * @param killCoin The extra coins gained per kill.
     */
    public Player(Point playerPos, int coin, int health, int speed, Properties GAME_PROPS, Properties MESSAGE_PROPS, Image playerLeft, Image playerRight, double hurtPerFrame, int killCoin) {
        // Cache sprites, initial stats and configuration
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
        this.playerPos = playerPos;
        this.coin = coin;
        this.health = health;
        this.speed = speed;
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        // Create HUD messages
        messegeCoin = new Messege(MESSAGE_PROPS.getProperty("coinDisplay"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), IOUtils.parseCoords(GAME_PROPS.getProperty("coinStat")), coin, false, false);
        messegeHealth = new Messege(MESSAGE_PROPS.getProperty("healthDisplay"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), IOUtils.parseCoords(GAME_PROPS.getProperty("healthStat")), health, false, true);
        messegeKey = new Messege(MESSAGE_PROPS.getProperty("keyDisplay"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), IOUtils.parseCoords(GAME_PROPS.getProperty("keyStat")), key, false, false);
        messegeWeaponLevel = new Messege(MESSAGE_PROPS.getProperty("weaponDisplay"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), IOUtils.parseCoords(GAME_PROPS.getProperty("weaponStat")), weaponLevel, false, false);
        // Gameplay numbers
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
    /**
     * Show the player and its HUD elements.
     * @param input The current input.
     */
    public void show(Input input) {
        // Draw facing based on mouse x relative to player
        if (input.getMouseX() > playerPos.x) playerRight.draw(playerPos.x, playerPos.y);
        else if (input.getMouseX() < playerPos.x) playerLeft.draw(playerPos.x, playerPos.y);
        // Draw HUD
        messegeCoin.show();
        messegeHealth.show();
        messegeKey.show();
        messegeWeaponLevel.show();
    }
    /**
     * Move the player with full collision checks.
     * @param input The current input.
     * @param walls The solid walls to collide with.
     * @param tmpBasket The basket obstacle.
     * @param tmpTable The table obstacle.
     * @param priDoor The primary door.
     * @param secDoor The secondary door.
     * @param gateDelay Whether doors should block the player currently.
     */
    public void move(Input input, List<Wall> walls, Basket tmpBasket, Table tmpTable, Doors priDoor, Doors secDoor, Boolean gateDelay) {
        // Read input to build desired delta
        double dx = 0, dy = 0;
        if (input.isDown(Keys.W)) dy -= speed;
        if (input.isDown(Keys.S)) dy += speed;
        if (input.isDown(Keys.A)) dx -= speed;
        if (input.isDown(Keys.D)) dx += speed;
        // Horizontal step: move tentatively, then resolve collisions and clamp
        if (dx != 0) {
            double old = playerPos.x;
            setterPosx(old + dx);
            setterBounds(getPlayerPos(input));
            // Resolve wall collision
            for (Wall wall : walls) { if (wall.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); break; } }
            // Resolve destructible collisions
            if (tmpBasket.isAlive() && tmpBasket.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            if (tmpTable.isAlive() && tmpTable.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            // Resolve closed doors when gateDelay applies
            if (!priDoor.getOpen() && gateDelay && priDoor.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            if (!secDoor.getOpen() && gateDelay && secDoor.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            // Keep within screen bounds
            clampX(input);
        }
        // Vertical step: same as above but in Y
        if (dy != 0) {
            double old = playerPos.y;
            setterPosy(old + dy);
            setterBounds(getPlayerPos(input));
            // Resolve wall collision
            for (Wall wall : walls) { if (wall.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); break; } }
            // Resolve destructible collisions
            if (tmpBasket.isAlive() && tmpBasket.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            if (tmpTable.isAlive() && tmpTable.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            // Resolve closed doors when gateDelay applies
            if (!priDoor.getOpen() && gateDelay && priDoor.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            if (!secDoor.getOpen() && gateDelay && secDoor.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            // Keep within screen bounds
            clampY(input);
        }
    }
    /**
     * Move the player with door collision checks (prep room).
     * @param input The current input.
     * @param priDoor The primary door.
     * @param secDoor The secondary door.
     * @param gateDelay Whether doors should block the player currently.
     */
    public void move(Input input, Doors priDoor, Doors secDoor, Boolean gateDelay) {
        // Read input to build desired delta
        double dx = 0, dy = 0;
        if (input.isDown(Keys.W)) dy -= speed;
        if (input.isDown(Keys.S)) dy += speed;
        if (input.isDown(Keys.A)) dx -= speed;
        if (input.isDown(Keys.D)) dx += speed;
        // Horizontal movement with door collision only
        if (dx != 0) {
            double old = playerPos.x;
            setterPosx(old + dx);
            setterBounds(getPlayerPos(input));
            if (!priDoor.getOpen() && gateDelay && priDoor.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            if (!secDoor.getOpen() && gateDelay && secDoor.clash(this)) { setterPosx(old); setterBounds(getPlayerPos(input)); }
            clampX(input);
        }
        // Vertical movement with door collision only
        if (dy != 0) {
            double old = playerPos.y;
            setterPosy(old + dy);
            setterBounds(getPlayerPos(input));
            if (!priDoor.getOpen() && gateDelay && priDoor.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            if (!secDoor.getOpen() && gateDelay && secDoor.clash(this)) { setterPosy(old); setterBounds(getPlayerPos(input)); }
            clampY(input);
        }
    }
    /**
     * Keep the player within the screen horizontally.
     * @param input The current input.
     */
    private void clampX(Input input) {
        // Compute half-width from current bounds and correct overflow
        Rectangle b = getterBounds();
        double halfW = (b.right() - b.left()) / 2.0;
        if (b.left() < 0) { setterPosx(halfW); setterBounds(getPlayerPos(input)); }
        else if (b.right() > Window.getWidth()) { setterPosx(Window.getWidth() - halfW); setterBounds(getPlayerPos(input)); }
    }
    /**
     * Keep the player within the screen vertically.
     * @param input The current input.
     */
    private void clampY(Input input) {
        // Compute half-height from current bounds and correct overflow
        Rectangle b = getterBounds();
        double halfH = (b.bottom() - b.top()) / 2.0;
        if (b.top() < 0) { setterPosy(halfH); setterBounds(getPlayerPos(input)); }
        else if (b.bottom() > Window.getHeight()) { setterPosy(Window.getHeight() - halfH); setterBounds(getPlayerPos(input)); }
    }
    /**
     * Get the player's X coordinate.
     * @return The x value of the player's position.
     */
    public double getPosX() { return playerPos.x; }
    /**
     * Get the player's Y coordinate.
     * @return The y value of the player's position.
     */
    public double getPosY() { return playerPos.y; }
    /**
     * Get the player's current health.
     * @return The current health value.
     */
    public double getHealth() { return health; }
    /**
     * Get the current coin amount.
     * @return The number of coins.
     */
    public int getCoin() { return coin; }
    /**
     * Get the current win count.
     * @return The number of wins.
     */
    public int getWin() { return win; }
    /**
     * Get the number of keys held.
     * @return The key count.
     */
    public int getKey() { return key; }
    /**
     * Get the weapon level.
     * @return The current weapon level.
     */
    public int getWeaponLevel() { return weaponLevel; }
    /**
     * Get the bullet speed.
     * @return The bullet speed.
     */
    public double getBulletSpeed() { return bulletSpeed; }
    /**
     * Get the continuous damage per frame.
     * @return The damage taken per frame.
     */
    public double getHurtPerFrame() { return hurtPerFrame; }
    /**
     * Get the damage dealt per shot.
     * @return The damage per bullet.
     */
    public double getHurtPerShot() { return hurtPerShoot; }
    /**
     * Get the player's current bounding box.
     * @return The player bounding rectangle.
     */
    public Rectangle getterBounds() { return bounds; }
    /**
     * Get the player's facing-dependent bounding box.
     * @param input The current input.
     * @return The bounding box at the current facing.
     */
    public Rectangle getPlayerPos(Input input) {
        if (input.getMouseX() > playerPos.x) return playerLeft.getBoundingBoxAt(playerPos);
        else return playerRight.getBoundingBoxAt(playerPos);
    }
    /**
     * Check if the player type has been chosen.
     * @return true if chosen, false otherwise.
     */
    public Boolean getHasChosen() { return hasChosen; }
    /**
     * Set the player's X coordinate.
     * @param x The new x value.
     */
    public void setterPosx(double x) { playerPos = new Point(x, playerPos.y); }
    /**
     * Set the player's Y coordinate.
     * @param y The new y value.
     */
    public void setterPosy(double y) { playerPos = new Point(playerPos.x, y); }
    /**
     * Set the player's bounding box.
     * @param bounds The new bounding rectangle.
     */
    public void setterBounds(Rectangle bounds) { this.bounds = bounds; }
    /**
     * Set the current win count.
     * @param win The win value to set.
     */
    public void setWin(int win) { this.win = win; }
    /**
     * Set the current key count.
     * @param key The key value to set.
     */
    public void setKey(int key) { this.key = key; messegeKey.setNum(key); }
    /**
     * Upgrade weapon damage and level.
     * @param hurtPerShoot The new damage per shot.
     */
    public void setHurtPerShoot(double hurtPerShoot) { weaponLevel++; this.hurtPerShoot = hurtPerShoot; messegeWeaponLevel.setNum(weaponLevel); }
    /**
     * Increase coin count by a value.
     * @param Coin The coin amount to add.
     */
    public void gainCoin(int Coin) { this.coin += Coin; messegeCoin.setNum(this.coin); }
    /**
     * Increase the number of keys by one.
     */
    public void gainKey() { key++; messegeKey.setNum(key); }
    /**
     * Increase health by a value.
     * @param health The health amount to add.
     */
    public void gainHealth(double health) { this.health += health; messegeHealth.setNum(this.health); }
    /**
     * Spend a certain amount of coins.
     * @param coin The coin amount to use.
     */
    public void useCoin(int coin) { this.coin -= coin; messegeCoin.setNum(this.coin); }
    /**
     * Gain coins after killing an enemy.
     * @param num The base coin value from the enemy.
     */
    public void getKill(int num) { coin = coin + num + killCoin; messegeCoin.setNum(coin); }
    /**
     * Gain coins from a treasure box.
     * @param tmp The treasure box looted.
     */
    public void getTreasure(TreasureBox tmp) { coin += tmp.getNum(); messegeCoin.setNum(coin); }
    /**
     * Apply continuous frame-based damage.
     */
    public void injured() { health -= hurtPerFrame; messegeHealth.setNum(health); }
    /**
     * Apply a specific amount of damage.
     * @param num The damage to apply.
     */
    public void injured(double num) { health -= num; messegeHealth.setNum(health); }
    /**
     * Reduce the shooting cooldown by one frame.
     */
    public void shotCoolDown() { if (coolDown != 0) coolDown--; }
    /**
     * Shoot a bullet into the given room if off cooldown.
     * @param room The room to receive the bullet.
     * @param input The current input.
     */
    public void shot(Room room, Input input) {
        // Early out when still cooling down; otherwise reset cooldown and emit a shot
        if (coolDown != 0) return;
        else coolDown = bulletFreq;
        room.shotBullet(this, input);
    }
    /**
     * Reset the player to initial state.
     */
    public void reset() {
        // Reset basic stats and HUD
        coin = 0;
        health = 100.0;
        messegeCoin.setNum(coin);
        messegeHealth.setNum(health);
        // Reset progress
        win = 0;
        key = 0;
        messegeKey.setNum(key);
        // Reset position and sprites
        setterPosx(initialX);
        setterPosy(initialY);
        playerLeft = new Image("res/player_left.png");
        playerRight = new Image("res/player_right.png");
        hasChosen = false;
        // Reset weapon
        weaponLevel = 0;
        messegeWeaponLevel.setNum(weaponLevel);
    }
    /**
     * Switch to Marine form (no river damage, no extra kill coin).
     */
    public void setMarine() {
        // Change sprites and form-specific attributes
        hasChosen = true;
        playerLeft = new Image("res/marine_left.png");
        playerRight = new Image("res/marine_right.png");
        hurtPerFrame = 0;
        killCoin = 0;
    }
    /**
     * Switch to Robot form (takes river damage, gains extra kill coin).
     */
    public void setRobot() {
        // Change sprites and form-specific attributes from config
        hasChosen = true;
        playerLeft = new Image("res/robot_left.png");
        playerRight = new Image("res/robot_right.png");
        hurtPerFrame = Double.parseDouble(GAME_PROPS.getProperty("riverDamagePerFrame"));
        killCoin = Integer.parseInt(GAME_PROPS.getProperty("robotExtraCoin"));
    }
}
