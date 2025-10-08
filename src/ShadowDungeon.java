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
                GAME_PROPS,MESSAGE_PROPS);
        //Create the prepRoom object
        Doors tmp = IOUtils.parseDoors(GAME_PROPS.getProperty("door.prep"));
        Point restartPos = IOUtils.parseCoords(GAME_PROPS.getProperty("restartarea.prep"));
        prepRoom = new PrepRoom(tmp,restartPos,GAME_PROPS,MESSAGE_PROPS);
        //Create the endRoom object
        tmp = IOUtils.parseDoors(GAME_PROPS.getProperty("door.end"));
        endRoom =  new EndRoom(tmp,restartPos,GAME_PROPS,MESSAGE_PROPS);
        //Create the roomA object
        Doors tmpPri = IOUtils.parseDoors(GAME_PROPS.getProperty("primarydoor.A"));
        Doors tmpSec = IOUtils.parseDoors(GAME_PROPS.getProperty("secondarydoor.A"));
        List<TreasureBox> tmpTreasure = IOUtils.parseTreasureBox(GAME_PROPS.getProperty("treasurebox.A"));
        KeyBulletKin enemy = IOUtils.parseKeyBulletKin(GAME_PROPS.getProperty("keyBulletKin.A"));
        List<Wall> tmpWalls = IOUtils.parseWalls(GAME_PROPS.getProperty("wall.A"));
        List<River> tmpRivers = IOUtils.parseRivers(GAME_PROPS.getProperty("river.A"));
        roomA = new BattleRoom(tmpPri,tmpSec,tmpTreasure,enemy,tmpWalls,tmpRivers);
        //Create the roomB object
        tmpPri = IOUtils.parseDoors(GAME_PROPS.getProperty("primarydoor.B"));
        tmpSec = IOUtils.parseDoors(GAME_PROPS.getProperty("secondarydoor.B"));
        tmpTreasure = IOUtils.parseTreasureBox(GAME_PROPS.getProperty("treasurebox.B"));
        enemy = IOUtils.parseKeyBulletKin(GAME_PROPS.getProperty("keyBulletKin.B"));
        tmpWalls = IOUtils.parseWalls(GAME_PROPS.getProperty("wall.B"));
        tmpRivers = IOUtils.parseRivers(GAME_PROPS.getProperty("river.B"));
        roomB = new BattleRoom(tmpPri,tmpSec,tmpTreasure,enemy,tmpWalls,tmpRivers);
        //Create the map between rooms and RoomType
        rooms.put(RoomType.PREP,prepRoom);
        rooms.put(RoomType.END,endRoom);
        rooms.put(RoomType.A,roomA);
        rooms.put(RoomType.B,roomB);
        currentRoom = prepRoom;
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
        //Get the wall of the currentroom, it is used to make sure the player will not walk into the wall
        List<Wall> solid = new ArrayList<>();
        if(currentRoom == roomA) solid = roomA.getWalls();
        if(currentRoom == roomB) solid = roomB.getWalls();
        if(input.isDown(Keys.R)){
            prepRoom.getSecDoors().setterOpen();
        }
        player.setterBounds(player.getPlayerPos(input));
        player.move(input,solid);
        int val = currentRoom.clashTest(player);
        //Deal with the clash with pridoor.
        if(val == 1){
            Doors clashDoor = currentRoom.getPriDoors();
            RoomType to = clashDoor.getToRoom();
            Room toRoom = rooms.get(to);
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
            currentRoom = toRoom;
            currentRoom.entry(player,false);
        }
        //Deal with the clash with river
        else if(val == 4){ player.injured(); }
        //Deal with the clash with treasurebox
        else if(val == 5){
            List<TreasureBox> treasureBoxes = new ArrayList<>();
            if(currentRoom == roomA) treasureBoxes = roomA.getTreasureBoxes();
            if(currentRoom == roomB) treasureBoxes = roomB.getTreasureBoxes();
            for(TreasureBox tmp : treasureBoxes) {
                if(tmp.clash(player) && input.isDown(Keys.K) && !tmp.getOpen()){
                    player.getTreasure(tmp);
                    tmp.setOpen(true);
                }
            }
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
