import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Minceraft {
	private static final String version = "ALPHA 0.3.1";
	protected static Scanner input = new Scanner(System.in);
	protected static Integer time;
	protected static ArrayList<Location> map;
	protected static ArrayList<Recipe> craftRegistry;
	protected static ArrayList<Recipe> cookRegistry;
	protected static ArrayList<Recipe> smeltRegistry;
	protected static Location currentLocation;
	protected static Player player;
	protected static Integer difficulty;
	public static boolean[] options;
	private static boolean run;
	
	private static String checkForUpdate(){
		print("Checking for updates...");
		String upd = null;
		final String readUrl = "https://dl.dropboxusercontent.com/u/53222456/minceraft-repo/update.txt";
		try {
			URL url = new URL(readUrl);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			upd = br.readLine();
			if (upd.equals(version)){
				return null;
			} else {
				return upd;
			}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void main(String[] args){
		print("Starting Minceraft version "+version+"...");
		print("Loading game data...");
		loadRecipes();
		options = new boolean[4];
		options[0] = false; //autosave
		options[1] = false; //page-like scrolling in Windows
		options[2] = false; //debug-verbose mode
		options[3] = false; //cheats
		
		String v = checkForUpdate();
		if (v!=null){
			print("NOTICE: An update is available:");
			print("MINCERAFT "+v);
			print("Visit the game page to download the new version.");
			print("Always keep up with the latest version of the game!");
			pause();
		}
		
		print("WARNING:");
		print("The game you are about to play is pretty cool. But it's nowhere near done!");
		print("This is an ALPHA. Not everything has been implemented yet.");
		print("Use this version for TESTING PURPOSES, but let us know if you find any bugs!");
		print("We hope you'll look forward to playing the finished version!");
		pause();
		
		print("  __  __  _____  _   _   _____  ______  _____             ______  _______ ");
		print(" |  \\/  ||_   _|| \\ | | / ____||  ____||  __ \\     /\\    |  ____||__   __|");
		print(" | \\  / |  | |  |  \\| || |     | |__   | |__) |   /  \\   | |__      | |   ");
		print(" | |\\/| |  | |  | . ` || |     |  __|  |  _  /   / /\\ \\  |  __|     | |   ");
		print(" | |  | | _| |_ | |\\  || |____ | |____ | | \\ \\  / ____ \\ | |        | |   ");
		print(" |_|  |_||_____||_| \\_| \\_____||______||_|  \\_\\/_/    \\_\\|_|        |_|   ");
		printEmptyLines(1);
		print("Minceraft Alpha | Version 0.3.1 | Developer Snapshot");
		printEmptyLines(1);
		print("Explore the world, fight to survive, solve the puzzle of the");
		print("blocky universe and the mysterious Herobrine");
		printEmptyLines(5);
		print("Written and developed by Derek Elam");
		print("This game is not affiliated with Minecraft or Mojang in any way.");
		printEmptyLines(5);
		print("Begin Game [NEW / LOAD / TEST / QUIT] :");
		String in = input.nextLine();
		if(in.equalsIgnoreCase("NEW")){
			newGame();
		}
		else if(in.equalsIgnoreCase("LOAD")){
			print("Load which character?");
			String filename = input.nextLine();
			loadGame(filename);
		}
		else if(in.equalsIgnoreCase("TEST")){
			player = new Player("Debugger");
			map = new ArrayList<Location>();
			map.add(new Location(Location.FOREST, 5000, 5000));
			currentLocation = map.get(0);
			Integer[] skl = {10, 10, 10, 10, 10, 10};
			player.setInitialSkills(skl);
			difficulty = 0;
			options[2] = true;
			options[3] = true;
			time = 0;
			runGame();
		}
	}
	
	private static void fish(Integer h){
		if(currentLocation.getType()==Location.RIVER){
			if (player.getInventory().hasItem(ToolItem.FISHING_ROD)){
				Integer f = 0;
				for (int i=0; i<h; i++){
					f += randInt(0, 3);
				}
				print("You take your rod and fish in the river for "+h+" hours.");
				if (f==0 || currentLocation.getResourceCache().hasItem(FoodItem.FISH_RAW)==false){
					print("To your frustration, You don't get any bites.");
					print("Maybe it's rotten luck, or maybe you need to find a different river.");
					return;
				}
				if (f > currentLocation.getResourceCache().getItem(FoodItem.FISH_RAW).getQuantity()){
					f = currentLocation.getResourceCache().getItem(FoodItem.FISH_RAW).getQuantity();
				}
				print("You catch "+f+" fish.");
				currentLocation.getResourceCache().removeItem(FoodItem.FISH_RAW, f);
				player.getInventory().addItem(new FoodItem(FoodItem.FISH_RAW, f));
			} else {
				print("You need a fishing rod first.");
				print("You'll need something to use for a line. What could that be...?");
				return;
			}
		} else {
			print("You can hear water flowing in the distance, but you can't tell from where.");
			print("If you explored for a few hours, you might find it...");
			return;
		}
	}
	
	private static void loadGame(String f){
		try {
			FileManager.load(f);
			difficulty = FileManager.loadedDiff;
			time = FileManager.loadedTime;
			map = FileManager.loadedMap;
			player = FileManager.loadedPlayer;
			currentLocation = map.get(0); //if location isn't found, default to location 0 instead of a NullPointerException
			for (Location l : map){
				if (l.getName().equals(FileManager.loadedLocationName)){
					currentLocation = l;
				}
			}
			runGame();
		} catch (IOException e){
			print("File Not Found.");
		}
	}
	
	private static void newGame(){
		boolean cont = true;
		String pn = "Herobrine";
		do {
		cont=true;
		print("Name your character:");
		pn = input.nextLine();
		File saveDir = new File("saves");
		File[] list = saveDir.listFiles();
		for (File f : list){
			if (f.getPath().equals("saves\\"+pn+".sav")){
				print("WARNING: A save file with that name already exists!");
				print("If you continue, saving will overwrite the existing file.");
				if (confirm("Continue with this name?")){
					cont=true;
				} else {
					cont=false;
				}
			}
		}
		} while (cont==false);
		player = new Player(pn);
		String d = null;
		boolean v = false;
		do {
			v=false;
			print("Select difficulty (Peaceful, Easy, Normal, Hard, Hardcore)");
			d = input.nextLine();
			if (d.equalsIgnoreCase("Peaceful")){
				difficulty=0;
				print("On Peaceful, there are no monsters. Use this difficulty to get used to the game.");
				v=true;
			}
			if (d.equalsIgnoreCase("Easy")){
				difficulty=1;
				print("On Easy, monsters are scarce, crops grow quickly, and your tools last longer.");
				v=true;
			}
			if (d.equalsIgnoreCase("Normal")){
				difficulty=2;
				print("On Normal, monsters will attack you, crops grow normally, and tools break after some use.");
				v=true;
			}
			if (d.equalsIgnoreCase("Hard")){
				difficulty=3;
				print("On Hard, monsters are everywhere, crops grow slowly, and your tools don't last as long.");
				v=true;
			}
			if (d.equalsIgnoreCase("Hardcore")){
				difficulty=4;
				print("On Hardcore, you play the game on Hard in one life only! There's no saving, and when you die, it's all over!");
				v=true;
			}
		} while (v==false || confirm("OK?")==false);
		map = new ArrayList<Location>();
		Integer t = randInt(0, 9);
		map.add(new Location(t, 5000, 5000));
		currentLocation = map.get(0);
		time = 0;
		printEmptyLines(25);
		
		print("SKILLS");
		printEmptyLines(2);
		print("COMBAT: You deal more damage in battle and your attacks are more accurate.");
		print("STRENGTH: You mine, build, and chop things faster, using less energy.");
		print("INTELLIGENCE: You fish, explore, and mine valuables more effectively.");
		print("DEXTERITY: You won't injure yourself as much using sharp tools.");
		print("AGILITY: You won't get hurt while exploring, and you can run faster.");
		print("CHARISMA: Local villagers offer you better trades.");
		printEmptyLines(12);
		boolean valid = true;
		Integer[] skillvalues = new Integer[6];
		do {
		valid=true;
		print("Enter six numbers from 1-10, separated by commas");
		print("For Combat, Strength, Intelligence, Dexterity, Agility, and Charisma.");
		print("The sum of your starting skills can't be higher than 30.");
		String sk = input.nextLine();
		String[] skillstring = sk.split(",");
		try {
		for (int i=0; i<6; i++){
			skillvalues[i] = Integer.parseInt(skillstring[i]);
			if (skillvalues[i] > 10 || skillvalues[i] < 1){
				print("Error: No skill may be lower than 1 or higher than 10.");
				valid=false;
			}
		}
		int sum = 0;
		for (int i=0; i<6; i++){
			sum += skillvalues[i];
		}
		if (sum>30){
			print("Error: The sum of all skills cannot exceed 30.");
			valid=false;
		}
		} catch (Exception e) {
			print("Formatting error. Enter six numbers, with commas in between.");
			valid=false;
		}
		} while (valid==false);
		player.setInitialSkills(skillvalues);
		
		print(player.getName().toUpperCase()+": THE ADVENTURE BEGINS");
		printEmptyLines(1);
		print("The last thing you remember is falling asleep.");
		print("You awake, slumped against a square block of dirt.");
		print("The sun is just beginning to climb above the horizon.");
		print("You look down at your arm and notice something odd... it's square.");
		print("In fact, everything is. Your body, the ground, the sun...");
		print("It's all blocky and rectangular.");
		printEmptyLines(15);
		pause();
		print("You look around at your surroundings.");
		printEmptyLines(1);
		if (currentLocation.getType()==Location.DESERT){
			print("There isn't much to see but sand and some cacti.");
			print("And it's hot... really hot.");
			print("There's no sign of other life out here, but");
			print("you're going to need food soon.");
		}
		else if (currentLocation.getType()==Location.FOREST){
			print("You're surrounded by blocky, square trees.");
			print("The ground is dotted with flowers and various plants.");
			print("You can hear what sounds like animals rustling leaves.");
			print("With wood for fire and animals for food, how hard can this be?");
		}
		else if (currentLocation.getType()==Location.PLAINS){
			print("There's a gentle breeze and the sound of rustling grass.");
			print("Rolling hills and a few square trees dot the landscape.");
			print("Herds of cows roam the plains, grazing on the tall grass.");
			print("They'd make a nice meal, but they'll fight back...");
		}
		else if (currentLocation.getType()==Location.JUNGLE){
			print("You can't see far through the dense fog and thick, green foliage.");
			print("You're surrounded by giant square trees, hundreds of feet high.");
			print("You hear what sounds like a pig's grunt in the distance.");
			print("Navigating this thick jungle isn't going to be easy.");
		}
		else if (currentLocation.getType()==Location.MOUNTAINS){
			print("There's a strong wind blowing. You're on top of a blocky mountain.");
			print("The ground is made mostly of stone, with a few scattered trees.");
			print("You can see herds of sheep wandering the hillside.");
			print("The wind is cold. You're going to need a fire before nightfall.");
		}
		else if (currentLocation.getType()==Location.WETLANDS){
			print("The air is warm and humid. Almost steamy.");
			print("The ground is made of soft clay and small stagnant pools of water.");
			print("The trees are covered in flat vines. It's some sort of swamp.");
			print("Making shelter on the soft ground will be tough.");
		}
		else if (currentLocation.getType()==Location.TUNDRA){
			print("All you can see is snow-covered hills and a few trees.");
			print("It's snowing hard, and it's very cold.");
			print("You decide you either need to make a fire, or find shelter.");
			print("The sonner you leave this frozen wasteland, the better.");
		}
		else if (currentLocation.getType()==Location.CAVE_SMALL){
			print("It's dark, but you can see daylight nearby. You're in a cave.");
			print("You see what looks like coal embedded in the stone walls.");
			print("You hear a strange hissing noise from deeper down.");
			print("Best to get out of here quick and find shelter... for now.");
		}
		else if (currentLocation.getType()==Location.CAVE_LARGE){
			print("It's almost too dark to see. You head towards the only light.");
			print("It's a square lava stream, pouring down into a huge cavern.");
			print("You can see glimmering gemstones in the rock down below.");
			print("A deep groaning noise from below catches your attention.");
		}
		else if (currentLocation.getType()==Location.RIVER){
			print("It's foggy. You hear water flowing nearby.");
			print("You're standing on the shore of a wide, deep river.");
			print("There are a few animals nearby drinking from it.");
			print("You feel hungry. A nice fish sounds great...");
		}
		printEmptyLines(1);
		print("Suddenly, you see something out of the corner of your eye.");
		print("Looking back at you from afar is another blocky person.");
		print("But something isn't right. He has no pupils in his eyes.");
		print("Not knowing what else to do, you give chase,");
		print("but he disappears as quickly as he came.");
		print("Must have been a hallucination.");
		print("You're on your own again. Best get to work making a shelter...");
		printEmptyLines(9);
		if (confirm("Would you like to read the tutorial?")){
			showTutorial();
		}
		runGame();
	}
	
	private static void runGame(){
		Scanner cmdscan;
		String cmd;
		run=true;
		while (run){
			cmdscan = new Scanner(System.in);
			printEmptyLines(25);
			showMainGameScreen();
			cmd = cmdscan.nextLine();
			parseCommand(cmd);
			pause();
		}
		main(null);
	}

	private static void parseCommand(String cmd){
		String[] words = cmd.split(" "); //Split the command into an array of words.
		//----------------------------------------------------------------------------------------------------------
		if (words[0].equalsIgnoreCase("turn") && words.length==3){
			int optIndex = 0;
			boolean optState = false;
			if (cmd.toLowerCase().contains("on")){ //Look for the word ON or OFF, and abort if neither is present
				optState = true;
			}
			else if (cmd.toLowerCase().contains("off")){
				optState = false;
			} else {
				print("Command Error: ON or OFF not specified.");
				return;
			}
			if (cmd.toLowerCase().contains("cheats")){ //Look for names of valid options, and abort if there are none
				optIndex = 3;
			}
			else if (cmd.toLowerCase().contains("debug")){
				optIndex = 2;
			}
			else if (cmd.toLowerCase().contains("clearing")){
				optIndex = 1;
			}
			else if (cmd.toLowerCase().contains("autosave")){
				optIndex = 0;
			} else {
				print("Command Error: Invalid option name.");
				print("Available game options are debug, autosave, and clearing.");
				return;
			}
			
			setOption(optIndex, optState);
		}
		//----------------------------------------------------------------------------------------------------------
		else if ((words[0].equalsIgnoreCase("view") || words[0].equalsIgnoreCase("check")) && words.length>1){
			if (cmd.toLowerCase().contains("skills")){ //View skills list
				showSkills();
			}
			if (cmd.toLowerCase().contains("farm") || cmd.equalsIgnoreCase("crops")){ //View the farm status
				checkFarm();
			}
			if (cmd.toLowerCase().contains("map")){ //View location map
				showMap();
			}
			if (cmd.toLowerCase().contains("location")){ //View location map
				showLocationData();
			}
			if (cmd.toLowerCase().contains("inventory")){ //View an inventory
				if (cmd.toLowerCase().contains("house")){ //View the house inventory if there is one
					if (currentLocation.getHouseInventory()!=null){
						showInventory(currentLocation.getHouseInventory());
					} else {
						print("There's no house built at this location.");
						return;
					}
				} else {
					showInventory(player.getInventory());
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("cheat") && options[3]==true){ //Don't recognize this as a command unless Cheats is on
			if (words.length==1){
				print("Enter the word 'cheat' followed by a valid code... you cheating bastard.");
				return;
			}
			if (words[1].equalsIgnoreCase("mothernature")){ //See the resources available at a location
				showInventory(currentLocation.getResourceCache());
			}
			if (words[1].equalsIgnoreCase("paramedic")){ //Restore to full health
				player.heal(100-player.getHealth());
				print("You have been healed.");
			}
			if (words[1].equalsIgnoreCase("freeenergy")){ //See the resources available at a location
				player.addEnergy(100-player.getEnergy());
				print("Your energy has been refilled.");
			}
			if (words[1].equalsIgnoreCase("oprah") && words.length==4){ //Give free items, you need the numeric ID and the quantity
				if (isNumeric(words[2]) && isNumeric(words[3])){
					player.getInventory().addItem(new Item(Integer.parseInt(words[2]), Integer.parseInt(words[3])));
					print("You have been given "+words[3]+" of the item "+Item.itemNames.get(Integer.parseInt(words[2])));
				}
			}
			if (words[1].equalsIgnoreCase("handyman") && words.length==4){ //Give free items, you need the numeric ID and the quantity
				if (isNumeric(words[2]) && isNumeric(words[3])){
					player.getInventory().addItem(new ToolItem(Integer.parseInt(words[2]), Integer.parseInt(words[3])));
					print("You have been given "+words[3]+" of the tool "+Item.itemNames.get(Integer.parseInt(words[2])));
				}
			}
			if (words[1].equalsIgnoreCase("fletcher") && words.length==4){ //Give free items, you need the numeric ID and the quantity
				if (isNumeric(words[2]) && isNumeric(words[3])){
					player.getInventory().addItem(new WeaponItem(Integer.parseInt(words[2]), Integer.parseInt(words[3])));
					print("You have been given "+words[3]+" of the weapon "+Item.itemNames.get(Integer.parseInt(words[2])));
				}
			}
			if (words[1].equalsIgnoreCase("ramsey") && words.length==4){ //Give free items, you need the numeric ID and the quantity
				if (isNumeric(words[2]) && isNumeric(words[3])){
					player.getInventory().addItem(new FoodItem(Integer.parseInt(words[2]), Integer.parseInt(words[3])));
					print("You have been given "+words[3]+" of the edible item "+Item.itemNames.get(Integer.parseInt(words[2])));
				}
			}
			if (words[1].equalsIgnoreCase("columbus") && words.length==3){
				if (isNumeric(words[2])){
					map.add(new Location(Integer.parseInt(words[2]), randInt(0, 10000), randInt(0, 10000)));
					print("A new location has been added to your map.");
				}
			}
			if (words[1].equalsIgnoreCase("madskillz") && words.length==4){
				if (isNumeric(words[3])){
					player.setSkill(words[2], Integer.parseInt(words[3]));
					print("Skill value updated.");
				}
			}
			if (words[1].equalsIgnoreCase("docbrown") && words.length==3){
				if (isNumeric(words[2])){
					time += Integer.parseInt(words[2]);
					print("Game clock advanced.");
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("chop") || words[0].equalsIgnoreCase("ch")){
			int hrs = 1;
			ToolItem tool = null;
			for (String s : words){
				if (isNumeric(s)){ //Look for numbers and use that to set the hour count
					hrs = Integer.parseInt(s);
				}
			}
			for (int i=0; i<words.length-1; i++){ //Two-word names of tools to use
				if (options[2]){
					print(words[i]+" "+words[i+1]);
				}
				if (player.getInventory().getItem(words[i]+" "+words[i+1]) instanceof ToolItem){ //The player has a tool item with a two-word name that matches
					tool = (ToolItem) player.getInventory().getItem(words[i]+" "+words[i+1]);
				}
			}
			if (cmd.toLowerCase().contains("fish")){
				print("You take a look at the tree, and decide that chopping it with a fish");
				print("is best left to Monty Python.");
				return;
			}
			chopTrees(hrs, tool);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("quit") || words[0].equalsIgnoreCase("q")){
			if (words.length==1){
			if (confirm("Do you want to save the game before quitting?")){
				saveGame();
			}
			pause();
			System.exit(0);
			} else {
				if (cmd.toLowerCase().contains("title")){
					if (confirm("Do you want to save the game before quitting?")){
						saveGame();
					}
					run=false;
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("save") || words[0].equalsIgnoreCase("s")){
			if (words.length==1){
				saveGame();
			} else {
				if (cmd.toLowerCase().contains("title")){
					saveGame();
					print("Returning to the title screen.");
					run=false;
				}
				else if (cmd.toLowerCase().contains("and quit")){
					saveGame();
					print("Minceraft will now exit. See you next time!");
					pause();
					System.exit(0);
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("mine") || words[0].equalsIgnoreCase("mi")){
			int hrs = 1;
			ToolItem tool = null;
			for (String s : words){
				if (isNumeric(s)){ //Look for numbers and use that to set the hour count
					hrs = Integer.parseInt(s);
				}
			}
			for (int i=0; i<words.length-1; i++){ //Two-word names of tools to use
				if (player.getInventory().getItem(words[i]+" "+words[i+1]) instanceof ToolItem){ //The player has a tool item with a two-word name that matches
					tool = (ToolItem) player.getInventory().getItem(words[i]+" "+words[i+1]);
				}
			}
			mine(hrs, tool);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("travel") || words[0].equalsIgnoreCase("t") || words[0].equalsIgnoreCase("go")){
			if (cmd.toLowerCase().contains("home")){
				Location biggestHouseLocation = map.get(0);
				for (Location l : map){
					if (l.getHouseLevel()>biggestHouseLocation.getHouseLevel()){
						biggestHouseLocation = l;
					}
				}
				moveTo(biggestHouseLocation);
			} else {
			boolean found = false;
			for (Location l : map){
				if (cmd.toLowerCase().contains(l.getName().toLowerCase())){
					moveTo(l);
					found = true;
				}
			}
			if (!found){
			print("Command Error: Location not found.");
			print("Make sure you're typing the name of the location.");
			return;
			}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("explore") || words[0].equalsIgnoreCase("ex")){
			Integer hrs = 1;
			for (String s : words){
				if (isNumeric(s)){ //Look for numbers and use that to set the hour count
					hrs = Integer.parseInt(s);
				}
			}
			explore(hrs);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("build")){
			if (words.length==1){
				print("Things you can build:");
				print("Fire, Smelter, Workbench, Farm, Potion Brewery");
			}
			if (cmd.toLowerCase().contains("workbench")){
				build("Workbench");
				return;
			}
			if (cmd.toLowerCase().contains("fire")){
				build("Fire");
				return;
			}
			if (cmd.toLowerCase().contains("smelter")){
				build("Smelter");
				return;
			}
			if (cmd.toLowerCase().contains("farm")){
				if (cmd.toLowerCase().contains("small")){
					buildFarm(1);
					return;
				}
				if (cmd.toLowerCase().contains("medium")){
					buildFarm(2);
					return;
				}
				if (cmd.toLowerCase().contains("large")){
					buildFarm(3);
					return;
				}
			}
			if (cmd.toLowerCase().contains("brewery")){
				build("Potion Brewery");
				return;
			}
			if (cmd.toLowerCase().contains("brewery")){
				build("Potion Brewery");
				return;
			}
			if (cmd.toLowerCase().contains("sod") || cmd.toLowerCase().contains("dirt") || cmd.toLowerCase().contains("mud") || cmd.toLowerCase().contains("hut")){
				buildHouse(1);
				return;
			}
			if (cmd.toLowerCase().contains("shack")){
				buildHouse(2);
				return;
			}
			if (cmd.toLowerCase().contains("cottage")){
				buildHouse(3);
				return;
			}
			if (cmd.toLowerCase().contains("cabin") || cmd.toLowerCase().contains("log")){
				buildHouse(4);
				return;
			}
			if (cmd.toLowerCase().contains("stone house")){
				buildHouse(5);
				return;
			}
			if (cmd.toLowerCase().contains("mansion")){
				buildHouse(6);
				return;
			}
			if (cmd.toLowerCase().contains("castle")){
				buildHouse(7);
				return;
			}
			if (cmd.toLowerCase().contains("palace") || cmd.toLowerCase().contains("fortress")){
				buildHouse(8);
				return;
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("cook")){
			Integer h = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					h = Integer.parseInt(words[i]);
				}
			}
			if (cmd.toLowerCase().contains("beef") || cmd.toLowerCase().contains("steak")){
				cook(Recipe.COOK_BEEF, h);
				return;
			}
			if (cmd.toLowerCase().contains("pork")){
				cook(Recipe.COOK_PORK, h);
				return;
			}
			if (cmd.toLowerCase().contains("fish")){
				cook(Recipe.COOK_FISH, h);
				return;
			}
			if (cmd.toLowerCase().contains("bread")){
				cook(Recipe.COOK_BREAD, h);
				return;
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("smelt")){
			Integer h = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					h = Integer.parseInt(words[i]);
				}
			}
			if (cmd.toLowerCase().contains("iron ingot") || cmd.toLowerCase().contains("steel")){
				smelt(Recipe.SMELT_STEEL, h);
				return;
			}
			if (cmd.toLowerCase().contains("iron")){
				smelt(Recipe.SMELT_IRON, h);
				return;
			}
			if (cmd.toLowerCase().contains("gold")){
				smelt(Recipe.SMELT_GOLD, h);
				return;
			}
			if (cmd.toLowerCase().contains("glass") || cmd.toLowerCase().contains("sand")){
				smelt(Recipe.SMELT_GLASS, h);
				return;
			}
			if (cmd.toLowerCase().contains("clay") || cmd.toLowerCase().contains("dirt")){
				smelt(Recipe.SMELT_CLAY, h);
				return;
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("craft") || words[0].equalsIgnoreCase("c")){
			for (Recipe r : craftRegistry){
				if (cmd.toLowerCase().contains(r.getProduct().getName().toLowerCase())){ //Match the product input to the recipe object
					craft(r);
					return;
				}
			}
			print("Command Error: No crafting recipe found.");
			print("Type 'craft' and the name of the item you want to craft. Contact us if there's a bug!");
			return;
		}
		//----------------------------------------------------------------------------------------------------------
				else if (words[0].equalsIgnoreCase("eat") || words[0].equalsIgnoreCase("e")){
					Integer q = 1;
					for (int i=0; i<words.length; i++){
						if (isNumeric(words[i])){
							q = Integer.parseInt(words[i]);
						}
					}
					for (Item i : player.getInventory()){
						if(cmd.toLowerCase().contains(i.getName().toLowerCase())){
							eat(i.id(), q);
							return;
						}
					}
					print("Command Error: Unknown food item.");
					print("You can't eat items you don't have. Remind me to make this more elegant later."); // TODO Elegance.
					return;
				}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("gather") || words[0].equalsIgnoreCase("ga")){
			Integer h = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					h = Integer.parseInt(words[i]);
				}
			}
			if (cmd.toLowerCase().contains("berry") || cmd.toLowerCase().contains("berries")){
				gather(FoodItem.STRAWBERRY, h);
			}
			else if (cmd.toLowerCase().contains("cocoa") || cmd.toLowerCase().contains("bean")){
				gather(FoodItem.COCOA, h);
			}
			else if (cmd.toLowerCase().contains("cactus") || cmd.toLowerCase().contains("fruit")){
				gather(FoodItem.CACTUS, h);
			}
			else if (cmd.toLowerCase().contains("seeds") || cmd.toLowerCase().contains("wheat")){
				gather(FoodItem.WHEAT, h);
			}
			else if (cmd.toLowerCase().contains("sand")){
				gather(Item.SAND, h);
			}
			else if (cmd.toLowerCase().contains("dirt")){
				gather(Item.DIRT, h);
			}
			else if (cmd.toLowerCase().contains("flint")){
				gather(Item.FLINT, h);
			} else {
				gather(null, h);
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("plant") || words[0].equalsIgnoreCase("pl")){
			Integer q = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					q = Integer.parseInt(words[i]);
				}
			}
			if (cmd.toLowerCase().contains("seed")){
				plant(Item.SEED, q);
				return;
			}
			if (cmd.toLowerCase().contains("sapling")){
				plant(Item.SAPLING, q);
				return;
			}
			if (cmd.toLowerCase().contains("berr")){
				plant(FoodItem.STRAWBERRY, q);
				return;
			}
			if (cmd.toLowerCase().contains("cocoa")){
				plant(FoodItem.COCOA, q);
				return;
			}
			if (cmd.toLowerCase().contains("cact")){
				plant(FoodItem.CACTUS, q);
				return;
			}
			print("Command Error: Unknown item.");
			print("Only seeds, saplings, wild strawberries, cocoa beans, and cactus fruit can be planted."); // TODO Sarcastic remark about planting other items.
			return;
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("harvest") || words[0].equalsIgnoreCase("ha")){
			if (cmd.toLowerCase().contains("wheat") || cmd.toLowerCase().contains("seed")){
				harvest(Item.SEED, true);
				return;
			}
			if (cmd.toLowerCase().contains("berr")){
				harvest(FoodItem.STRAWBERRY, true);
				return;
			}
			if (cmd.toLowerCase().contains("cocoa")){
				harvest(FoodItem.COCOA, true);
				return;
			}
			if (cmd.toLowerCase().contains("cact")){
				harvest(FoodItem.CACTUS, true);
				return;
			}
			if (cmd.toLowerCase().contains("tree") || cmd.toLowerCase().contains("sapling")){
				harvest(Item.SAPLING, true);
				return;
			}
			harvest(null, false);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("fish") || words[0].equalsIgnoreCase("f")){
			Integer h = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					h = Integer.parseInt(words[i]);
				}
			}
			fish(h);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("store") || words[0].equalsIgnoreCase("st")){
			Integer q = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					q = Integer.parseInt(words[i]);
				}
			}
			for (Item i : player.getInventory()){
				if (cmd.toLowerCase().contains(i.getName().toLowerCase())){ //Match the product input to the recipe object
					store(i.id(), q);
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("retrieve") || words[0].equalsIgnoreCase("take") || words[0].equalsIgnoreCase("get")){
			Integer q = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					q = Integer.parseInt(words[i]);
				}
			}
			for (Item i : player.getInventory()){
				if (cmd.toLowerCase().contains(i.getName().toLowerCase())){ //Match the product input to the recipe object
					retrieve(i.id(), q);
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("name")){
			if (cmd.length()<14){
				print("Command error: Couldn't recognize location name.");
				return;
			}
			if (options[2]){
				print("[DEBUG] "+cmd.substring(0, 12));
			}
			if (cmd.substring(0, 14).equalsIgnoreCase("name this spot")){
				String n = cmd.substring(15);
				currentLocation.setName(n);
				print("Location name changed.");
			}
			if (cmd.substring(0, 18).equalsIgnoreCase("name this location")){
				String n = cmd.substring(19);
				currentLocation.setName(n);
				print("Location name changed.");
			}
			if (cmd.substring(0, 13).equalsIgnoreCase("name location")){
				String n = cmd.substring(14);
				currentLocation.setName(n);
				print("Location name changed.");
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("sleep") || words[0].equalsIgnoreCase("sl")){
			Integer q = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					q = Integer.parseInt(words[i]);
				}
			}
			sleep(q);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("help") || words[0].equalsIgnoreCase("?")){
			showHelp();
		}
		//----------------------------------------------------------------------------------------------------------
		//Shorthand commands
		else if (words[0].equalsIgnoreCase("i") || words[0].equalsIgnoreCase("inv")){
			showInventory(player.getInventory());
		}
		else if (words[0].equalsIgnoreCase("s")){
			saveGame();
		}
		else if (words[0].equalsIgnoreCase("l")){
			showLocationData();
		}
		else if (words[0].equalsIgnoreCase("h") || words[0].equalsIgnoreCase("hinv")){
			if (currentLocation.getHouseLevel()>0){
				showInventory(currentLocation.getHouseInventory());
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("recipe")){
			String s = "";
			if (words.length==1){
				showRecipeBook("");
				return;
			}
			if (cmd.substring(7, 10).equalsIgnoreCase("for")){
				s = cmd.substring(10);
			} else {
				s = cmd.substring(7);
			}
			showRecipeBook(s);
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("discard") || words[0].equalsIgnoreCase("trash")){
			Integer q = 1;
			for (int i=0; i<words.length; i++){
				if (isNumeric(words[i])){
					q = Integer.parseInt(words[i]);
				}
			}
			A: for (Item i : player.getInventory()){
				if (cmd.toLowerCase().contains(i.getName().toLowerCase())){ //Match the product input to the recipe object
					if (confirm("Are you sure you want to throw away items?")){
						player.getInventory().removeItem(i.id(), q);
						print("You threw away "+q+" "+i.getName()+".");
						break A;
					}
				}
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else if (words[0].equalsIgnoreCase("drink")){
			// TODO placeholder for potions
			if (cmd.toLowerCase().contains("piss")){
				print("You can't get away with what Bear Grylls can.");
				print("You drink your own piss and vomit profusely. Lose 10 health.");
				player.damage(10);
			}
		}
		//----------------------------------------------------------------------------------------------------------
		else {
			print("Command Error: Unknown command.");
			print("Type 'help' for a list of commands, or 'view the tutorial' for detailed help.");
			print("This is a PRE-ALPHA, so this command might be added in the future!");
			return;
		}
	}
	
	private static void sleep(Integer h){
		if (rawTimeToDMH(time)[1] >= 14){
			if (currentLocation.getHouseLevel()==0){
				print("You can't rest until you've built a shelter.");
				print("You can build a basic house from dirt or wood.");
				return;
			}
			if (h==null){
				h=8;
			}
			if (h>9){
				print("You can't sleep for longer than 9 hours.");
				return;
			}
			time += 60*h;
			player.addEnergy(3*h); //These numbers have to be low to prevent sleep abuse for free healing and energy
			player.heal(1*h);
			int hour = rawTimeToDMH(time)[1];
			int minute = rawTimeToDMH(time)[2];
			String minString = "";
			if (minute < 10){
				minString = "0"+minute;
			} else {
				minString = ""+minute;
			}
			
			int hourActual = 0;
			String ap = "";
			
			if (hour < 6){
				hourActual = hour + 6;
				ap = "AM";
			}
			else if (hour > 6 && hour < 18){
				hourActual = hour - 6;
				ap = "PM";
			}
			else if (hour > 18){
				hourActual = hour - 18;
				ap = "AM";
			}
			else if (hour == 6){
				hourActual = 12;
				ap = "PM";
			}
			else if (hour == 18){
				hourActual = 12;
				ap = "AM";
			}
			print("You sleep for "+h+" hours.");
			print("You awaken at "+hourActual+":"+minString+" "+ap+" feeling rested and refreshed.");
		} else {
			print("You can only sleep at night (8 PM or later).");
			return;
		}
	}
	
	private static void store(Integer id, Integer q){
		if (currentLocation.getHouseLevel()==0){
			print("There's no house at this location.");
			print("You should build one so you can store items!");
			return;
		}
		if (player.getInventory().hasItem(id, q)==false){
			print("You don't have that many "+Item.itemNames.get(id)+" to store.");
			print("Check your inventory to see how many you have.");
			return;
		}
		Item s = player.getInventory().getItem(id);
		currentLocation.getHouseInventory().addItem(s);
		player.getInventory().removeItem(id, q);
		
		print("You stored "+q+" "+s.getName()+" in your house at "+currentLocation.getName()+".");
	}
	
	private static void retrieve(Integer id, Integer q){
		if (currentLocation.getHouseLevel()==0){
			print("There's no house at this location.");
			print("You should build one so you can store items!");
			return;
		}
		if (currentLocation.getResourceCache().hasItem(id, q)==false){
			print("You don't have that many "+Item.itemNames.get(id)+" in storage.");
			print("Check your house's inventory to see how many you have.");
			return;
		}
		Item s = currentLocation.getResourceCache().getItem(id);
		player.getInventory().addItem(s);
		currentLocation.getResourceCache().removeItem(id, q);
		
		print("You stored "+q+" "+s.getName()+" in your house at "+currentLocation.getName()+".");
	}
	
	private static void harvest(Integer id, Boolean spec){
		Integer energyPerCrop = 0;
		Integer timePerCrop = 0;
		ToolItem shovel = player.getInventory().strongestShovel();
		if (currentLocation.getFarmLevel()==0){
			print("There's no farm here.");
			print("Build one by gathering some dirt, and wood for a fence.");
			print("After all, those monsters might be herbivores...");
			return;
		}
		if (shovel==null){
			print("You need a shovel to farm.");
			print("Check the recipe book to see how to craft one.");
			return;
		}
		if (shovel.id()==ToolItem.DIAMOND_SHOVEL){
			energyPerCrop = 2;
			timePerCrop = 5;
		}
		else if (shovel.id()==ToolItem.STEEL_SHOVEL){
			energyPerCrop = 2;
			timePerCrop = 6;
		}
		else if (shovel.id()==ToolItem.IRON_SHOVEL){
			energyPerCrop = 3;
			timePerCrop = 8;
		}
		else if (shovel.id()==ToolItem.STONE_SHOVEL){
			energyPerCrop = 4;
			timePerCrop = 12;
		}
		else if (shovel.id()==ToolItem.WOODEN_SHOVEL){
			energyPerCrop = 5;
			timePerCrop = 16;
		}
		ArrayList<Item> crops = new ArrayList<Item>();
		if(spec){
		for(int i=0; i<currentLocation.getFarm().getCropCount(); i++){
			if (currentLocation.getFarm().getCrop(i)==id && currentLocation.getFarm().isReady(i)){
				Item crop = currentLocation.getFarm().harvest(i);
				crops.add(crop);
			}
		}
		} else {
			for(int i=0; i<currentLocation.getFarm().getCropCount(); i++){
				if (currentLocation.getFarm().isReady(i)){
					Item crop = currentLocation.getFarm().harvest(i);
					crops.add(crop);
				}
			}
		}
		if (crops.size()==0){
			print("Nothing is ready to harvest yet.");
			return;
		}
		print("Your harvest yields "+itemListAsString(crops)+".");
		for (Item i : crops){
			player.getInventory().addItem(i);
			player.loseEnergy(energyPerCrop);
			time += timePerCrop;
		}
	}
	
	private static void plant(Integer id, Integer q){
		Integer energyPerCrop = 0;
		Integer timePerCrop = 0;
		ToolItem shovel = player.getInventory().strongestShovel();
		if (currentLocation.getFarmLevel()==0){
			print("There's no farm here.");
			print("Build one by gathering some dirt, and wood for a fence.");
			print("After all, those monsters might be herbivores...");
			return;
		}
		if (shovel==null){
			print("You need a shovel to farm.");
			print("Check the recipe book to see how to craft one.");
			return;
		}
		if (shovel.id()==ToolItem.DIAMOND_SHOVEL){
			energyPerCrop = 2;
			timePerCrop = 5;
		}
		else if (shovel.id()==ToolItem.STEEL_SHOVEL){
			energyPerCrop = 2;
			timePerCrop = 6;
		}
		else if (shovel.id()==ToolItem.IRON_SHOVEL){
			energyPerCrop = 3;
			timePerCrop = 8;
		}
		else if (shovel.id()==ToolItem.STONE_SHOVEL){
			energyPerCrop = 4;
			timePerCrop = 12;
		}
		else if (shovel.id()==ToolItem.WOODEN_SHOVEL){
			energyPerCrop = 5;
			timePerCrop = 16;
		}
		if (player.getInventory().hasItem(id, q)==false){
			print("You don't have that many "+Item.itemNames.get(id)+" to plant.");
			print("Check your inventory to see how many you have.");
			return;
		}
		for (int i=0; i<q; i++){
			currentLocation.getFarm().plant(id);
			player.getInventory().removeItem(id, 1);
			player.loseEnergy(energyPerCrop);
			time += timePerCrop;
		}
		print("You planted "+q+" "+Item.itemNames.get(id)+" on your farm at "+currentLocation.getName()+".");
	}
	
	private static void checkFarm(){
		if (currentLocation.getFarmLevel()==0){
			print("There's no farm here.");
			print("Build one by gathering some dirt, and wood for a fence.");
			print("After all, those monsters might be herbivores...");
			return;
		}
		Integer[] crops = {0, 0, 0, 0, 0};
		Integer[] ready = {0, 0, 0, 0, 0};
		for (int i=0; i<currentLocation.getFarm().getCropCount(); i++){
			if(currentLocation.getFarm().getCrop(i)==Item.SEED){
				crops[0]++;
				if(currentLocation.getFarm().isReady(i)){
					ready[0]++;
				}
			}
			if(currentLocation.getFarm().getCrop(i)==Item.SAPLING){
				crops[1]++;
				if(currentLocation.getFarm().isReady(i)){
					ready[1]++;
				}
			}
			if(currentLocation.getFarm().getCrop(i)==FoodItem.STRAWBERRY){
				crops[2]++;
				if(currentLocation.getFarm().isReady(i)){
					ready[2]++;
				}
			}
			if(currentLocation.getFarm().getCrop(i)==FoodItem.COCOA){
				crops[3]++;
				if(currentLocation.getFarm().isReady(i)){
					ready[3]++;
				}
			}
			if(currentLocation.getFarm().getCrop(i)==FoodItem.CACTUS){
				crops[4]++;
				if(currentLocation.getFarm().isReady(i)){
					ready[4]++;
				}
			}
		}
		int t=0;
		for (int i=0; i<5; i++){
			t += crops[i];
		}
		if (t==0){
			print("Your farm is barren.");
			print("Try 'plant' to get started farming.");
			return;
		}
		String s = "";
		if (crops[0]>0){
			s=s.concat("There's "+crops[0]+" wheat growing, ");
			if (ready[0]>0){
				s=s.concat("and "+ready[0]+" of it is ready to harvest.");
			} else {
				s=s.concat("but none of it is ready yet.");
			}
		}
		print(s);
		s="";
		if (crops[1]>0){
			s=s.concat("There's "+crops[1]+" tree saplings growing, ");
			if (ready[1]>0){
				s=s.concat("and "+ready[1]+" of them are ready to harvest.");
			} else {
				s=s.concat("but none of them are ready yet.");
			}
		}
		print(s);
		s="";
		if (crops[2]>0){
			s=s.concat("There's "+crops[2]+" wild strawberry plants growing, ");
			if (ready[2]>0){
				s=s.concat("and "+ready[2]+" of them are ready to harvest.");
			} else {
				s=s.concat("but none of them are ready yet.");
			}
		}
		print(s);
		s="";
		if (crops[3]>0){
			s=s.concat("There's "+crops[3]+" cocoa plants growing, ");
			if (ready[3]>0){
				s=s.concat("and "+ready[3]+" of them are ready to harvest.");
			} else {
				s=s.concat("but none of them are ready yet.");
			}
		}
		print(s);
		s="";
		if (crops[4]>0){
			s=s.concat("There's "+crops[4]+" cacti growing, ");
			if (ready[4]>0){
				s=s.concat("and "+ready[4]+" of them are ready to harvest.");
			} else {
				s=s.concat("but none of them are ready yet.");
			}
		}
		print(s);
		s="";
	}
	
	private static void buildFarm(Integer lvl){
		if (currentLocation.getFarmLevel()>=lvl){
			print("There's already a bigger farm at this location. You can't build two farms in the same location.");
			print("Go to another location that doesn't already have a farm, and you can build it there!");
			return;
		}
		ToolItem hammer = player.getInventory().strongestHammer();
		Double speed = 1.0;
		Integer energyPerHour = 5;
		try {
			if (hammer.id()==ToolItem.DIAMOND_HAMMER){
				speed = 2.0;
				energyPerHour = 1;
			}
			if (hammer.id()==ToolItem.STEEL_HAMMER){
				speed = 1.75;
				energyPerHour = 2;
			}
			if (hammer.id()==ToolItem.IRON_HAMMER){
				speed = 1.5;
				energyPerHour = 3;
			}
			if (hammer.id()==ToolItem.STONE_HAMMER){
				speed = 1.25;
				energyPerHour = 4;
			}
		} catch (NullPointerException e){
			speed = 1.0;
		}
		if(lvl==1){
			for (Item i : Recipe.FARM_SMALL.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.FARM_SMALL.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setFarmLevel(lvl);
			double x = (double)Recipe.FARM_SMALL.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);			
			if (hammer==null){
				print("You successfully built a small farm at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a small farm at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}	
		}
		if(lvl==2){
			for (Item i : Recipe.FARM_MEDIUM.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.FARM_MEDIUM.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setFarmLevel(lvl);
			double x = (double)Recipe.FARM_MEDIUM.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);			
			if (hammer==null){
				print("You successfully built a medium-size farm at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a medium-size farm at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}		}
		if(lvl==3){
			for (Item i : Recipe.FARM_LARGE.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.FARM_LARGE.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setFarmLevel(lvl);
			double x = (double)Recipe.FARM_LARGE.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);			
			if (hammer==null){
				print("You successfully built a large farm at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a large farm at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}		}
		currentLocation.addBuilding("Farm");
	}
	
	private static void eat(Integer id, Integer q){
		if (player.getInventory().hasItem(id)==false){ //Player doesn't have any of the item they entered
			print("You don't have any of those!");
			return;
		}
		if (!(player.getInventory().getItem(id) instanceof FoodItem)){ //Player tries to eat something that isn't food
			print("No matter how desperate you are, eating a "+Item.itemNames.get(id)+" would do more harm than good.");
			return;
		}
		if (player.getInventory().hasItem(id, q)==false){ //Player tries to eat more of something than they have
			print("You only have "+player.getInventory().getItem(id).getQuantity()+" of "+Item.itemNames.get(id)+".");
			return;
		}
		FoodItem food = new FoodItem(id, q);
		player.getInventory().removeItem(id, q);
		player.addEnergy(food.resEnergy*q);
		player.heal(food.resHealth*q);
		time += 5*q;
		if(q==1){
			print("You eat a "+food.getName()+".");
		}
		if(q>1){
			print("You eat "+q+" "+food.getName()+"s.");
		}
		print("You feel refreshed and energized. "+(food.resEnergy*q)+"% Energy restored.");
		print("Sustenance helps your wounds heal. "+(food.resHealth*q)+"% Health restored.");
	}
	
	private static void gather(Integer id, Integer h){
		Integer[] gatherables = {Item.SAND, Item.DIRT, Item.FLINT, FoodItem.CACTUS, FoodItem.STRAWBERRY, FoodItem.COCOA, Item.WHEAT};
		ToolItem shovel = null;
		Integer toolGatherRate = 1;
		Integer itemsToCollect = 0;
		if (id!=null){ //Player specified an item to gather, focus solely on that
			if(id==Item.SAND || id==Item.DIRT){ //Sand or dirt are special because they use a shovel
				if (player.getInventory().hasItem(ToolItem.DIAMOND_SHOVEL)){
					shovel = (ToolItem) player.getInventory().getItem(ToolItem.DIAMOND_SHOVEL);
					toolGatherRate = 6;
				}
				else if (player.getInventory().hasItem(ToolItem.STEEL_SHOVEL)){
					shovel = (ToolItem) player.getInventory().getItem(ToolItem.STEEL_SHOVEL);
					toolGatherRate = 5;
				}
				else if (player.getInventory().hasItem(ToolItem.IRON_SHOVEL)){
					shovel = (ToolItem) player.getInventory().getItem(ToolItem.IRON_SHOVEL);
					toolGatherRate = 4;
				}
				else if (player.getInventory().hasItem(ToolItem.STONE_SHOVEL)){
					shovel = (ToolItem) player.getInventory().getItem(ToolItem.STONE_SHOVEL);
					toolGatherRate = 3;
				}
				else if (player.getInventory().hasItem(ToolItem.WOODEN_SHOVEL)){
					shovel = (ToolItem) player.getInventory().getItem(ToolItem.WOODEN_SHOVEL);
					toolGatherRate = 2;
				}
				if (currentLocation.getResourceCache().hasItem(id)==false){ //Stop if there isn't any of the item
					print("There isn't any "+Item.itemNames.get(id)+" to collect here.");
					print("You'll have to look elsewhere...");
					return;
				}
				do {
					itemsToCollect = (toolGatherRate*h) + randInt(-3, 3);
					if (itemsToCollect > currentLocation.getResourceCache().getItem(id).getQuantity()){
						itemsToCollect = currentLocation.getResourceCache().getItem(id).getQuantity();
					}
				} while (itemsToCollect < 0);
				if (shovel!=null){
					print("You use your "+shovel.getName()+" to gather "+Item.itemNames.get(id)+" for "+h+" hours.");
					print("You come back with "+itemsToCollect+" "+Item.itemNames.get(id)+".");
				} else {
					print("You gather "+Item.itemNames.get(id)+" for "+h+" hours.");
					print("You come back with "+itemsToCollect+" "+Item.itemNames.get(id)+".");
				}
				currentLocation.getResourceCache().removeItem(id, itemsToCollect);
				player.getInventory().addItem(new Item(id, itemsToCollect));
				player.loseEnergy(2*h);
				time += 60*h;
				shovel.damage(2*h);
				return;
			} else { //Gatherables that do not use the shovel
				if (currentLocation.getResourceCache().hasItem(id)==false){ //Stop if there isn't any of the item
					print("There isn't any "+Item.itemNames.get(id)+" to collect here.");
					print("You'll have to look elsewhere...");
					return;
				}
				do {
					itemsToCollect = (3*h) + randInt(-3, 3);
					if (itemsToCollect > currentLocation.getResourceCache().getItem(id).getQuantity()){
						itemsToCollect = currentLocation.getResourceCache().getItem(id).getQuantity();
					}
				} while (itemsToCollect < 0);
				print("You gather "+Item.itemNames.get(id)+" for "+h+" hours.");
				print("You come back with "+itemsToCollect+" "+Item.itemNames.get(id)+".");
				currentLocation.getResourceCache().removeItem(id, itemsToCollect);
				if (id==FoodItem.APPLE || id==FoodItem.CACTUS || id==FoodItem.COCOA || id==FoodItem.STRAWBERRY){
					player.getInventory().addItem(new FoodItem(id, itemsToCollect));
				} else {
					player.getInventory().addItem(new Item(id, itemsToCollect));
				}
				player.loseEnergy(2*h);
				time += 60*h;
				return;
			}
		} else { //Gather anything you can (results in less of each thing)
			ArrayList<Item> loot = new ArrayList<Item>();
			itemsToCollect = (3*h) + randInt(-2, 3);
			Integer rgather = 0;
			for (Integer i : gatherables){ //Determine how many of each item in the list to get
				if (itemsToCollect<=0){
					break;
				}
				if (currentLocation.getResourceCache().hasItem(i)){
					rgather = randInt(1, itemsToCollect);
					if (rgather > currentLocation.getResourceCache().getItem(i).getQuantity()){
						rgather = currentLocation.getResourceCache().getItem(i).getQuantity();
					}
					loot.add(new Item(i, rgather));
					currentLocation.getResourceCache().removeItem(i, rgather);
					if (i==FoodItem.APPLE || i==FoodItem.CACTUS || i==FoodItem.COCOA || i==FoodItem.STRAWBERRY){
						player.getInventory().addItem(new FoodItem(i, rgather));
					} else {
						player.getInventory().addItem(new Item(i, rgather));
					}
					itemsToCollect -= rgather;
				}
			}
			print("You gather supplies from the environment for "+h+" hours.");
			print("You come back with "+itemListAsString(loot)+".");
			player.loseEnergy(2*h);
			time += 60*h;
			return;
		}
	}
	
	private static void smelt(Recipe r, Integer q){
		if (currentLocation.hasBuilding("Smelter")==false){
			print("You don't have a smelter yet.");
			print("Try building one out of stone and some clay.");
			return;
		}
		Integer fuelID = Item.COAL;
		if (r==Recipe.SMELT_STEEL){ //Special recipe uses basalt as fuel instead of coal
			fuelID = Item.BASALT;
		}
		if (player.getInventory().hasItem(fuelID, q)==false){
			print("You'd need fuel for the smelter!");
			if (fuelID==Item.COAL){
				print("Smelting requires coal for fuel. 1 piece can smelt 4 items.");
			} else {
				print("Smelting iron ingots can produce steel, but you have to use Basalt as fuel!");
				print("This igneous rock can be found around volcanoes, but it's hard to mine...");
			}
			return;
		}
		for (Item i : r.getIngredients()){
			if(player.getInventory().hasItem(i.id(), i.getQuantity()*q)==false){
				print("You don't have that much "+i.getName()+" to smelt.");
				return;
			}
		}
		for (int j=0; j<q; j++){ //Iterate to run the recipe multiple times
			for (Item i : r.getIngredients()){
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			if (j%4 == 0){ //Consume fuel every fourth run-through
				player.getInventory().removeItem(fuelID, 1);
			}
			player.getInventory().addItem(new Item(r.getProduct().id(), r.getProduct().getQuantity()));
			time += r.getCraftTime();
			if (player.getInventory().hasItem(fuelID)==false){
				print("You ran out of fuel before you could finish smelting.");
				break;
			}
		}
		print("You successfully smelt "+q+" "+r.getProduct().getName()+".");
	}
	
	private static void cook(Recipe r, Integer q){
		if (currentLocation.hasBuilding("Fire")==false){
			print("To cook food, you'll need a fire.");
			print("Gather some wood by chopping down trees, then build one.");
			return;
		}
		if (player.getInventory().hasItem(Item.WOOD)==false){
			print("You'd need fuel for the fire!");
			print("Cooking requires firewood. 1 piece can cook 4 items.");
			return;
		}
		for (Item i : r.getIngredients()){
			if(player.getInventory().hasItem(i.id(), i.getQuantity()*q)==false){
				print("You don't have that much "+i.getName()+" to cook.");
				return;
			}
		}
		for (int j=0; j<q; j++){ //Iterate to run the recipe multiple times
			for (Item i : r.getIngredients()){
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			if (j%4 == 0){ //Consume wood every fourth run-through
				player.getInventory().removeItem(Item.WOOD, 1);
			}
			player.getInventory().addItem(new FoodItem(r.getProduct().id(), r.getProduct().getQuantity()));
			time += r.getCraftTime();
			if (player.getInventory().hasItem(Item.WOOD)==false){
				print("You ran out of firewood before you could finish cooking.");
				break;
			}
		}
		
		print("You successfully cook "+q+" "+r.getProduct().getName()+".");
	}
	
	private static void craft(Recipe r){
		if (currentLocation.hasBuilding("Workbench")==false){
			print("No matter your survivalist skills, you need a solid, level surface to craft on.");
			print("Try building a workbench with some wood!");
			return;
		}
		for (Item i : r.getIngredients()){
			if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
				print("You don't have the supplies to craft a "+r.getProduct().getName()+".");
				return;
			}
		}
		print("Crafting a "+r.getProduct().getName()+" will require "+r.ingredientsAsString()+".");
		if (confirm("Proceed?")){
			for (Item i : r.getIngredients()){
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			player.getInventory().addItem(r.getProduct());
			print("You successfully craft a "+r.getProduct().getName()+".");
			time += r.getCraftTime();
			player.loseEnergy(5);
		}
	}
	
	private static void buildHouse(Integer lvl){
		ToolItem hammer = player.getInventory().strongestHammer();
		Double speed = 1.0;
		Integer energyPerHour = 5;
		try {
			if (hammer.id()==ToolItem.DIAMOND_HAMMER){
				speed = 2.0;
				energyPerHour = 1;
			}
			if (hammer.id()==ToolItem.STEEL_HAMMER){
				speed = 1.75;
				energyPerHour = 2;
			}
			if (hammer.id()==ToolItem.IRON_HAMMER){
				speed = 1.5;
				energyPerHour = 3;
			}
			if (hammer.id()==ToolItem.STONE_HAMMER){
				speed = 1.25;
				energyPerHour = 4;
			}
		} catch (NullPointerException e){
			speed = 1.0;
		}
		if (currentLocation.getHouseLevel()>=lvl){
			print("There's already a bigger house at this location. You can't build two houses in the same location.");
			print("Go to another location that doesn't already have a house, and you can build it there!");
			return;
		}
		if(lvl==1){
			for (Item i : Recipe.HOUSE_SOD.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_SOD.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_SOD.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a sod house at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a sod house at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}
		}
		if(lvl==2){
			for (Item i : Recipe.HOUSE_WOOD_SMALL.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_WOOD_SMALL.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_WOOD_SMALL.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a wooden shack at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a wooden shack at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}
		}
		if(lvl==3){
			for (Item i : Recipe.HOUSE_STONE_SMALL.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_STONE_SMALL.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_STONE_SMALL.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a stone cottage at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a stone cottage at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}
		}
		if(lvl==4){
			for (Item i : Recipe.HOUSE_WOOD_LARGE.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_WOOD_LARGE.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_WOOD_LARGE.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);			
			if (hammer==null){
				print("You successfully built a log cabin at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a log cabin at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}		
		}
		if(lvl==5){
			for (Item i : Recipe.HOUSE_STONE_LARGE.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_STONE_LARGE.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_STONE_LARGE.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a stone house at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a stone house at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}		
		}
		if(lvl==6){
			for (Item i : Recipe.HOUSE_MANSION.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_MANSION.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_MANSION.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a mansion at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a mansion at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}	
		}
		if(lvl==7){
			for (Item i : Recipe.HOUSE_CASTLE.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_CASTLE.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_CASTLE.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a castle at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a castle at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}		}
		if(lvl==8){
			for (Item i : Recipe.HOUSE_FORTRESS.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			for (Item i : Recipe.HOUSE_FORTRESS.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			currentLocation.setHouseLevel(lvl);
			double x = (double)Recipe.HOUSE_FORTRESS.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer==null){
				print("You successfully built a palace fortress at "+currentLocation.getName()+".");
			} else {
				print("You use your "+hammer.getName()+" to build a palace fortress at "+currentLocation.getName()+".");
				hammer.damage(3*e);
			}	
		}
	}
	
	private static void build(String b){
		ToolItem hammer = player.getInventory().strongestHammer();
		Double speed = 1.0;
		Integer energyPerHour = 5;
		try {
			if (hammer.id()==ToolItem.DIAMOND_HAMMER){
				speed = 2.0;
				energyPerHour = 1;
			}
			if (hammer.id()==ToolItem.STEEL_HAMMER){
				speed = 1.75;
				energyPerHour = 2;
			}
			if (hammer.id()==ToolItem.IRON_HAMMER){
				speed = 1.5;
				energyPerHour = 3;
			}
			if (hammer.id()==ToolItem.STONE_HAMMER){
				speed = 1.25;
				energyPerHour = 4;
			}
		} catch (NullPointerException e){
			speed = 1.0;
		}
		if(b.equals("Workbench")){
			for (Item i : Recipe.WORKBENCH.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			print("Building a workbench will require "+Recipe.WORKBENCH.ingredientsAsString()+".");
			if (confirm("Proceed?")==false){
				return;
			}
			for (Item i : Recipe.WORKBENCH.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			double x = (double)Recipe.WORKBENCH.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer!=null){
				hammer.damage(2*e);
			}
		}
		if(b.equals("Fire")){
			for (Item i : Recipe.BONFIRE.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			print("Building a bonfire will require "+Recipe.BONFIRE.ingredientsAsString()+".");
			if (confirm("Proceed?")==false){
				return;
			}
			for (Item i : Recipe.BONFIRE.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			double x = (double)Recipe.BONFIRE.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer!=null){
				hammer.damage(2*e);
			}
		}
		if(b.equals("Smelter")){
			for (Item i : Recipe.SMELTER.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			print("Building a smelter will require "+Recipe.SMELTER.ingredientsAsString()+".");
			if (confirm("Proceed?")==false){
				return;
			}
			for (Item i : Recipe.SMELTER.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			double x = (double)Recipe.SMELTER.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer!=null){
				hammer.damage(2*e);
			}
		}
		if(b.equals("Potion Brewery")){
			for (Item i : Recipe.BREWERY.getIngredients()){ //Check the player for required supplies
				if(player.getInventory().hasItem(i.id(), i.getQuantity())==false){
					print("You don't have the supplies to build that.");
					return;
				}
			}
			print("Building a workbench will require "+Recipe.BREWERY.ingredientsAsString()+".");
			if (confirm("Proceed?")==false){
				return;
			}
			for (Item i : Recipe.BREWERY.getIngredients()){ //Remove the supplies
				player.getInventory().removeItem(i.id(), i.getQuantity());
			}
			double x = (double)Recipe.BREWERY.getCraftTime();
			double y = x / speed;
			time += (int)y;
			int e = ((int)y) / 60;
			player.loseEnergy(energyPerHour*e);
			if (hammer!=null){
				hammer.damage(2*e);
			}
		}
		if (hammer==null){
			print("You successfully built a "+b+" at "+currentLocation.getName()+".");
		} else {
			print("You use your "+hammer.getName()+" to build a "+b+" at "+currentLocation.getName()+".");
		}
		currentLocation.addBuilding(b);
	}
	
	private static void showSkills(){
		print("[ SKILLS - "+player.getName().toUpperCase()+" ]");
		print("Combat: "+player.getSkill("Combat")+" - "+Player.skillTerms.get("Combat").get(player.getSkill("Combat")));
		print("Strength: "+player.getSkill("Strength")+" - "+Player.skillTerms.get("Strength").get(player.getSkill("Strength")));
		print("Intelligence: "+player.getSkill("Intelligence")+" - "+Player.skillTerms.get("Intelligence").get(player.getSkill("Intelligence")));
		print("Dexterity: "+player.getSkill("Dexterity")+" - "+Player.skillTerms.get("Dexterity").get(player.getSkill("Dexterity")));
		print("Agility: "+player.getSkill("Agility")+" - "+Player.skillTerms.get("Agility").get(player.getSkill("Agility")));
		print("Charisma: "+player.getSkill("Charisma")+" - "+Player.skillTerms.get("Charisma").get(player.getSkill("Charisma")));
		printEmptyLines(16);
	}
	
	private static void explore(Integer h){
		boolean hasAltar = false;
		for (Location l : map){
			if(l.getType()==Location.ALTAR){
				hasAltar = true;
			}
		}
		if (player.getInventory().hasItem(Item.BASALT_COMPASS) && !hasAltar){ //If the player has a basalt compass and doesn't already have the altar on the map, add it
			print("The special compass waves its needle in circles briefly, before dialing in on");
			print("a single direction. You follow it and see, in the distance, an odd stone structure.");
			print("There's no doubt in your mind that this is the Altar the villagers were talking about.");
			print("The base is lined with gold and old, cracked stone bricks. There is a torch on the top.");
			Location altar = new Location(Location.ALTAR, randInt(0, 10000), randInt(0, 10000));
			altar.setName("The Altar of Herobrine");
			map.add(altar);
		} else { //Normal exploration
			print("You venture out and look around the blocky world for "+h+" hours.");
			printEmptyLines(1);
			ArrayList<Location> found = new ArrayList<Location>();
			for (int i=0; i<h; i++){
				if (randInt(1, 100)<=25){ //Find a resource location
					Integer roll = randInt(1, 100);
					if (roll<=20){
						found.add(new Location(Location.CAVE_LARGE, randInt(0, 10000), randInt(0, 10000)));
						print("You find a deep, dark cave, lit by lava flows and littered with various ores and gems.");
					}
					else if (roll<=60){
						found.add(new Location(Location.CAVE_SMALL, randInt(0, 10000), randInt(0, 10000)));
						print("You find a cave, littered with rocks and some coal and other ores.");
					}
					else{
						found.add(new Location(Location.RIVER, randInt(0, 10000), randInt(0, 10000)));
						print("You hear water flowing, and discover a river teeming with fish.");
					}
					printEmptyLines(1);
				}
				if (randInt(1, 100)<=10){ //Find a regular location
					Integer roll = randInt(1, 7);
					if (roll==1){
						found.add(new Location(Location.DESERT, randInt(0, 10000), randInt(0, 10000)));
						print("You find a vast, hot, sandy desert dotted with cacti bearing red fruit.");
					}
					if (roll==2){
						found.add(new Location(Location.FOREST, randInt(0, 10000), randInt(0, 10000)));
						print("You find a serene forest filled with trees, various flowers, and animals.");
					}
					if (roll==3){
						found.add(new Location(Location.PLAINS, randInt(0, 10000), randInt(0, 10000)));
						print("You find wide open plains, covered in tall grass. Herds of cows roam the hills.");
					}
					if (roll==4){
						found.add(new Location(Location.JUNGLE, randInt(0, 10000), randInt(0, 10000)));
						print("You find a lush, green jungle. Thick fog conceals the herds of pigs within.");
					}
					if (roll==5){
						found.add(new Location(Location.MOUNTAINS, randInt(0, 10000), randInt(0, 10000)));
						print("You find a mountain range. Herds of sheep climb the steep hills and cliffs.");
					}
					if (roll==6){
						found.add(new Location(Location.WETLANDS, randInt(0, 10000), randInt(0, 10000)));
						print("You find a muggy swamp with pools of stagnant water and soft clay in the ground.");
					}
					if (roll==7){
						found.add(new Location(Location.TUNDRA, randInt(0, 10000), randInt(0, 10000)));
						print("You find a cold, barren tundra, covered in thick snow and devoid of animal life.");
					}
					printEmptyLines(1);
				}
				if (randInt(1, 100)<=3){ //Find a rare location
					Integer roll = randInt(1, 2);
					if (roll==1){
						found.add(new Location(Location.VOLCANO, randInt(0, 10000), randInt(0, 10000)));
						print("You discover a tall mountain in the distance.");
						print("You move closer and see lava flowing down its steep, black sides.");
						print("It's a volcano, made of a hard, black stone.");
						print("You might be able to mine it, but you'll need a really good pickaxe...");
					}
					if (roll==2){
						found.add(new Location(Location.VILLAGE, randInt(0, 10000), randInt(0, 10000)));
						print("You discover, to your relief, a small village over a nearby hill.");
						print("The people here look strange. Like everything else, they're square and blocky.");
						print("You can see several farms, houses, and various animals in fenced-in yards.");
						print("Should you approach? It's not like you know where you are anyway...");
					}
					printEmptyLines(1);
				}
			}
			if (found.size()==0){
				print("Unfortunately, this "+currentLocation.getBiome()+" seems to go on for miles.");
				print("You find nothing worth mentioning.");
			} else {
				print("You mark down these "+found.size()+" new locations on your map.");
				map.addAll(found);
			}
			int energyLoss = randInt(5, 10)*h;
			player.loseEnergy(energyLoss);
			time += 60*h;
			print("Exploring unfamiliar land is tiring work. You lose "+energyLoss+"% energy.");
		}
	}
	
	private static void moveTo(Location l){
		if (l.equals(currentLocation)){
			print("You're already here! Look at that!");
			return;
		}
		time += l.getTravelTime(currentLocation);
		currentLocation = l;
		int hour = rawTimeToDMH(time)[1];
		int minute = rawTimeToDMH(time)[2];
		String minString = "";
		if (minute < 10){
			minString = "0"+minute;
		} else {
			minString = ""+minute;
		}
		
		int hourActual = 0;
		String ap = "";
		
		if (hour < 6){
			hourActual = hour + 6;
			ap = "AM";
		}
		else if (hour > 6 && hour < 18){
			hourActual = hour - 6;
			ap = "PM";
		}
		else if (hour > 18){
			hourActual = hour - 18;
			ap = "AM";
		}
		else if (hour == 6){
			hourActual = 12;
			ap = "PM";
		}
		else if (hour == 18){
			hourActual = 12;
			ap = "AM";
		}
		print("You arrive at "+l.getName()+" at "+hourActual+":"+minString+" "+ap+".");
	}
	
	private static void showMap(){
		Integer blankLines = 22;
		print("[ MAP : "+map.size()+" locations discovered, "+uniqueLocations()+" / 13 biomes seen ]");
		for (Location l : map){
			if (l.equals(currentLocation)){
				print("[ "+l.getName()+": "+l.getBiome()+", Current Location ]");
			}
			else {
				print("[ "+l.getName()+": "+l.getBiome()+", "+DMHToTimeString(rawTimeToDMH(l.getTravelTime(currentLocation)))+" away ]");
			}
			blankLines--;
		}
		printEmptyLines(blankLines);
	}
	
	private static void mine(Integer h, ToolItem tool){
		Integer[] mineables = {Item.DIRT, Item.STONE, Item.CLAY, Item.FLINT, Item.COAL, Item.IRON_ORE, Item.GOLD_ORE, Item.DIAMOND, Item.BASALT, Item.OBSIDIAN};
		Integer[] minerates = {80, 90, 60, 60, 40, 30, 15, 5, 60, 5};
		Integer baseMineRate = 0;
		if(tool==null){ //If no tool is specified by the parser, use the strongest pickaxe the player has (cannot mine with fists)
			if (player.getInventory().hasItem(ToolItem.DIAMOND_PICK)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.DIAMOND_PICK);
			}
			else if (player.getInventory().hasItem(ToolItem.STEEL_PICK)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.STEEL_PICK);
			}
			else if (player.getInventory().hasItem(ToolItem.IRON_PICK)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.IRON_PICK);
			}
			else if (player.getInventory().hasItem(ToolItem.STONE_PICK)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.STONE_PICK);
			}
			else if (player.getInventory().hasItem(ToolItem.WOODEN_PICK)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.WOODEN_PICK);
			}
			else {
				print("You'll need a pickaxe before you can mine effectively.");
				print("You hear deep groaning noises below you. Maybe a weapon wouldn't hurt either...");
				return;
			}
			}
		if(tool.id()==ToolItem.WOODEN_PICK){
			baseMineRate = 1;
		}
		else if(tool.id()==ToolItem.STONE_PICK){
			baseMineRate = 2;
		}
		if(tool.id()==ToolItem.IRON_PICK){
			baseMineRate = 3;
		}
		if(tool.id()==ToolItem.STEEL_PICK){
			baseMineRate = 4;
		}
		if(tool.id()==ToolItem.DIAMOND_PICK){
			baseMineRate = 4;
		}
			Integer rmine = 0;
			Inventory acq = new Inventory(65536);
			for(int i=0; i<h; i++){ // Iterate for every hour spent mining
				for (int j=0; j<mineables.length; j++){ // Iterate over the list of mineable resources
					if (currentLocation.getResourceCache().hasItem(mineables[j])){ // If the location has any of that resource
						if (randInt(1, 100) <= minerates[j]){ // Roll to see if any will be collected this hour
							rmine = baseMineRate + randInt(-1, 1);
							if (rmine > currentLocation.getResourceCache().getItem(mineables[j]).getQuantity()){
								rmine = currentLocation.getResourceCache().getItem(mineables[j]).getQuantity();
							}
							if (rmine>0){
							currentLocation.getResourceCache().removeItem(mineables[j], rmine);
							player.getInventory().addItem(new Item(mineables[j], rmine));
							acq.addItem(new Item(mineables[j], rmine));
							}
						}
					}
				}
				time += 60;
				player.loseEnergy(randInt(0, 7));
			}
			print("You mine for "+h+" hours using your "+tool.getName()+".");
			if (acq.getTotalItems()==0){
				print("The stone walls are tough! You didn't collect anything.");
				print("Don't give up! Try mining some more or making a better pickaxe.");
			} else {
				print("You collect "+itemListAsString(acq.toItemArray())+".");
			}
			tool.damage(4*h);
	}
	
	private static void chopTrees(Integer h, ToolItem tool){
		if(tool==null){ //If no tool is specified by the parser, use the strongest axe the player has, or their fists as a last resort
			if (player.getInventory().hasItem(ToolItem.DIAMOND_AXE)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.DIAMOND_AXE);
			}
			else if (player.getInventory().hasItem(ToolItem.STEEL_AXE)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.STEEL_AXE);
			}
			else if (player.getInventory().hasItem(ToolItem.IRON_AXE)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.IRON_AXE);
			}
			else if (player.getInventory().hasItem(ToolItem.STONE_AXE)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.STONE_AXE);
			}
			else if (player.getInventory().hasItem(ToolItem.WOODEN_AXE)){
				tool = (ToolItem) player.getInventory().getItem(ToolItem.WOODEN_AXE);
			}
		}
			Integer baseChopRate = 0;
			Boolean fists = false;
			if (tool==null){
				baseChopRate = 1;
				fists = true;
			}
			else if (tool.id()==ToolItem.DIAMOND_AXE){
				baseChopRate = 5;
			}
			else if (tool.id()==ToolItem.STEEL_AXE){
				baseChopRate = 5;
			}
			else if (tool.id()==ToolItem.IRON_AXE){
				baseChopRate = 4;
			}
			else if (tool.id()==ToolItem.STONE_AXE){
				baseChopRate = 3;
			}
			else if (tool.id()==ToolItem.WOODEN_AXE){
				baseChopRate = 2;
			} else {
				baseChopRate = 1;
			}
			
			Integer acquired = 0;
			Integer apples = 0;
			Integer rchop = 0;
			for (int i=0; i<h; i++){
				if (currentLocation.getResourceCache().hasItem(Item.WOOD)==false){
					print("You've depleted this location's wood supply. You'll have to go exploring to find more trees...");
					return;
				}
				rchop=baseChopRate+randInt(-1, 1);
				if (rchop > currentLocation.getResourceCache().getItem(Item.WOOD).getQuantity()){
					rchop = currentLocation.getResourceCache().getItem(Item.WOOD).getQuantity();
				}
				currentLocation.getResourceCache().removeItem(Item.WOOD, rchop); //Remove the wood from the location
				player.getInventory().addItem(new Item(Item.WOOD, rchop)); //Add the wood to the player
				acquired += rchop;
				time += 60;
				player.loseEnergy(randInt(0, 5));
				if (currentLocation.getResourceCache().hasItem(FoodItem.APPLE)){
					currentLocation.getResourceCache().removeItem(FoodItem.APPLE, 1);
					player.getInventory().addItem(new FoodItem(FoodItem.APPLE, 1));
					apples++;
				}
			}
			if (!fists && baseChopRate == 1){
				print("You chop wood for "+h+" hours with your "+tool.getName()+". You collect "+acquired+" wood.");
				print(tool.getName()+"s weren't made to chop trees. The tool wears out twice as quickly.");
				tool.damage(8*h);
			}
			else if (fists){
				print("You punch trees for "+h+" hours with your bare fists! You collect "+acquired+" wood.");
				print("Punching trees hurts! You take "+2*h+" damage.");
				player.damage(2*h);
			}
			else {
				print("You chop wood for "+h+" hours with your "+tool.getName()+". You collect "+acquired+" wood.");
				tool.damage(4*h);
			}
			if (apples>0){
				print("You also gather "+apples+" apples from the fallen trees.");
			}
			if (acquired==0){
				print("These trees are awfully thick! You didn't get any wood.");
				print("Don't give up, try chopping longer, or crafting a better tool!");
			}
	}
	
	private static void showInventory(Inventory inv){
		Integer blankLines = 22;
		print("[ INVENTORY : "+inv.getTotalItems()+" / "+inv.getMaxItems()+" Items ]");
		for (Item i : inv){
			if (i instanceof ToolItem){
				print("[ "+i.getName()+" x"+i.getQuantity()+" - "+((ToolItem) i).getWearPercent()+"% wear ]");
			}
			else {
				print("[ "+i.getName()+" x"+i.getQuantity()+" ]");
			}
			blankLines--;
		}
		printEmptyLines(blankLines);
	}
	
	private static void setOption(Integer i, Boolean s){
		String statement = "";
		options[i] = s;
		if (i==0){
			statement = statement.concat("Auto-save ");
		}
		if (i==1){
			statement = statement.concat("Terminal clearing ");
		}
		if (i==2){
			statement = statement.concat("Debug/Verbose Mode ");
		}
		if (i==3){
			statement = statement.concat("Cheats ");
		}
		if (s==true){
			statement = statement.concat("enabled.");
		}
		if (s==false){
			statement = statement.concat("disabled.");
		}
		print(statement);
	}
	
	private static void printEmptyLines(Integer x){
		for (int i=0; i<x; i++){
			print("");
		}
	}
	
	private static void pause(){
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		print("ENTER to continue >>>");
		s.nextLine();
	}
	
	public static void print(String s){
		System.out.println(s);
	}

	public static Integer randInt(int min, int max){
		max++; //Add 1 to max because the value is exclusive
		Random r = new Random();
		return r.nextInt(max-min)+min;
	}

	@SuppressWarnings("resource")
	private static boolean confirm(String msg){
		print(msg+" (y/n)");
		Scanner c = new Scanner(System.in);
		String conf = c.nextLine();
		if (conf.equalsIgnoreCase("y") || conf.equalsIgnoreCase("yes")){
			return true;
		} else {
			return false;
		}
	}

	private static void showTutorial(){
		print("MINCERAFT TUTORIAL | Page 1 / 4");
		print("Minceraft is an open-world text-based survival game.");
		print("If you've ever played Zork, it's a little bit like that.");
		print("You perform tasks by typing them in.");
		print("Minceraft recognizes many words in many orders, and even some prepositions.");
		print("Your goal is to survive, explore, thrive, build, and eventually");
		print("Find your way home from this mysterious blocky universe.");
		printEmptyLines(16);
		pause();
		print("MINCERAFT TUTORIAL | Page 2 / 4");
		print("When you begin, your first day should probably be spent");
		print("gathering food or chopping down trees.");
		print("Monsters attack you at night, so you'll have to fight them");
		print("off if that happens - or build a house to protect yourself.");
		print("Be sure to build a workbench, because that allows you to craft tools and weapons.");
		print("A fire can deter monsters, and you can use it to cook food.");
		print("There are eight different types of house. Each one provides different levels");
		print("of security and extra storage.");
		printEmptyLines(13);
		pause();
		print("MINCERAFT TUTORIAL | Page 3 / 4");
		print("Your energy is limited. Everything you do causes you to lose energy.");
		print("You can refill your energy and health by eating food.");
		print("Cooked food restores a lot more energy than its raw conterpart.");
		print("Be careful! If you keep working with no energy, you'll lose health instead!");
		print("Obviously, when your health reaches 0%, you die.");
		print("When you die, you lose everything you were carrying and your skills decrease.");
		printEmptyLines(16);
		pause();
		print("MINCERAFT TUTORIAL | Page 4 / 4");
		print("You can look at your inventory by typing 'view my inventory' or similar.");
		print("You can carry 20 total items at any time.");
		print("If you need more storage, you can build a house to hold hundreds of items!");
		print("You can also view your map, which shows locations you've found.");
		print("In the game, type 'help' for a complete list of commands.");
		printEmptyLines(17);
		pause();
	}

	private static int[] rawTimeToDMH(int min){
		int[] result = new int[3];
		result[0] = min/24/60;
		result[1] = min/60%24;
		result[2] = min%60;
		if (options[2]){
			print("[DEBUG] Raw time separated values are "+result.toString());
		}
		return result;
	}

	private static String DMHToTimeString(int[] time){
		if (time[0]==0 && time[1] == 0 && time[2] != 0){
			return time[2] + " minutes";
		}
		else if (time[0]==0 && time[1] != 0 && time[2] == 0){
			return time[1] + " hours";
		}
		else if (time[0]!=0 && time[1] == 0 && time[2] == 0){
			return time[0] + " days";
		}
		else if (time[0]==0 && time[1] != 0 && time[2] != 0){
			return time[1] + " hours and "+time[2]+" minutes";
		}
		else if (time[0]!=0 && time[1] != 0 && time[2] == 0){
			return time[0] + " days and "+time[1]+" hours";
		}
		else {
			return time[0] + " days, "+time[1]+" hours and "+time[2]+" minutes";
		}
	}

	private static void showMainGameScreen(){
		int day = rawTimeToDMH(time)[0] + 1;
		int hour = rawTimeToDMH(time)[1];
		int minute = rawTimeToDMH(time)[2];
		if (options[2]){
			print("[DEBUG] Raw time value is "+time);
			print("[DEBUG] DMH values: "+day+" "+hour+" "+minute);
		}
		String minString = "";
		if (minute < 10){
			minString = "0"+minute;
		} else {
			minString = ""+minute;
		}
		
		int hourActual = 0;
		String ap = "";
		
		if (hour < 6){
			hourActual = hour + 6;
			ap = "AM";
		}
		else if (hour > 6 && hour < 18){
			hourActual = hour - 6;
			ap = "PM";
		}
		else if (hour > 18){
			hourActual = hour - 18;
			ap = "AM";
		}
		else if (hour == 6){
			hourActual = 12;
			ap = "PM";
		}
		else if (hour == 18){
			hourActual = 12;
			ap = "AM";
		}
		
		String healthMeter = "";
		for (int i=0; i<player.getHealth()/2; i++){
			healthMeter = healthMeter.concat("#");
		}
		for (int i=healthMeter.length(); i<50; i++){
			healthMeter = healthMeter.concat(" ");
		}
		
		String energyMeter = "";
		for (int i=0; i<player.getEnergy()/2; i++){
			energyMeter = energyMeter.concat("=");
		}
		for (int i=energyMeter.length(); i<50; i++){
			energyMeter = energyMeter.concat(" ");
		}
		String hspaces;
		String espaces;
		if (player.getHealth()==100){
			hspaces="   ";
		} else {
			hspaces="    ";
		}
		if (player.getEnergy()==100){
			espaces="   ";
		} else {
			espaces="    ";
		}
		print("--------------------------------------------------------------------------------");
		print("[ "+player.getName()+" ] [ DAY "+day+" / "+hourActual+":"+minString+" "+ap+" ] [ Location : "+currentLocation.getName()+" ]");
		printEmptyLines(1);
		print("[ HEALTH    {"+healthMeter+"} "+player.getHealth()+"%"+hspaces+"]");
		print("[ ENERGY    {"+energyMeter+"} "+player.getEnergy()+"%"+espaces+"]");
		printEmptyLines(2);
		printSceneryText(); //6 Lines
		printEmptyLines(2);
		printTip(); //2 Lines
		print("--------------------------------------------------------------------------------");
		printEmptyLines(3);
		print("What do you do now?");
	}

	public static Boolean isNumeric(String s){
		try {
			Integer.parseInt(s);
		} catch (Exception e){
			return false;
		}
		return true;
	}

	private static Integer uniqueLocations(){
		ArrayList<Integer> typesSeen = new ArrayList<Integer>();
		for (Location l : map){
			if (typesSeen.contains(l.getType())==false){
				typesSeen.add(l.getType());
			}
		}
		return typesSeen.size();
	}
	
	private static void saveGame(){
		try {
			FileManager.save();
			print("Game successfully saved under saves/"+player.getName()+".");
		} catch (IOException e){
			print("Error: Could not save the game file!");
			e.printStackTrace();
		}
	}
	
	private static void loadRecipes(){
		craftRegistry = new ArrayList<Recipe>();
		cookRegistry = new ArrayList<Recipe>();
		smeltRegistry = new ArrayList<Recipe>();
		
		//Regular item crafting
		craftRegistry.add(Recipe.STICK);
		craftRegistry.add(Recipe.ARROW);
		craftRegistry.add(Recipe.COMPASS);
		craftRegistry.add(Recipe.COMPASS_BASALT);
		craftRegistry.add(Recipe.BUCKET);
		
		//Tool crafting
		craftRegistry.add(Recipe.AXE_WOOD);
		craftRegistry.add(Recipe.AXE_STONE);
		craftRegistry.add(Recipe.AXE_IRON);
		craftRegistry.add(Recipe.AXE_STEEL);
		craftRegistry.add(Recipe.AXE_DIAMOND);
		craftRegistry.add(Recipe.PICK_WOOD);
		craftRegistry.add(Recipe.PICK_STONE);
		craftRegistry.add(Recipe.PICK_IRON);
		craftRegistry.add(Recipe.PICK_STEEL);
		craftRegistry.add(Recipe.PICK_DIAMOND);
		craftRegistry.add(Recipe.SHOVEL_WOOD);
		craftRegistry.add(Recipe.SHOVEL_STONE);
		craftRegistry.add(Recipe.SHOVEL_IRON);
		craftRegistry.add(Recipe.SHOVEL_STEEL);
		craftRegistry.add(Recipe.SHOVEL_DIAMOND);
		craftRegistry.add(Recipe.HAMMER_STONE);
		craftRegistry.add(Recipe.HAMMER_IRON);
		craftRegistry.add(Recipe.HAMMER_STEEL);
		craftRegistry.add(Recipe.HAMMER_DIAMOND);
		craftRegistry.add(Recipe.FISHING_ROD);
		craftRegistry.add(Recipe.AXE_STONE);
		craftRegistry.add(Recipe.AXE_IRON);
		craftRegistry.add(Recipe.AXE_STEEL);
		craftRegistry.add(Recipe.AXE_DIAMOND);
		
		//Weapon crafting
		craftRegistry.add(Recipe.WEAPON_WOOD);
		craftRegistry.add(Recipe.WEAPON_STONE);
		craftRegistry.add(Recipe.WEAPON_IRON);
		craftRegistry.add(Recipe.WEAPON_STEEL);
		craftRegistry.add(Recipe.WEAPON_DIAMOND);
		craftRegistry.add(Recipe.BOW);
		craftRegistry.add(Recipe.LONG_BOW);
		craftRegistry.add(Recipe.COMPOUND_BOW);
		
		// TODO Register armor recipes
		
		//Cooking recipes
		cookRegistry.add(Recipe.COOK_PORK);
		cookRegistry.add(Recipe.COOK_BEEF);
		cookRegistry.add(Recipe.COOK_FISH);
		cookRegistry.add(Recipe.COOK_BREAD);
		
		//Smelting recipes
		smeltRegistry.add(Recipe.SMELT_IRON);
		smeltRegistry.add(Recipe.SMELT_GOLD);
		smeltRegistry.add(Recipe.SMELT_STEEL);
		smeltRegistry.add(Recipe.SMELT_GLASS);
		smeltRegistry.add(Recipe.SMELT_CLAY);
	}
	
	protected static void toolBreak(ToolItem t){
		print("Your "+t.getName()+" has worn out and breaks. You'll need to craft another.");
		player.getInventory().flushZeroStacks();
	}

	private static void showHelp(){
		print("[ LIST OF COMMANDS ]");
		print("The first word of your command is what matters.");
		print("VIEW - View inventories, map, or skills i.e. 'View house inventory',");
		print("'View my inventory', 'View the map', 'View skills'");
		print("TURN - Turn game options on or off.");
		print("SAVE - Save the game to a file. Also try 'Save and quit'");
		print("QUIT - Quit and close the command prompt. Save first!");
		print("");
		print("TRAVEL - Travel to another location, or 'Travel home'");
		print("EXPLORE - Explore the area and mark down new locations to visit.");
		print("MINE - Mine the ground, find minerals, and maybe something valuable...");
		print("CHOP - Chop down trees and gather wood.");
		print("GATHER* - Gather fruit and other various resources from your location.");
		print("HUNT* - Hunt animals to collect meat and other resources.");
		print("BUILD - Construct a house or other structure like a workbench.");
		print("EAT* - Eat food to replenish your energy and health.");
		print("CRAFT - Craft items and tools out of resources you gather.");
		print("COOK* - Cook food to increase its nutritional value.");
		print("SMELT* - Smelt ore and other materials to create other materials.");
		print("TRADE* - Trade with the locals for gold coins and valuable stuff.");
		print("With some commands you can choose the tool or weapon to use.");
		print("You can also choose how long (in hours) to do a task for.");
		print("");
		pause();
	}

	public static String itemListAsString(ArrayList<Item> items){
		String ls = "";
		for (int i=0; i<items.size(); i++){
			ls=ls.concat(items.get(i).getQuantity()+" "+items.get(i).getName());
			if (i==items.size()-2){
				ls=ls.concat(" and ");
			} else if (i==items.size()-1){
				//Add nothing, this is the last ingredient
			} else {
				ls=ls.concat(", ");
			}
		}
		return ls;
	}

	private static void printSceneryText(){
		// TODO Actual scenery
		print("The world is black. All you see is darkness.");
		print("White text scrolls in front of you.");
		print("This is a placeholder for the scenery text.");
		print("It'll be added in a later version!");
		print("");
		print("");
	}

	private static void printTip(){
		print("[TIP] Type things to make things happen!");
		print("This is a placeholder for helpful game tips.");
	}

	private static void showLocationData(){
		print("[ LOCATION : "+currentLocation.getName().toUpperCase()+" ]");
		printEmptyLines(2);
		print("Coordinates: x="+currentLocation.getX()+", y="+currentLocation.getY());
		printEmptyLines(1);
		print("Type: "+currentLocation.getBiome());
		printEmptyLines(2);
		print("[ BUILDINGS ]");
		if (currentLocation.hasBuilding("Workbench")){
			print("Workbench : Yes");
		} else {
			print("Workbench : No");
		}
		if (currentLocation.hasBuilding("Fire")){
			print("Bonfire : Yes");
		} else {
			print("Bonfire : No");
		}
		if (currentLocation.hasBuilding("Smelter")){
			print("Smelter : Yes");
		} else {
			print("Smelter : No");
		}
		if (currentLocation.hasBuilding("Potion Brewery")){
			print("Potion Brewery : Yes");
		} else {
			print("Potion Brewery : No");
		}
		printEmptyLines(1);
		if (currentLocation.getHouseLevel()==0){
			print("House: No");
		}
		else if (currentLocation.getHouseLevel()==1){
			print("House: Sod Hut ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==2){
			print("House: Wooden Shack ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==3){
			print("House: Stone Cottage ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==4){
			print("House: Log Cabin ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==5){
			print("House: Stone House ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==6){
			print("House: Mansion ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==7){
			print("House: Castle ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		else if (currentLocation.getHouseLevel()==8){
			print("House: Palace Fortress ("+currentLocation.getHouseInventory().getTotalItems()+" / "+currentLocation.getHouseInventory().getMaxItems()+" items in storage)");
		}
		
		if (currentLocation.getFarmLevel()==0){
			print("Farm: No");
		}
		else if (currentLocation.getFarmLevel()==1){
			print("Farm: Small");
		}
		else if (currentLocation.getFarmLevel()==2){
			print("Farm: Medium");
		}
		else if (currentLocation.getFarmLevel()==3){
			print("Farm: Large");
		}
		printEmptyLines(7);
	}

	private static void showRecipeBook(String search){
		Integer e = 22;
		print("[ Crafting recipes containing "+search+" ]");
		for (Recipe r : craftRegistry){
			if (r.getProduct().getName().toLowerCase().contains(search.toLowerCase())){
				print("[ Craft "+r.getProduct().getName()+": requires "+itemListAsString(r.getIngredients())+" ]");
				e--;
			}
		}
		printEmptyLines(e);
	}

	private static void combineLikeItems(ArrayList<Item> items){
		ArrayList<Item> output = new ArrayList<Item>();
		ArrayList<Item> input = new ArrayList<Item>(items);
		for (Item x : input){
			for (Item y : output){
				if (x.id()==y.id()){
					y.updateQuantity(x.getQuantity());
				}
			}
		}
	}
}
