import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class FileManager {
	public static Player loadedPlayer;
	public static ArrayList<Location> loadedMap;
	public static int loadedDiff;
	public static int loadedTime;
	public static String loadedLocationName;
	
	public static void save() throws IOException{
		String charName = Minceraft.player.getName();
		FileOutputStream fileOut = new FileOutputStream("saves/"+charName+".sav");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeInt(Minceraft.difficulty);
		out.writeInt(Minceraft.time);
		out.writeChars(Minceraft.currentLocation.getName());
		out.writeObject(Minceraft.player); //This handles health, energy, skill values, and player inventory
		out.writeObject(Minceraft.map); //This handles all locations and their fields including inventories, building lists and farms
		out.flush();
		out.close();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void load(String ch) throws IOException{
		try {
		FileInputStream fileIn = new FileInputStream("saves/"+ch+".sav");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		loadedDiff = in.readInt();
		loadedTime = in.readInt();
		loadedLocationName = in.readLine();
		loadedPlayer = (Player) in.readObject();
		loadedMap = (ArrayList<Location>) in.readObject();
		in.close();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}
