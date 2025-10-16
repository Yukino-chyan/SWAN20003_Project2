import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.lang.reflect.Member;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
/*
About the naming conventions of methods:
getterxxx/setterxxx: get a value of a variable in the object/set a value of a variable in the object
show(): demonstrate all of images in this object
clashTest(player)/clash(player): test if there is a clash between the player and this object
reset(): when game restart, this method can let this object return to its initial value.
 */
public class ShadowDungeon extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private Room currentRoom;
    private PrepRoom prepRoom;
    private BattleRoom roomA,roomB;
    private EndRoom endRoom;
    private Player player;
    private Store store;
    private boolean pause;
    private final Map<RoomType, Room> rooms = new EnumMap<>(RoomType.class);
    public ShadowDungeon(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        //Create the player object.
        this.player = new Player(
                IOUtils.parseCoords(GAME_PROPS.getProperty("player.start")),
                Integer.parseInt(GAME_PROPS.getProperty("initialCoins")),
                Integer.parseInt(GAME_PROPS.getProperty("initialHealth")),
                Integer.parseInt(GAME_PROPS.getProperty("movingSpeed")),
                GAME_PROPS,MESSAGE_PROPS,
                new Image("res/player_left.png"),new Image("res/player_right.png"),
                Double.parseDouble(GAME_PROPS.getProperty("riverDamagePerFrame")), 0);
        //Create the prepRoom object
        Doors tmp = IOUtils.parseDoors(GAME_PROPS.getProperty("door.prep"));
        Point restartPos = IOUtils.parseCoords(GAME_PROPS.getProperty("restartarea.prep"));
        Point marineMessagePos = IOUtils.parseCoords(GAME_PROPS.getProperty("marineMessage"));
        Point robotMesssagePos = IOUtils.parseCoords(GAME_PROPS.getProperty("robotMessage"));
        Point marinePos = IOUtils.parseCoords(GAME_PROPS.getProperty("Marine"));
        Point robotPos = IOUtils.parseCoords(GAME_PROPS.getProperty("Robot"));
        prepRoom = new PrepRoom(tmp,restartPos,GAME_PROPS,MESSAGE_PROPS,marineMessagePos,robotMesssagePos,marinePos,robotPos);
        //Create the endRoom object
        tmp = IOUtils.parseDoors(GAME_PROPS.getProperty("door.end"));
        endRoom =  new EndRoom(tmp,restartPos,GAME_PROPS,MESSAGE_PROPS);
        //Create the roomA object
        Doors tmpPri = IOUtils.parseDoors(GAME_PROPS.getProperty("primarydoor.A"));
        Doors tmpSec = IOUtils.parseDoors(GAME_PROPS.getProperty("secondarydoor.A"));
        List<TreasureBox> tmpTreasure = IOUtils.parseTreasureBox(GAME_PROPS.getProperty("treasurebox.A"));
        KeyBulletKin enemy = IOUtils.parseKeyBulletKin(GAME_PROPS.getProperty("keyBulletKin.A"), GAME_PROPS, MESSAGE_PROPS);
        List<Wall> tmpWalls = IOUtils.parseWalls(GAME_PROPS.getProperty("wall.A"));
        List<River> tmpRivers = IOUtils.parseRivers(GAME_PROPS.getProperty("river.A"));
        List<BulletKin> tmpBulletKin = IOUtils.parseBulletKin(GAME_PROPS.getProperty("bulletKin.A"), GAME_PROPS, MESSAGE_PROPS);
        List<AshenBulletKin> tmpAshenBulletKin = IOUtils.parseAshenBulletKin(GAME_PROPS.getProperty("ashenBulletKin.A"), GAME_PROPS, MESSAGE_PROPS);
        Basket tmpBasket = IOUtils.parseBasket(GAME_PROPS.getProperty("basket.A"),Integer.parseInt(GAME_PROPS.getProperty("basketCoin")));
        Table tmpTable = IOUtils.parseTable(GAME_PROPS.getProperty("table.A"),0);
        roomA = new BattleRoom(tmpPri,tmpSec,tmpTreasure,enemy,tmpWalls,tmpRivers,tmpBulletKin,tmpAshenBulletKin,tmpBasket,tmpTable);
        //Create the roomB object
        tmpPri = IOUtils.parseDoors(GAME_PROPS.getProperty("primarydoor.B"));
        tmpSec = IOUtils.parseDoors(GAME_PROPS.getProperty("secondarydoor.B"));
        tmpTreasure = IOUtils.parseTreasureBox(GAME_PROPS.getProperty("treasurebox.B"));
        enemy = IOUtils.parseKeyBulletKin(GAME_PROPS.getProperty("keyBulletKin.B"), GAME_PROPS, MESSAGE_PROPS);
        tmpWalls = IOUtils.parseWalls(GAME_PROPS.getProperty("wall.B"));
        tmpRivers = IOUtils.parseRivers(GAME_PROPS.getProperty("river.B"));
        tmpBulletKin = IOUtils.parseBulletKin(GAME_PROPS.getProperty("bulletKin.B"), GAME_PROPS, MESSAGE_PROPS);
        tmpAshenBulletKin = IOUtils.parseAshenBulletKin(GAME_PROPS.getProperty("ashenBulletKin.B"), GAME_PROPS, MESSAGE_PROPS);
        tmpBasket = IOUtils.parseBasket(GAME_PROPS.getProperty("basket.B"),Integer.parseInt(GAME_PROPS.getProperty("basketCoin")));
        tmpTable = IOUtils.parseTable(GAME_PROPS.getProperty("table.B"),0);
        roomB = new BattleRoom(tmpPri,tmpSec,tmpTreasure,enemy,tmpWalls,tmpRivers,tmpBulletKin,tmpAshenBulletKin,tmpBasket,tmpTable);
        //Create the map between rooms and RoomType
        rooms.put(RoomType.PREP,prepRoom);
        rooms.put(RoomType.END,endRoom);
        rooms.put(RoomType.A,roomA);
        rooms.put(RoomType.B,roomB);
        currentRoom = prepRoom;
        this.store = new Store(GAME_PROPS);
    }
    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if(pause){
            currentRoom.show(input);
            player.show(input);
            store.show();
            if(input.wasPressed(Keys.L)){ store.buyForWeapon(player); }
            else if(input.wasPressed(Keys.E)){ store.buyForHealth(player); }
            else if(input.isDown(Keys.P)){
                pause = false;
                player.reset();
                prepRoom.reset();
                roomA.reset();
                roomB.reset();
                endRoom.reset();
                currentRoom = prepRoom;
            }
        }
        else{
            //Get the wall of the currentroom, it is used to make sure the player will not walk into the wall
            List<Wall> solid = roomA.getWalls();
            Basket tmpBasket = roomA.getBasket();
            Table tmpTable = roomA.getTable();
            if(currentRoom == roomB) { solid = roomB.getWalls(); tmpBasket = roomB.getBasket(); tmpTable = roomB.getTable(); }
            if(input.isDown(Keys.R) || input.isDown(Keys.M)){
                prepRoom.getSecDoors().setterOpen();
                if(input.isDown(Keys.R)){ player.setRobot(); }
                else if(input.isDown(Keys.M)){ player.setMarine(); }
            }
            player.shotCoolDown();
            if(input.wasPressed(MouseButtons.LEFT) && player.getHasChosen() == true) player.shot(currentRoom,input);
            currentRoom.move();
            if(currentRoom == roomA && roomA.getGateDelay()) roomA.shotFireball(player);
            if(currentRoom == roomB && roomB.getGateDelay()) roomB.shotFireball(player);
            if(currentRoom == roomA) roomA.projectilesClashTest(player);
            if(currentRoom == roomB) roomB.projectilesClashTest(player);
            if(currentRoom == roomA && roomA.getPassed() == false) roomA.winTest(player);
            if(currentRoom == roomB && roomB.getPassed() == false) roomB.winTest(player);
            player.setterBounds(player.getPlayerPos(input));
            if(currentRoom == roomA || currentRoom == roomB){
                player.move(input,solid,tmpBasket,tmpTable,currentRoom.getPriDoors(),currentRoom.getSecDoors(),currentRoom.getGateDelay());
            }
            else player.move(input,currentRoom.getPriDoors(),currentRoom.getSecDoors(),currentRoom.getGateDelay());
            int val = currentRoom.clashTest(player);
            //Deal with the clash with pridoor.
            if(val == 1){
                Doors clashDoor = currentRoom.getPriDoors();
                RoomType to = clashDoor.getToRoom();
                Room toRoom = rooms.get(to);
                currentRoom.clean();
                currentRoom = toRoom;
                currentRoom.entry(player,true);
            }
            //Deal with the clash with enemy
            else if(val == 2 && input.isDown(Keys.ENTER)){
                player.reset();
                prepRoom.reset();
                roomA.reset();
                roomB.reset();
                endRoom.reset();
                currentRoom = prepRoom;
            }
            //Deal with the clash with secdoor
            else if(val == 3){
                Doors clashDoor = currentRoom.getSecDoors();
                RoomType to = clashDoor.getToRoom();
                Room toRoom = rooms.get(to);
                currentRoom.clean();
                currentRoom = toRoom;
                currentRoom.entry(player,false);
            }
            //Deal with the clash with river
            else if(val == 4){ player.injured(); }
            //Deal with the clash with treasurebox
            else if(val == 5 && player.getKey() > 0){
                List<TreasureBox> treasureBoxes = new ArrayList<>();
                if(currentRoom == roomA) treasureBoxes = roomA.getTreasureBoxes();
                if(currentRoom == roomB) treasureBoxes = roomB.getTreasureBoxes();
                for(TreasureBox tmp : treasureBoxes) {
                    if(tmp.clash(player) && input.isDown(Keys.K) && !tmp.getOpen()){
                        player.getTreasure(tmp);
                        tmp.setOpen(true);
                        player.setKey(player.getKey() - 1);
                    }
                }
            }
            else if(val == 6){
                player.injured(player.getHurtPerFrame());
            }
            //If the player's health lower than zero, he loses.
            if(player.getHealth() <= 0){
                endRoom.setWinFlag(false);
                currentRoom = endRoom;
            }
            //If the player has killed two enemy, he wins.
            if(player.getWin() == 2) {
                endRoom.setWinFlag(true);
                player.setWin(0);
            }
            //Show the scene
            currentRoom.show(input);
            player.show(input);
        }
        if(input.wasPressed(Keys.SPACE) && pause == false){ pause = true; }
        else if(input.wasPressed(Keys.SPACE) && pause == true){ pause = false; }
    }
    /**
     * The main entry point of the Shadow Dungeon game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDungeon game = new ShadowDungeon(gameProps, messageProps);
        game.run();
    }
}
