import bagel.Image;
import bagel.Input;
import bagel.Window;
import java.util.Properties;

public interface Room {
    public Image bg = new Image("res/background.png");
    public int width = Window.getWidth();
    public int height = Window.getHeight();
    void show(Input input);
    Doors getPriDoors();
    Doors getSecDoors();
    int clashTest(Player player);
    void entry(Player player,Boolean backflag);
    void clean();
    void shotBullet(Player player, Input input);
    void move();
    Boolean getGateDelay();
}
