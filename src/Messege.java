import bagel.Font;
import bagel.Window;
import bagel.util.Point;

import java.util.Locale;

public class Messege {
    private String messege;
    private Point pos;
    private Font font;
    private double num;
    private boolean centerX;
    public Messege(String messege,Font font,Point pos,double num,boolean centerX) {
        this.messege=messege;
        this.font = font;
        this.pos=pos;
        if(num >= 0.0) this.num = num;
        else this.num = -1.0;
        this.centerX=centerX;
    }
    public void setNum(double num) {
        this.num = num;
    }
    public void show (){
        String output = messege + ( num != -1.0 ? " : "+ String.format(Locale.ROOT, "%.2f", num) : "" );
        double drawX = centerX == true ? (Window.getWidth()- font.getWidth(messege))/2 : pos.x;
        font.drawString(output,drawX,pos.y);
    }
}
