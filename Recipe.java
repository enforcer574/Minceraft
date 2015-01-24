import java.util.ArrayList;


public class Recipe {
	private Integer recipeType;
	private Integer product_id;
	private Integer product_qty;
	private Integer craftTime;
	private ArrayList<Item> ingredients;
	
	public static final int REGULAR = 0;
	public static final int TOOL = 1;
	public static final int WEAPON = 2;
	public static final int BUILDING = 3;
	public static final int COOKED = 4;
	public static final int ARMOR = 5;
	public static final int SMELT = 6;
	
	public Recipe(Integer t){
		recipeType = t;
		ingredients = new ArrayList<Item>();
	}
	public Integer getType(){
		return recipeType;
	}
	public Item getProduct() throws IndexOutOfBoundsException{
		if (recipeType==0){
			return new Item(product_id, product_qty);
		}
		else if (recipeType==1){
			return new ToolItem(product_id, product_qty);
		}
		else if (recipeType==2){
			return new WeaponItem(product_id, product_qty);
		}
		else if (recipeType==4){
			return new FoodItem(product_id, product_qty);
		}
		else if (recipeType==6){
			return new FoodItem(product_id, product_qty);
		}
		else if (recipeType==5){
			return new Item(product_id, product_qty); // TODO ArmorItem
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public String ingredientsAsString(){
		String ls = "";
		for (int i=0; i<ingredients.size(); i++){
			ls=ls.concat(ingredients.get(i).getQuantity()+" "+ingredients.get(i).getName());
			if (i==ingredients.size()-2){
				ls=ls.concat(" and ");
			} else if (i==ingredients.size()-1){
				//Add nothing, this is the last ingredient
			} else {
				ls=ls.concat(", ");
			}
		}
		return ls;
	}
	public void addIngredient(Integer id, Integer q){
		ingredients.add(new Item(id, q));
	}
	public void setProduct(Integer id, Integer q){
		product_id = id;
		product_qty = q;
	}
	public void setTime(Integer t){
		craftTime=t;
	}
	public Integer getCraftTime(){
		return craftTime;
	}
	public ArrayList<Item> getIngredients(){
		return ingredients;
	}
	public static final Recipe WORKBENCH = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 3);
			setTime(120);
		}
	};
	public static final Recipe HOUSE_SOD = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.DIRT, 10);
			setTime(120);
		}
	};
	public static final Recipe HOUSE_WOOD_SMALL = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 10);
			setTime(360);
		}
	};
	public static final Recipe HOUSE_STONE_SMALL = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 10);
			addIngredient(Item.CLAY, 4);
			addIngredient(Item.WOOD, 4);
			setTime(400);
		}
	};
	public static final Recipe HOUSE_WOOD_LARGE = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 25);
			addIngredient(Item.GLASS, 4);
			setTime(720);
		}
	};
	public static final Recipe HOUSE_STONE_LARGE = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 30);
			addIngredient(Item.CLAY, 15);
			addIngredient(Item.WOOD, 10);
			addIngredient(Item.GLASS, 10);
			setTime(800);
		}
	};
	public static final Recipe HOUSE_MANSION = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 75);
			addIngredient(Item.CLAY, 30);
			addIngredient(Item.WOOD, 25);
			addIngredient(Item.GLASS, 12);
			addIngredient(Item.IRON_INGOT, 4);
			setTime(1440);
		}
	};
	public static final Recipe HOUSE_CASTLE = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 100);
			addIngredient(Item.CLAY, 40);
			addIngredient(Item.WOOD, 30);
			addIngredient(Item.GLASS, 20);
			addIngredient(Item.IRON_INGOT, 6);
			setTime(2880);
		}
	};
	public static final Recipe HOUSE_FORTRESS = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 200);
			addIngredient(Item.BASALT, 50);
			addIngredient(Item.CLAY, 75);
			addIngredient(Item.WOOD, 50);
			addIngredient(Item.GLASS, 40);
			addIngredient(Item.STEEL_INGOT, 25);
			setTime(11520);
		}
	};
	public static final Recipe BONFIRE = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 2);
			addIngredient(Item.FLINT, 1);
			setTime(15);
		}
	};
	public static final Recipe SMELTER = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 6);
			addIngredient(Item.CLAY, 3);
			addIngredient(Item.FLINT, 1);
			setTime(90);
		}
	};
	public static final Recipe FARM_SMALL = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 2);
			addIngredient(Item.DIRT, 8);
			setTime(180);
		}
	};
	public static final Recipe FARM_MEDIUM = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 5);
			addIngredient(Item.DIRT, 20);
			setTime(300);
		}
	};
	public static final Recipe FARM_LARGE = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.WOOD, 10);
			addIngredient(Item.IRON_INGOT, 2);
			addIngredient(Item.DIRT, 40);
			setTime(480);
		}
	};
	public static final Recipe BREWERY = new Recipe(Recipe.BUILDING){
		{
			addIngredient(Item.STONE, 5);
			addIngredient(Item.GLASS, 3);
			addIngredient(Item.STEEL_INGOT, 1);
			setTime(90);
		}
	};
	public static final Recipe AXE_WOOD = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.WOOD, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.WOODEN_AXE, 1);
			setTime(30);
		}
	};
	public static final Recipe AXE_STONE = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STONE, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STONE_AXE, 1);
			setTime(30);
		}
	};
	public static final Recipe AXE_IRON = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.IRON_INGOT, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.IRON_AXE, 1);
			setTime(30);
		}
	};
	public static final Recipe AXE_STEEL = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STEEL_INGOT, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STEEL_AXE, 1);
			setTime(30);
		}
	};
	public static final Recipe AXE_DIAMOND = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.DIAMOND, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.DIAMOND_AXE, 1);
			setTime(30);
		}
	};
	public static final Recipe PICK_WOOD = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.WOOD, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.WOODEN_PICK, 1);
			setTime(30);
		}
	};
	public static final Recipe PICK_STONE = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STONE, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STONE_PICK, 1);
			setTime(30);
		}
	};
	public static final Recipe PICK_IRON = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.IRON_INGOT, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.IRON_PICK, 1);
			setTime(30);
		}
	};
	public static final Recipe PICK_STEEL = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STEEL_INGOT, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STEEL_PICK, 1);
			setTime(30);
		}
	};
	public static final Recipe PICK_DIAMOND = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.DIAMOND, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.DIAMOND_PICK, 1);
			setTime(30);
		}
	};
	public static final Recipe SHOVEL_WOOD = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.WOOD, 1);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.WOODEN_SHOVEL, 1);
			setTime(30);
		}
	};
	public static final Recipe SHOVEL_STONE = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STONE, 2);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STONE_SHOVEL, 1);
			setTime(30);
		}
	};
	public static final Recipe SHOVEL_IRON = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.IRON_INGOT, 1);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.IRON_SHOVEL, 1);
			setTime(30);
		}
	};
	public static final Recipe SHOVEL_STEEL = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STEEL_INGOT, 1);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STEEL_SHOVEL, 1);
			setTime(30);
		}
	};
	public static final Recipe SHOVEL_DIAMOND = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.DIAMOND, 1);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.DIAMOND_SHOVEL, 1);
			setTime(30);
		}
	};
	public static final Recipe HAMMER_STONE = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STONE, 3);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STONE_HAMMER, 1);
			setTime(30);
		}
	};
	public static final Recipe HAMMER_IRON = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.IRON_INGOT, 3);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.IRON_HAMMER, 1);
			setTime(30);
		}
	};
	public static final Recipe HAMMER_STEEL = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.STEEL_INGOT, 3);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.STEEL_HAMMER, 1);
			setTime(30);
		}
	};
	public static final Recipe HAMMER_DIAMOND = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.DIAMOND, 3);
			addIngredient(Item.STICK, 2);
			setProduct(ToolItem.DIAMOND_HAMMER, 1);
			setTime(30);
		}
	};
	public static final Recipe FISHING_ROD = new Recipe(Recipe.TOOL){
		{
			addIngredient(Item.SILK, 4);
			addIngredient(Item.STICK, 3);
			setProduct(ToolItem.FISHING_ROD, 1);
			setTime(30);
		}
	};
	public static final Recipe STICK = new Recipe(Recipe.REGULAR){
		{
			addIngredient(Item.WOOD, 1);
			setProduct(Item.STICK, 2);
			setTime(10);
		}
	};
	public static final Recipe BUCKET = new Recipe(Recipe.REGULAR){
		{
			addIngredient(Item.STEEL_INGOT, 3);
			setProduct(Item.BUCKET, 1);
			setTime(40);
		}
	};
	public static final Recipe COMPASS = new Recipe(Recipe.REGULAR){
		{
			addIngredient(Item.IRON_INGOT, 2);
			addIngredient(Item.WATER_BUCKET, 1);
			setProduct(Item.COMPASS, 1);
			setTime(25);
		}
	};
	public static final Recipe COMPASS_BASALT = new Recipe(Recipe.REGULAR){
		{
			addIngredient(Item.BASALT, 4);
			addIngredient(Item.WATER_BUCKET, 1);
			setProduct(Item.BASALT_COMPASS, 1);
			setTime(25);
		}
	};
	public static final Recipe ARROW = new Recipe(Recipe.REGULAR){
		{
			addIngredient(Item.STICK, 1);
			addIngredient(Item.FLINT, 1);
			setProduct(Item.ARROW, 3);
			setTime(15);
		}
	};
	public static final Recipe WEAPON_WOOD = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.WOOD, 2);
			addIngredient(Item.STICK, 1);
			setProduct(WeaponItem.WOODEN_CLUB, 1);
			setTime(30);
		}
	};
	public static final Recipe WEAPON_STONE = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.STONE, 1);
			addIngredient(Item.STICK, 2);
			setProduct(WeaponItem.STONE_CLUB, 1);
			setTime(30);
		}
	};
	public static final Recipe WEAPON_IRON = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.IRON_INGOT, 2);
			addIngredient(Item.STICK, 1);
			setProduct(WeaponItem.IRON_BLADE, 1);
			setTime(30);
		}
	};
	public static final Recipe WEAPON_STEEL = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.STEEL_INGOT, 2);
			addIngredient(Item.STICK, 1);
			setProduct(WeaponItem.STEEL_SWORD, 1);
			setTime(30);
		}
	};
	public static final Recipe WEAPON_DIAMOND = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.DIAMOND, 2);
			addIngredient(Item.STICK, 1);
			setProduct(WeaponItem.DIAMOND_SWORD, 1);
			setTime(30);
		}
	};
	public static final Recipe BOW = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.SILK, 3);
			addIngredient(Item.STICK, 3);
			setProduct(WeaponItem.BOW_BASIC, 1);
			setTime(30);
		}
	};
	public static final Recipe LONG_BOW = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.WOOD, 3);
			addIngredient(Item.SILK, 4);
			setProduct(WeaponItem.BOW_LONG, 1);
			setTime(30);
		}
	};
	public static final Recipe COMPOUND_BOW = new Recipe(Recipe.WEAPON){
		{
			addIngredient(Item.STEEL_INGOT, 3);
			addIngredient(Item.SILK, 5);
			setProduct(WeaponItem.BOW_COMPOUND, 1);
			setTime(30);
		}
	};
	public static final Recipe COOK_PORK = new Recipe(Recipe.COOKED){
		{
			addIngredient(FoodItem.PORK_RAW, 1);
			setProduct(FoodItem.PORK_COOKED, 1);
			setTime(10);
		}
	};
	public static final Recipe COOK_BEEF = new Recipe(Recipe.COOKED){
		{
			addIngredient(FoodItem.BEEF_RAW, 1);
			setProduct(FoodItem.BEEF_COOKED, 1);
			setTime(10);
		}
	};
	public static final Recipe COOK_FISH = new Recipe(Recipe.COOKED){
		{
			addIngredient(FoodItem.FISH_RAW, 1);
			setProduct(FoodItem.FISH_COOKED, 1);
			setTime(10);
		}
	};
	public static final Recipe COOK_BREAD = new Recipe(Recipe.COOKED){
		{
			addIngredient(Item.WHEAT, 1);
			setProduct(FoodItem.BREAD, 1);
			setTime(10);
		}
	};
	public static final Recipe GOLDEN_APPLE = new Recipe(Recipe.REGULAR){
		{
			addIngredient(FoodItem.APPLE, 1);
			addIngredient(Item.GOLD_INGOT, 2);
			setProduct(FoodItem.GOLDEN_APPLE, 1);
			setTime(20);
		}
	};
	public static final Recipe SMELT_IRON = new Recipe(Recipe.SMELT){
		{
			addIngredient(Item.IRON_ORE, 1);
			setProduct(Item.IRON_INGOT, 1);
			setTime(15);
		}
	};
	public static final Recipe SMELT_STEEL = new Recipe(Recipe.SMELT){ //This one is special because it requires different fuel
		{
			addIngredient(Item.IRON_INGOT, 1);
			setProduct(Item.STEEL_INGOT, 1);
			setTime(30);
		}
	};
	public static final Recipe SMELT_GOLD = new Recipe(Recipe.SMELT){
		{
			addIngredient(Item.GOLD_ORE, 1);
			setProduct(Item.GOLD_INGOT, 1);
			setTime(15);
		}
	};
	public static final Recipe MELT_COINS = new Recipe(Recipe.SMELT){
		{
			addIngredient(Item.COIN, 5);
			setProduct(Item.GOLD_INGOT, 1);
			setTime(15);
		}
	};
	public static final Recipe SMELT_GLASS = new Recipe(Recipe.SMELT){
		{
			addIngredient(Item.SAND, 1);
			setProduct(Item.GLASS, 1);
			setTime(15);
		}
	};
	public static final Recipe SMELT_CLAY = new Recipe(Recipe.SMELT){
		{
			addIngredient(Item.DIRT, 1);
			setProduct(Item.CLAY, 1);
			setTime(15);
		}
	};
}
