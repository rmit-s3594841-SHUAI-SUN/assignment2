package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;


public class RWFileData {
	/**
	 * The author is Xuesong Zeng
	 */

	private BufferedReader br;
	private BufferedWriter bw = null;
	private File personFile = new File("Person.txt");
	private File relationFile = new File("Relation.txt");
	

	/**
	 * read file from person.txt
	 * if person.txt does not exists, load people profile from database
	 */
	public ArrayList<String> readPersonData() throws IOException {
		if (!personFile.exists()) {  
			//call the method to read DataBase
		    RWDatabase db = new RWDatabase();
		    return db.readPersonFromDataBase();
		}else {
			ArrayList<String> result = new ArrayList<String>();
			br = new BufferedReader(new FileReader(personFile));
			String line = "";
			try {
				while((line = br.readLine()) != null) {
					result.add(line);
				}		
				return result;
			}catch(IOException e){
				System.out.println();
			}finally {
				br.close();	
			}
		}
		return null;
	}


	/**
	 * read file from relation.txt
	 * if relation.txt does not exists, load relation profile from database
	 */
	public ArrayList<String> readRelationData() throws IOException {
		if (!relationFile.exists()) {  
			//call the method to read DataBase
		    RWDatabase db = new RWDatabase();
		    return db.readRelationFromDataBase();
		}else {
			ArrayList<String> result = new ArrayList<String>();
			br = new BufferedReader(new FileReader(relationFile));
			String line = "";
			try {
				while((line = br.readLine()) != null) {
					result.add(line);
				}		
				return result;
			}catch(IOException e){
				System.out.println();
			}finally {
				br.close();	
			}
		}
		return null;
	}


	/**
	 * write the Person profile into person.txt
	 * if person.txt does not exist, then create a new file named person.txt
	*/
	public void writePersonData(TreeMap<String, Person> sortedPerson) {
		try {
			if (!personFile.exists()) personFile.createNewFile();   
			FileWriter fw = new FileWriter(personFile);
			bw = new BufferedWriter(fw);
			
			for(String key : sortedPerson.keySet()) {
				Person person = sortedPerson.get(key);
				StringBuilder sb = new StringBuilder();
				sb.append(person.getName()).append(", ").append(person.getImageName()).append(", ").append(person.getStatus()).append(", ").append(person.getGender()).append(", ").append(person.getAge()).append(", ").append(person.getState());			
				bw.write(sb.toString());
				bw.newLine();
			}
			bw.flush();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * write the Relation profile into person.txt
	 * if relation.txt does not exist, then create a new file named relation.txt
	 */
	public void writeRelationData(TreeMap<String, Person> sortedPerson) {
		
		try {
			if(!relationFile.exists()) {
				relationFile.createNewFile();
			}
			FileWriter fw = new FileWriter(relationFile);
			bw = new BufferedWriter(fw);
			for(String name : sortedPerson.keySet()) {
				Person first = sortedPerson.get(name);
				TreeMap<String, String> relation = new TreeMap<>();
				for(Person person : first.getRelation().keySet()) {
					if(person.getRelation().get(first) == "child") {
						relation.put(person.getName(), "parent");
					}else relation.put(person.getName(), person.getRelation().get(first));
				}
				//replace child into parent
				for(String second : relation.keySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append(first.getName()).append(", ").append(second).append(", ").append(relation.get(second));	
					bw.write(sb.toString());
					bw.newLine();
				}
				//sortedPerson.remove(first.getName());
				for(String s : sortedPerson.keySet()) {
					if(sortedPerson.get(s).getRelation().containsKey(first)) {
						sortedPerson.get(s).getRelation().remove(first);
					}
				}
			}
			bw.flush();
			bw.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
