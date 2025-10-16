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
    private boolean haveDecimal;
    public Messege(String messege,Font font,Point pos,double num,boolean centerX,boolean haveDecimal) {
        this.messege=messege;
        this.font = font;
        this.pos=pos;
        if(num >= 0.0) this.num = num;
        else this.num = -1.0;
        this.centerX = centerX;
        this.haveDecimal = haveDecimal;
    }
    public void setNum(double num) {
        this.num = num;
    }
    public void show (){
        String output = messege + ( num != -1.0 ? " : "+ (haveDecimal ? String.format(Locale.ROOT, "%.2f", num) :  String.format(Locale.ROOT, "%.0f", num)) : "" );
        double drawX = centerX == true ? (Window.getWidth()- font.getWidth(messege))/2 : pos.x;
        font.drawString(output,drawX,pos.y);
    }
}
