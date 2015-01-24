import java.util.HashMap;


public class WeaponItem extends ToolItem{

	protected Integer dmgRate;
	protected Boolean useArrows;
	
	public static final int WOODEN_CLUB = 42;
	public static final int STONE_CLUB = 43;
	public static final int IRON_BLADE = 44;
	public static final int DIAMOND_SWORD = 45;
	public static final int STEEL_SWORD = 52;
	
	public static final int BOW_BASIC = 46;
	public static final int BOW_LONG = 47;
	public static final int BOW_COMPOUND = 48;
	
	public static final int BLACK_BLADE = 58;
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, Integer> defaultDurability = new HashMap<Integer, Integer>(){
		{
			put(WeaponItem.WOODEN_CLUB, 50);
			put(WeaponItem.STONE_CLUB, 100);
			put(WeaponItem.IRON_BLADE, 250);
			put(WeaponItem.STEEL_SWORD, 400);
			put(WeaponItem.DIAMOND_SWORD, 1000);
			
			put(WeaponItem.BOW_BASIC, 80);
			put(WeaponItem.BOW_LONG, 300);
			put(WeaponItem.BOW_COMPOUND, 700);
			
			put(WeaponItem.BLACK_BLADE, 3000);
		}
	};
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, Integer> defaultDmgRate = new HashMap<Integer, Integer>(){
		{
			put(WeaponItem.WOODEN_CLUB, 3);
			
		}
	};
	
	public WeaponItem(Integer i, Integer q) {
		super(i, q);
		durability = defaultDurability.get(id);
		dmgRate = defaultDmgRate.get(id);
		
		if (id==46 || id==47 || id==48){
			useArrows = true;
		} else {
			useArrows = false;
		}
	}
	
	
}
