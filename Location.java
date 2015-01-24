import java.io.Serializable;
import java.util.HashMap;

public class Location implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer type;
	private Integer x;
	private Integer y;
	private HashMap<String, Boolean> buildings;
	
	private Integer houseSize;
	private Integer farmSize;
	
	private Inventory houseStorage;
	private Inventory resources;
	private Farm farm;
	
	public Location(Integer t, Integer a, Integer b){
		type=t;
		x=b;
		y=a;
		buildings = new HashMap<String, Boolean>();
		buildings.put("Workbench", false);
		buildings.put("Fire", false);
		buildings.put("House", false);
		buildings.put("Smelter", false);
		buildings.put("Farm", false);
		buildings.put("Potion Brewery", false);
		
		resources = new Inventory(10000);
		setHouseLevel(0); //Also constructs the house inventory
		setFarmLevel(0); //Also constructs th
		this.addResources();
		
		int count=0;
		for (Location l : Minceraft.map){
			if (l.getName().contains("Unnamed "+this.getBiome())){
				count++;
			}
		}
		name="Unnamed "+this.getBiome()+" #"+(count+1);
	}
	
	public Integer getTravelTime(Location l){
		int a = Math.abs(l.getX() - this.x);
		int b = Math.abs(l.getY() - this.y);
		
		return (int) Math.round(Math.sqrt((a*a)+(b*b))/4);
	}
	public Integer getX(){
		return x;
	}
	public Integer getY(){
		return y;
	}
	public Integer getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public void setName(String n){
		name=n;
	}
	public String getBiome(){
		if (type==0){
			return "Desert";
		}
		else if (type==1){
			return "Forest";
		}
		else if (type==2){
			return "Plains";
		}
		else if (type==3){
			return "Jungle";
		}
		else if (type==4){
			return "Mountains";
		}
		else if (type==5){
			return "Wetlands";
		}
		else if (type==6){
			return "Tundra";
		}
		else if (type==7){
			return "River";
		}
		else if (type==8){
			return "Cave";
		}
		else if (type==9){
			return "Big Cave";
		}
		else if (type==10){
			return "Village";
		}
		else if (type==11){
			return "Volcano";
		}
		else if (type==12){
			return "Stronghold";
		}
		else if (type==13){
			return "Altar";
		}
		else {
			return "Unknown Type (Game bug or messing with cheats?)";
		}
	}
	public void addBuilding(String b){
		buildings.put(b, true);
	}
	public boolean hasBuilding(String b){
		return buildings.get(b);
	}
	public Integer getFarmLevel(){
		return farmSize;
	}
	public Integer getHouseLevel(){
		return houseSize;
	}
	public void setFarmLevel(Integer l){
		farmSize=l;
		if (l==0){
			farm=new Farm(0);
		}
		else if (l==1){ //Small farm
			farm.setSize(8);
		}
		else if (l==2){ //Medium farm
			farm.setSize(24);
		}
		else if (l==3){ //Large farm
			farm.setSize(60);
		}
	}
	public void setHouseLevel(Integer l){
		houseSize=l;
		if (l==0){ //No house or house destroyed
			houseStorage=new Inventory(0);
		}
		else if (l==1){ //Sod Hut
			houseStorage.setMaxItems(50);
		}
		else if (l==2){ //Wooden Shack
			houseStorage.setMaxItems(120);
		}
		else if (l==3){ //Stone Cottage
			houseStorage.setMaxItems(100);
		}
		else if (l==4){ //Log Cabin
			houseStorage.setMaxItems(200);
		}
		else if (l==5){ //Stone House
			houseStorage.setMaxItems(150);
		}
		else if (l==6){ //Mansion
			houseStorage.setMaxItems(250);
		}
		else if (l==7){ //Castle
			houseStorage.setMaxItems(500);
		}
		else if (l==8){ //Fortress
			houseStorage.setMaxItems(1000);
		}
	}
	public Inventory getResourceCache(){
		return resources;
	}
	public Inventory getHouseInventory(){
		return houseStorage;
	}
	public Farm getFarm(){
		return farm;
	}
	public void plantCrop(Item i){
		
	}
	public static final int DESERT = 0;
	public static final int FOREST = 1;
	public static final int PLAINS = 2;
	public static final int JUNGLE = 3;
	public static final int MOUNTAINS = 4;
	public static final int WETLANDS = 5;
	public static final int TUNDRA = 6;
	
	public static final int RIVER = 7;
	
	public static final int CAVE_SMALL = 8;
	public static final int CAVE_LARGE = 9;
	
	public static final int VILLAGE = 10;
	public static final int VOLCANO = 11;
	public static final int STRONGHOLD = 12;
	public static final int ALTAR = 13;
	
	public void addResources(){
		if (this.getType()==Location.DESERT){
			resources.addItem(new Item(Item.SAND, Minceraft.randInt(20, 50)));
			resources.addItem(new Item(FoodItem.CACTUS, Minceraft.randInt(5, 10)));
		}
		else if (this.getType()==Location.FOREST){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(15, 30)));
			resources.addItem(new Item(Item.DIRT, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(FoodItem.APPLE, Minceraft.randInt(5, 15)));
			resources.addItem(new Item(FoodItem.STRAWBERRY, Minceraft.randInt(5, 10)));
			resources.addItem(new Item(Item.SEED, Minceraft.randInt(0, 10)));
			resources.addItem(new Item(Item.FLINT, Minceraft.randInt(0, 3)));
		}
		else if (this.getType()==Location.PLAINS){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(8, 15)));
			resources.addItem(new Item(Item.DIRT, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(Item.SEED, Minceraft.randInt(15, 25)));
			resources.addItem(new Item(FoodItem.STRAWBERRY, Minceraft.randInt(0, 8)));
			resources.addItem(new Item(Item.FLINT, Minceraft.randInt(0, 3)));
		}
		else if (this.getType()==Location.JUNGLE){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(20, 35)));
			resources.addItem(new Item(Item.DIRT, Minceraft.randInt(5, 15)));
			resources.addItem(new Item(FoodItem.STRAWBERRY, Minceraft.randInt(0, 10)));
			resources.addItem(new Item(FoodItem.COCOA, Minceraft.randInt(3, 12)));
		}
		else if (this.getType()==Location.MOUNTAINS){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(5, 10)));
			resources.addItem(new Item(Item.DIRT, Minceraft.randInt(5, 15)));
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(25, 30)));
			resources.addItem(new Item(Item.SEED, Minceraft.randInt(0, 5)));
			resources.addItem(new Item(Item.FLINT, Minceraft.randInt(3, 8)));
			resources.addItem(new Item(Item.COAL, Minceraft.randInt(3, 8)));
		}
		else if (this.getType()==Location.WETLANDS){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(8, 15)));
			resources.addItem(new Item(Item.DIRT, Minceraft.randInt(0, 10)));
			resources.addItem(new Item(Item.CLAY, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(5, 10)));
			resources.addItem(new Item(FoodItem.STRAWBERRY, Minceraft.randInt(0, 5)));
			resources.addItem(new FoodItem(FoodItem.FISH_RAW, Minceraft.randInt(2, 5)));
		}
		else if (this.getType()==Location.TUNDRA){
			resources.addItem(new Item(Item.WOOD, Minceraft.randInt(0, 8)));
		}
		else if (this.getType()==Location.RIVER){
			resources.addItem(new Item(Item.CLAY, Minceraft.randInt(15, 25)));
			resources.addItem(new FoodItem(FoodItem.FISH_RAW, Minceraft.randInt(10, 20)));
		}
		else if (this.getType()==Location.CAVE_SMALL){
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(20, 30)));
			resources.addItem(new Item(Item.FLINT, Minceraft.randInt(5, 10)));
			resources.addItem(new Item(Item.COAL, Minceraft.randInt(10, 20)));
			resources.addItem(new Item(Item.IRON_ORE, Minceraft.randInt(5, 10)));
		}
		else if (this.getType()==Location.CAVE_LARGE){
			resources.addItem(new Item(Item.STONE, Minceraft.randInt(20, 40)));
			resources.addItem(new Item(Item.FLINT, Minceraft.randInt(5, 15)));
			resources.addItem(new Item(Item.COAL, Minceraft.randInt(15, 30)));
			resources.addItem(new Item(Item.IRON_ORE, Minceraft.randInt(15, 30)));
			resources.addItem(new Item(Item.GOLD_ORE, Minceraft.randInt(5, 15)));
			resources.addItem(new Item(Item.DIAMOND, Minceraft.randInt(3, 10)));
		}
		else if (this.getType()==Location.VOLCANO){
			resources.addItem(new Item(Item.BASALT, Minceraft.randInt(15, 30)));
			resources.addItem(new Item(Item.OBSIDIAN, Minceraft.randInt(1, 3)));
		}
		resources.flushZeroStacks();
	}
}
