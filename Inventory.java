import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Inventory implements Iterable<Item>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> inv;
	private Integer max;
	
	public Inventory(int m){
		max=m;
		inv = new ArrayList<Item>();
	}
	public void addItem(Item i){
		if (i.getQuantity()+this.getTotalItems() > max){
			i.updateQuantity(max-this.getTotalItems()); //Shrink the stack if the inventory is full
		}
		if (this.hasItem(i.id())){
			this.getItem(i.id()).updateQuantity(i.getQuantity());
		} else {
			inv.add(i);
		}
	}
	public boolean hasItem(Integer id){
		for (Item i : inv){
			if (i.id()==id){
				return true;
			}
		}
		return false;
	}
	public boolean hasItem(Integer id, Integer q){
		for (Item i : inv){
			if (i.id()==id && i.getQuantity()>=q){
				return true;
			}
		}
		return false;
	}
	public boolean hasItem(String n){
		for (Item i : inv){
			if (i.getName().equalsIgnoreCase(n)){
				return true;
			}
		}
		return false;
	}
	public Item getItem(Integer id){
		for (Item i : inv){
			if (i.id()==id){
				return i;
			}
		}
		return null;
	}
	public Item getItem(String n){
		for (Item i : inv){
			if (i.getName().equalsIgnoreCase(n)){
				return i;
			}
		}
		return null;
	}
	public void removeItem(Integer id, Integer q){
		ArrayList<Item> toRemove = new ArrayList<Item>();
		for (Item i : inv){
			if (i.id()==id){
				i.updateQuantity(q*-1);
			}
			if (i.getQuantity()==0){
				toRemove.add(i);
			}
		}
		inv.removeAll(toRemove);
	}
	public ArrayList<Item> toItemArray(){
		return inv;
	}
	public Integer getTotalUniqueItems(){
		return inv.size();
	}
	public Integer getTotalItems(){
		Integer sum = 0;
		for (Item i : inv){
			sum += i.getQuantity();
		}
		return sum;
	}
	public Integer getMaxItems(){
		return max;
	}
	public void setMaxItems(Integer m){
		max=m;
	}
	public boolean isFull(){
		if(this.getTotalItems()==max){
			return true;
		} else {
			return false;
		}
	}
	public void flushZeroStacks(){
		ArrayList<Item> toRemove = new ArrayList<Item>();
		for (Item i : inv){
			if (i.getQuantity()==0){
				toRemove.add(i);
			}
		}
		inv.removeAll(toRemove);
	}
	public ToolItem strongestHammer(){
		if (this.hasItem(ToolItem.DIAMOND_HAMMER)){
			return (ToolItem) this.getItem(ToolItem.DIAMOND_HAMMER);
		}
		else if (this.hasItem(ToolItem.STEEL_HAMMER)){
			return (ToolItem) this.getItem(ToolItem.STEEL_HAMMER);
		}
		else if (this.hasItem(ToolItem.IRON_HAMMER)){
			return (ToolItem) this.getItem(ToolItem.IRON_HAMMER);
		}
		else if (this.hasItem(ToolItem.STONE_HAMMER)){
			return (ToolItem) this.getItem(ToolItem.STONE_HAMMER);
		} else {
			return null;
		}
	}
	public ToolItem strongestShovel(){
		if (this.hasItem(ToolItem.DIAMOND_SHOVEL)){
			return (ToolItem) this.getItem(ToolItem.DIAMOND_SHOVEL);
		}
		else if (this.hasItem(ToolItem.STEEL_SHOVEL)){
			return (ToolItem) this.getItem(ToolItem.STEEL_SHOVEL);
		}
		else if (this.hasItem(ToolItem.IRON_SHOVEL)){
			return (ToolItem) this.getItem(ToolItem.IRON_SHOVEL);
		}
		else if (this.hasItem(ToolItem.STONE_SHOVEL)){
			return (ToolItem) this.getItem(ToolItem.STONE_SHOVEL);
		}
		else if (this.hasItem(ToolItem.WOODEN_SHOVEL)){
			return (ToolItem) this.getItem(ToolItem.WOODEN_SHOVEL);
		} else {
			return null;
		}
	}
	@Override
	public Iterator<Item> iterator() {
		Iterator<Item> i = inv.iterator();
		return i;
	}
}
