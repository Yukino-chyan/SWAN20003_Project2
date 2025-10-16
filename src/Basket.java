import bagel.Image;
import bagel.util.Point;

public class Basket extends DestructibleObject {
    public Basket(Point pos,int coin){
        this.pos = pos;
        this.coin = coin;
        this.image = new Image("res/basket.png");
        this.rect = image.getBoundingBoxAt(pos);
    }
}
