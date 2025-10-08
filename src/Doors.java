import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
public class Doors {
    private Point pos;
    private boolean open = false;
    private RoomType toRoom;
    private final Image lockedImg = new Image("res/locked_door.png");
    private final Image openImg = new Image("res/unlocked_door.png");
    private final Rectangle rect;
    public Doors(Point pos,RoomType toRoom) {
        this.pos = pos;
        this.toRoom = toRoom;
        this.rect = lockedImg.getBoundingBoxAt(pos);
    }
    public void show() {
        if(open) openImg.draw(pos.x,pos.y);
        else lockedImg.draw(pos.x,pos.y);
    }
    public double getPosX() {
        return pos.x;
    }
    public double getPosY() {
        return pos.y;
    }
    public Boolean getOpen() {
        return open;
    }
    public void setterOpen() {
        open = true;
    }
    public RoomType getToRoom() {
        return toRoom;
    }
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
    public void reset(){
        open = false;
    }
}
