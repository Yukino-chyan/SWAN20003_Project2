import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class TreasureBox {
    private Point pos;
    private Image treasureBoxImage = new Image("res/treasure_box.png");
    private int num;
    private Rectangle rect;
    private Boolean open = false;
    TreasureBox(Point pos,int num) {
        this.pos = pos;
        this.num = num;
        this.rect = treasureBoxImage.getBoundingBoxAt(pos);
    }
    public void show() {
        treasureBoxImage.draw(pos.x, pos.y);
    }
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
    public int getNum() {
        return num;
    }
    public Boolean getOpen() {
        return open;
    }
    public void setOpen(Boolean open) {
        this.open = open;
    }
    public void reset(){
        open = false;
    }
}

