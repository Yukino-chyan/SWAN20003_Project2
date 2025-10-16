import bagel.Font;
import bagel.Window;
import bagel.util.Point;
import java.util.Locale;
/**
 * Messege class: Display a text label with an optional numeric value.
 */
public class Messege {
    private String messege;
    private Point pos;
    private Font font;
    private double num;
    private boolean centerX;
    private boolean haveDecimal;
    /**
     * This is the constructor of the Messege object.
     * @param messege The base text to display.
     * @param font The font used to draw the text.
     * @param pos The position where the text is drawn.
     * @param num The numeric value to append (use negative to hide).
     * @param centerX Whether to horizontally center the text on the screen.
     * @param haveDecimal Whether to display the number with two decimals.
     */
    public Messege(String messege, Font font, Point pos, double num, boolean centerX, boolean haveDecimal) {
        this.messege = messege;
        this.font = font;
        this.pos = pos;
        if (num >= 0.0) this.num = num;
        else this.num = -1.0;
        this.centerX = centerX;
        this.haveDecimal = haveDecimal;
    }
    /**
     * Set the numeric value to show after the text.
     * @param num The new numeric value (use negative to hide).
     */
    public void setNum(double num) {
        this.num = num;
    }
    /**
     * Draw the message on the screen.
     */
    public void show() {
        String output = messege + (num != -1.0 ? " : " + (haveDecimal ? String.format(Locale.ROOT, "%.2f", num) : String.format(Locale.ROOT, "%.0f", num)) : "");
        double drawX = centerX ? (Window.getWidth() - font.getWidth(messege)) / 2 : pos.x;
        font.drawString(output, drawX, pos.y);
    }
}
