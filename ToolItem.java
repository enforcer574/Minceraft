import java.util.HashMap;


public class ToolItem extends Item{
	protected Integer durability;
	
	public static final int WOODEN_AXE = 30;
	public static final int WOODEN_PICK = 34;
	public static final int WOODEN_SHOVEL = 38;
	public static final int STONE_AXE = 31;
	public static final int STONE_PICK = 35;
	public static final int STONE_SHOVEL = 39;
	public static final int IRON_AXE = 32;
	public static final int IRON_PICK = 36;
	public static final int IRON_SHOVEL = 40;
	public static final int DIAMOND_AXE = 33;
	public static final int DIAMOND_PICK = 37;
	public static final int DIAMOND_SHOVEL = 41;
	public static final int STEEL_AXE = 49;
	public static final int STEEL_PICK = 50;
	public static final int STEEL_SHOVEL = 51;
	public static final int FISHING_ROD = 53;
	public static final int OBSIDIAN_PICK = 59;
	public static final int STONE_HAMMER = 77;
	public static final int IRON_HAMMER = 78;
	public static final int STEEL_HAMMER = 79;
	public static final int DIAMOND_HAMMER = 80;
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, Integer> defaultDurability = new HashMap<Integer, Integer>(){
		{
			put(ToolItem.WOODEN_AXE, 50);
			put(ToolItem.WOODEN_PICK, 40);
			put(ToolItem.WOODEN_SHOVEL, 50);
			put(ToolItem.STONE_AXE, 100);
			put(ToolItem.STONE_PICK, 80);
			put(ToolItem.STONE_SHOVEL, 100);
			put(ToolItem.STONE_HAMMER, 80);
			put(ToolItem.IRON_AXE, 250);
			put(ToolItem.IRON_PICK, 200);
			put(ToolItem.IRON_SHOVEL, 250);
			put(ToolItem.IRON_HAMMER, 200);
			put(ToolItem.STEEL_AXE, 400);
			put(ToolItem.STEEL_PICK, 320);
			put(ToolItem.STEEL_SHOVEL, 400);
			put(ToolItem.STEEL_HAMMER, 320);
			put(ToolItem.DIAMOND_AXE, 1000);
			put(ToolItem.DIAMOND_PICK, 800);
			put(ToolItem.DIAMOND_SHOVEL, 1000);
			put(ToolItem.DIAMOND_HAMMER, 800);
			put(ToolItem.FISHING_ROD, 300);
			put(ToolItem.OBSIDIAN_PICK, 2400);
		}
	};
	public ToolItem(Integer i, Integer q){
		super(i, q);
		durability = defaultDurability.get(i);
	}
	public ToolItem(Integer i, Integer q, Integer d){
		super(i, q);
		durability = d;
	}
	public void damage(Integer d){
		durability -= d;
		if (durability<=0){ //The tool breaks
			qty--;
			durability = defaultDurability.get(id);
			Minceraft.toolBreak(this);
		}
	}
	public void repair(Integer d){
		durability += d;
		if (durability > defaultDurability.get(id)){
			durability = defaultDurability.get(id);
		}
	}
	public Integer getDurability(){
		return durability;
	}
	public Integer getWearPercent(){
		Double a = new Double(durability);
		Double b = new Double(defaultDurability.get(this.id()));
		double c = (a / b) * 100.0;
		Integer x = (int)c;
		if (Minceraft.options[2]){
			Minceraft.print("[DEBUG] Tool use left: "+durability);
			Minceraft.print("[DEBUG] Default value: "+defaultDurability.get(this.id()));
			Minceraft.print("[DEBUG] Divided value: "+x);
		}
		return 100 - x;
		
	}
}
