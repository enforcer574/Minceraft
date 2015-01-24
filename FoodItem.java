import java.util.HashMap;


public class FoodItem extends Item{

	protected Integer resHealth;
	protected Integer resEnergy;
	protected Boolean canPoison;
	
	public static final int APPLE = 20;
	public static final int BEEF_RAW = 21;
	public static final int BEEF_COOKED = 22;
	public static final int PORK_RAW = 23;
	public static final int PORK_COOKED = 24;
	public static final int FISH_RAW = 25;
	public static final int FISH_COOKED = 26;
	public static final int BREAD = 27;
	public static final int GOLDEN_APPLE = 28;
	public static final int FLESH = 29;
	public static final int CACTUS = 55;
	public static final int STRAWBERRY = 56;
	public static final int COCOA = 57;
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, Integer> defaultFoodValues = new HashMap<Integer, Integer>(){
		{
			put(FoodItem.GOLDEN_APPLE, 20);
			put(FoodItem.BEEF_COOKED, 10);
			put(FoodItem.PORK_COOKED, 9);
			put(FoodItem.FISH_COOKED, 8);
			put(FoodItem.BREAD, 6);
			put(FoodItem.FLESH, 4);
			put(FoodItem.APPLE, 4);
			put(FoodItem.CACTUS, 3);
			put(FoodItem.STRAWBERRY, 3);
			put(FoodItem.BEEF_RAW, 3);
			put(FoodItem.PORK_RAW, 2);
			put(FoodItem.FISH_RAW, 2);
			put(FoodItem.COCOA, 2);
		};
			
		};
	
	public FoodItem(Integer i, Integer q) {
		super(i, q);
		resHealth = defaultFoodValues.get(i);
		resEnergy = resHealth*2;
	}

}
