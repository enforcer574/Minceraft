import java.io.Serializable;
import java.util.HashMap;


public class Item implements Serializable{

	protected static final long serialVersionUID = 1L;
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, Integer> growTime = new HashMap<Integer, Integer>(){
		{
			put(Item.SEED, 4320);
			put(Item.SAPLING, 10080);
			put(FoodItem.STRAWBERRY, 1440);
			put(FoodItem.COCOA, 2880);
			put(FoodItem.CACTUS, 5760);
		}
	};
	
	public static final int SAND = 0;
	public static final int DIRT = 1;
	public static final int WOOD = 2;
	public static final int STONE = 3;
	public static final int GLASS = 4;
	public static final int CLAY = 5;
	public static final int BASALT = 6;
	public static final int IRON_INGOT = 7;
	public static final int GOLD_INGOT = 8;
	public static final int FLINT = 9;
	public static final int IRON_ORE = 10;
	public static final int GOLD_ORE = 11;
	public static final int COAL = 12;
	public static final int DIAMOND = 13;
	public static final int STICK = 14;
	public static final int SAPLING = 15;
	public static final int WHEAT = 16;
	public static final int STEEL_INGOT = 17;
	public static final int SILK = 18;
	public static final int ARROW = 19;
	public static final int COIN = 54;
	public static final int COMPASS = 60;
	public static final int OBSIDIAN = 61;
	public static final int BASALT_COMPASS = 62;
	public static final int HIDE = 63;
	public static final int BUCKET = 64;
	public static final int WATER_BUCKET = 65;
	public static final int SEED = 81;
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, String> itemNames = new HashMap<Integer, String>(){
		{
		put(0, "Sand");
		put(1, "Dirt");
		put(2, "Wood");
		put(3, "Stone");
		put(4, "Glass");
		put(5, "Clay");
		put(6, "Basalt");
		put(7, "Iron Ingot");
		put(8, "Gold Ingot");
		put(9, "Flint");
		put(10, "Iron Ore");
		put(11, "Gold Ore");
		put(12, "Coal");
		put(13, "Diamond");
		put(14, "Wooden Stick");
		put(15, "Sapling");
		put(16, "Wheat");
		put(17, "Steel Ingot");
		put(18, "Spider Silk");
		put(19, "Arrow");
		put(20, "Apple");
		put(21, "Raw Beef");
		put(22, "Steak");
		put(23, "Raw Pork");
		put(24, "Porkchop");
		put(25, "Raw Fish");
		put(26, "Fish");
		put(27, "Bread");
		put(28, "Golden Apple");
		put(29, "Zombie Flesh");
		put(30, "Wooden Axe");
		put(31, "Stone Axe");
		put(32, "Iron Axe");
		put(33, "Diamond Axe");
		put(34, "Wooden Pickaxe");
		put(35, "Stone Pickaxe");
		put(36, "Iron Pickaxe");
		put(37, "Diamond Pickaxe");
		put(38, "Wooden Shovel");
		put(39, "Stone Shovel");
		put(40, "Iron Shovel");
		put(41, "Diamond Shovel");
		put(42, "Wooden Club");
		put(43, "Stone Club");
		put(44, "Iron Knife");
		put(45, "Diamond Sword");
		put(46, "Bow");
		put(47, "Long Bow");
		put(48, "Compound Bow");
		put(49, "Steel Axe");
		put(50, "Steel Pickaxe");
		put(51, "Steel Shovel");
		put(52, "Steel Sword");
		put(53, "Fishing Rod");
		put(54, "Gold Coin");
		put(55, "Cactus Fruit");
		put(56, "Wild Strawberry");
		put(57, "Cocoa Bean");
		put(58, "Obsidian Greatsword");
		put(59, "Obsidian Pickaxe");
		put(60, "Compass");
		put(61, "Obsidian Shard");
		put(62, "Basalt Compass");
		put(63, "Cow Hide");
		put(64, "Hide Armor");
		put(65, "Iron Armor");
		put(66, "Steel Armor");
		put(67, "Diamond Armor");
		put(68, "Obsidian Armor");
		put(69, "Bucket");
		put(70, "Bucket of Water");
		put(71, "Potion of Health");
		put(72, "Potion of Energy");
		put(73, "Potion of Strength");
		put(74, "Potion of the Warrior");
		put(75, "Potion of Dexterity");
		put(76, "Potion of Agility");
		put(77, "Stone Hammer");
		put(78, "Iron Hammer");
		put(79, "Steel Hammer");
		put(80, "Diamond Hammer");
		put(81, "Wheat Seeds");
		}
	};
	
	protected String name;
	protected Integer id;
	protected Integer qty;
	
	public Item(Integer i, Integer q){
		name=itemNames.get(i);
		id=i;
		qty=q;
	}
	
	public Integer id(){
		return id;
	}
	public String getName(){
		return name;
	}
	public Integer getQuantity(){
		return qty;
	}
	public void updateQuantity(Integer q){
		qty += q;
		if (qty<=0){
			qty=0;
		}
	}
	
}
