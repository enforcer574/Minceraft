import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
public class Farm implements Iterable<Integer>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int size;
	private HashMap<Integer, Integer> crops;
	private HashMap<Integer, Integer> readyTimes;
	public Farm(Integer s){
		size=s;
		crops=new HashMap<Integer, Integer>();
		readyTimes = new HashMap<Integer, Integer>();
		for (int i=0; i<s; i++){
			crops.put(i, null);
			readyTimes.put(i, null);
		}
	}
	/*
	 * Returns the number of null slots in the farm.
	 */
	public Integer openSlots(){
		Integer sum = 0;
		for (int i=0; i<crops.size(); i++){
			if (crops.get(i)==null){
				sum++;
			}
		}
		return sum;
	}
	public boolean isFull(){
		if(openSlots()==0){
			return true;
		} else {
			return false;
		}
	}
	public void setSize(Integer s){
		size=s;
		for (int i=crops.size(); i<s; i++){
			crops.put(i, null);
			readyTimes.put(i, null);
		}
	}
	public Integer firstAvailableSlot(){
		for (int i=0; i<crops.size(); i++){
			if (crops.get(i)==null){
				return i;
			}
		}
		return null;
	}
	public void plant(Integer id){
		if(this.isFull()){
			return;
		}
		Integer s = firstAvailableSlot();
		crops.put(s, id);
		Integer rt = Minceraft.time + Item.growTime.get(id) + Minceraft.randInt(-300, 300);
		readyTimes.put(s, rt);
	}
	public Item harvest(int slot){
		Item yield = null;
		if (isReady(slot)){
			if (crops.get(slot)==Item.SEED){
				yield = new Item(Item.WHEAT, 4);
			}
			else if (crops.get(slot)==Item.SAPLING){
				yield = new Item(Item.WOOD, 6);
			}
			else if (crops.get(slot)==FoodItem.STRAWBERRY){
				yield = new FoodItem(FoodItem.STRAWBERRY, 3);
			}
			else if (crops.get(slot)==FoodItem.COCOA){
				yield = new FoodItem(FoodItem.COCOA, 5);
			}
			else if (crops.get(slot)==FoodItem.CACTUS){
				yield = new FoodItem(FoodItem.CACTUS, 4);
			} else {
				yield = new Item(crops.get(slot), 1);
			}
		} else {
			yield = new Item(crops.get(slot), 1);
		}
		crops.put(slot, null);
		readyTimes.put(slot, null);
		return yield;
	}
	public Integer getCrop(Integer slot){
		return crops.get(slot);
	}
	public int getSize(){
		return size;
	}
	public int getCropCount(){
		return crops.size() - openSlots();
	}
	public boolean isReady(Integer slot){
		if (Minceraft.time >= readyTimes.get(slot)){
			if (Minceraft.options[2]){
				Minceraft.print("[DEBUG] isReady check for slot "+slot+" returned true");
			}
			return true;
		} else {
			if (Minceraft.options[2]){
				Minceraft.print("[DEBUG] isReady check for slot "+slot+" returned false");
			}
			return false;
		}
	}
	@Override
	public Iterator<Integer> iterator() {
		return crops.values().iterator();
	}
}
