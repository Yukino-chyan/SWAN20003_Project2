import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

public class Store {
    private Point pos;
    private Image storeImage = new Image("res/store.png");
    private int healthPurchase, weaponPurchase;
    private double healthBonus, weaponAdvanceDamage, weaponEliteDamage;
    public Store(Properties GAME_PROPS){
        this.healthPurchase = Integer.parseInt(GAME_PROPS.getProperty("healthPurchase"));
        this.weaponPurchase = Integer.parseInt(GAME_PROPS.getProperty("weaponPurchase"));;
        this.healthBonus = Double.parseDouble(GAME_PROPS.getProperty("healthBonus"));
        this.weaponAdvanceDamage = Double.parseDouble(GAME_PROPS.getProperty("weaponAdvanceDamage"));
        this.weaponEliteDamage = Double.parseDouble(GAME_PROPS.getProperty("weaponEliteDamage"));
        this.pos = IOUtils.parseCoords(GAME_PROPS.getProperty("store"));
    }
    public void buyForHealth(Player player){
        if(player.getCoin() < healthPurchase) return ;
        player.useCoin(healthPurchase);
        player.gainHealth(healthBonus);
    }
    public void buyForWeapon(Player player){
        if(player.getCoin() < weaponPurchase) return ;
        int weaponLevel = player.getWeaponLevel();
        if(weaponLevel == 2) return ;
        player.useCoin(weaponPurchase);
        if(weaponLevel == 1) player.setHurtPerShoot(weaponEliteDamage);
        else if(weaponLevel == 0) player.setHurtPerShoot(weaponAdvanceDamage);
    }
    public void show(){ storeImage.draw(pos.x, pos.y); }
}
