import bagel.Input;
import bagel.Keys;
import bagel.Image;
import bagel.Font;
import bagel.util.Point;
import java.util.Optional;
import java.util.Properties;
import bagel.Window;
public class PrepRoom implements Room {
    private Boolean gateDelay = false;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private Doors doorToA;
    private Restart_Area restartArea;
    private Messege title,moveMessege;
    public PrepRoom(Doors doorToA,Point restartPos,Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.doorToA = doorToA;
        this.restartArea = new Restart_Area(restartPos);
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        title = new Messege(
                MESSAGE_PROPS.getProperty("title"),
                new Font (GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))),
                new Point (Window.getWidth()/2.0,Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true
        );
        moveMessege = new Messege(
                MESSAGE_PROPS.getProperty("moveMessage"),
                new Font (GAME_PROPS.getProperty("font"),Integer.parseInt(GAME_PROPS.getProperty("prompt.fontSize"))),
                new Point (Window.getWidth()/2.0,Double.parseDouble(GAME_PROPS.getProperty("moveMessage.y"))), -1.0, true
        );
    }
    public void show(Input input) {
        bg.draw(width/2,height/2);
        restartArea.show();
        doorToA.show();
        title.show();
        moveMessege.show();
    }
    public Doors getPriDoors() { return doorToA; }
    public Doors getSecDoors() { return doorToA; }
    public int clashTest(Player player) {
        if(!doorToA.clash(player)) { gateDelay = true; }
        if(doorToA.clash(player) && doorToA.getOpen() && gateDelay) return 3;
        if(restartArea.clash(player)) return 2;
        return 0;
    }
    public void entry(Player player,Boolean backflag) {
        player.setterPosx(doorToA.getPosX());
        player.setterPosy(doorToA.getPosY());
        gateDelay = false;
    }
    public void reset() {
        doorToA.reset();
        gateDelay = false;
    }
}
