import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Player implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer health;
	private Integer energy;
	private Inventory inv;
	private HashMap<String, Integer> skills;
	
	@SuppressWarnings("serial")
	public Player(String n){
		skills = new HashMap<String, Integer>(){
			{
				put("Combat", 0);
				put("Strength", 0);
				put("Intelligence", 0);
				put("Dexterity", 0);
				put("Agility", 0);
				put("Charisma", 0);
			}
		};
		health=100;
		energy=100;
		inv = new Inventory(20);
		name=n;
	}
	public void setInitialSkills(Integer[] s){
		skills.put("Combat", s[0]);
		skills.put("Strength", s[1]);
		skills.put("Intelligence", s[2]);
		skills.put("Dexterity", s[3]);
		skills.put("Agility", s[4]);
		skills.put("Charisma", s[5]);
	}
	public void increaseSkill(String s){
		Integer lvl = skills.get(s);
		skills.put(s, lvl+1);
	}
	public void decreaseSkill(String s){
		Integer lvl = skills.get(s);
		skills.put(s, lvl-1);
	}
	public void setSkill(String s, Integer l){
		skills.put(s, l);
	}
	public Integer getHealth(){
		return health;
	}
	public Integer getEnergy(){
		return energy;
	}
	public Integer getSkill(String s){
		return skills.get(s);
	}
	public void damage(int d){
		health -= d;
		if (health<0){
			health=0;
		}
	}
	public void heal(int h){
		health += h;
		if (health>100){
			health=100;
		}
	}
	public void loseEnergy(int e){
		energy -= e;
		if (energy<0){
			energy=0;
		}
	}
	public void addEnergy(int e){
		energy += e;
		if (energy>100){
			energy=100;
		}
	}
	public Inventory getInventory(){
		return inv;
	}
	public String getName(){
		return name;
	}
	
	@SuppressWarnings("serial")
	public static HashMap<String, ArrayList<String>> skillTerms = new HashMap<String, ArrayList<String>>(){
		{
			put("Combat", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Weenie");
					add(2, "Wimp");
					add(3, "Weak");
					add(4, "Trainee");
					add(5, "Apprentice");
					add(6, "Hardy");
					add(7, "Fighter");
					add(8, "Warrior");
					add(9, "Brutal");
					add(10, "Master");
				}
			});
			put("Strength", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Baby");
					add(2, "Scrawny");
					add(3, "Weakling");
					add(4, "Average");
					add(5, "Strong");
					add(6, "Ripped");
					add(7, "Burly");
					add(8, "Jerk");
					add(9, "Olympian");
					add(10, "Godlike");
				}
			});
			put("Intelligence", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Air-Head");
					add(2, "Foolish");
					add(3, "Dropout");
					add(4, "Dim");
					add(5, "Competent");
					add(6, "Bright");
					add(7, "Survivalist");
					add(8, "Clever");
					add(9, "Wise");
					add(10, "Genius");
				}
			});
			put("Dexterity", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Klutz");
					add(2, "Clumsy");
					add(3, "Unwieldy");
					add(4, "Heavy-Handed");
					add(5, "Regular");
					add(6, "Coordinated");
					add(7, "Handyman");
					add(8, "Adept");
					add(9, "Expert");
					add(10, "Super-human");
				}
			});
			put("Agility", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Lethargic");
					add(2, "Bumbling");
					add(3, "Slow");
					add(4, "Out-of-shape");
					add(5, "Active");
					add(6, "Quick");
					add(7, "Energetic");
					add(8, "Fast");
					add(9, "Sprinter");
					add(10, "Matrix");
				}
			});
			put("Charisma", new ArrayList<String>(){
				{
					add(0, null);
					add(1, "Repugnant");
					add(2, "Unalluring");
					add(3, "Outcast");
					add(4, "Shy");
					add(5, "Social");
					add(6, "Haggler");
					add(7, "Merchant");
					add(8, "Charming");
					add(9, "Persuasive");
					add(10, "Manipulative");
				}
			});
		}
	};
}
