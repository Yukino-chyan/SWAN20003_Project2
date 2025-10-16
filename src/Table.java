import bagel.Image;
import bagel.util.Point;

public class Table extends DestructibleObject {
    public Table(Point pos,int Coin){
        this.pos = pos;
        this.coin = Coin;
        this.image = new Image("res/table.png");
        this.rect = image.getBoundingBoxAt(pos);
    }
}
