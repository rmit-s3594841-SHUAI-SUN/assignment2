package application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Driver {

	/**
	 * The author is Shuai Sun
	 */

	TreeMap<String, Person> miniNet = new TreeMap<String, Person>();

	/**
	 * Add a person to the MiniNet
	 */
	public String addPerson(Person person) {
		miniNet.put(person.getName(), person);
		System.out.println(person.getName()+" joined this Net");
		String result = person.getName()+" joined this Net";
		return result;
	}


	/**
	 * Show all relation of this person
	 */
	public String listRelation(Person person) {
		String result = "";
		for(Person curr : person.getRelation().keySet()) {
			result += curr.getName()+" is "+person.getName()+"'s "+curr.getRelation().get(person)+"\n";
		}
		return result;
	}


	/**
	//this method uses Dijkstra Algorithm to computer the shortest path
	//each person in miniNet is treated as a vertex and each relation is treated
	//as an edge. Then the shortest relation chain is same as the shortest path.
	//an arraylist 'track' is used to store the path from one person to another
	//it is a recursion.
	 */
	public HashMap<Person, ArrayList<Person>> Mdjstl(Person first, HashMap<Person, ArrayList<Person>> djstl) {
		HashMap<Person, ArrayList<Person>> result = new HashMap<>();
		while(!djstl.isEmpty()) {
			//first time, add relation
			for(Person person: first.getRelation().keySet()) {
				if(djstl.containsKey(person)) {
					if(djstl.get(person).size() == 0) {
						djstl.get(person).addAll(djstl.get(first));
						djstl.get(person).add(first);
					}
				}	
			}
			djstl.get(first).add(first);
			result.put(first, djstl.get(first));
			djstl.remove(first);
			first = shortest(djstl);
		}
		return result;
		
	}


	/**
	 * a support method to look for the shortest path
	*/
	private Person shortest(HashMap<Person, ArrayList<Person>> djstl) {
		Person curr = null;
		int min = Integer.MAX_VALUE;
		for(Person person : djstl.keySet())
		{
			if(djstl.get(person).size()!=0 && djstl.get(person).size()<min) {
				min = djstl.get(person).size();
				curr = person;
			}
		}
		if(curr == null) {
			if(djstl.keySet().iterator().hasNext())
			curr = djstl.keySet().iterator().next();
		}
		return curr;
	}


	/**
	 * identify the relationship between two people in miniNet
	 * if they have direct relationship then print their relation
	 * if they are have no relation but are connected in some way, then print the relation chains
	 * if they are not connected, print they are not connected.
	*/
	public String identifyRelation(Person first, Person second) {
		boolean isConnected = false;
		String result = null;
		if(first.getRelation().containsKey(second)) {
			System.out.println(second.getName()+" is "+first.getName()+"'s "+second.getRelation().get(first));
			result = second.getName()+" is "+first.getName()+"'s "+second.getRelation().get(first);
			isConnected = true;
			return result;
		}
		if(!isConnected) {
			HashMap<Person, ArrayList<Person>> djstl = new HashMap<>();
			//add all people in miniNet into table djstl
			for(String name : miniNet.keySet()) {
				djstl.put(miniNet.get(name), new ArrayList<Person>());
			}
			djstl = Mdjstl(first, djstl);
	
			ArrayList<Person> track = new ArrayList<>();
			track = djstl.get(second);
			
			if(track.size()>1) {
				isConnected = true;
				System.out.println();
				System.out.println("The connection chain bewteen "+first.getName()+
						" and "+second.getName()+" is shown below:");
				result = first.getName()+" and "+second.getName()+" have no relation";
				result += "\nThe connection chain bewteen "+first.getName()+
						" and "+second.getName()+" is shown below:";
				for(int i = 0; i+1 < track.size(); i++)
				{
					System.out.println(track.get(i).getName()+" is "+
							track.get(i+1).getName()+"'s "+
							track.get(i).getRelation().get(track.get(i+1)));
					result += "\n"+track.get(i).getName()+" is "+
							track.get(i+1).getName()+"'s "+
							track.get(i).getRelation().get(track.get(i+1));
				}
				return result;
			}
		}
		if(!isConnected) {
			System.out.println(first.getName()+" and "+second.getName()+" are not connected.");
			result = first.getName()+" and "+second.getName()+" are not connected.";
			return result;
		}
		return result;
	}


	/**
	 * List everyone
	*/
	public String listEveryone() {
		String text = "";
		Person person;
		for(String name : miniNet.keySet()) {
			person = miniNet.get(name);
			System.out.println(name+", "+ person.getImageName()+", "
					+person.getStatus()+", "+person.getGender()+", "
					+person.getAge()+", "+person.getState());
			text += name+", "+ person.getImageName()+", "
					+person.getStatus()+", "+person.getGender()+", "
					+person.getAge()+", "+person.getState()+"\n";
		}
		return text;
	}


	/**
	 * as we consider object store in map miniNet in the relation network
	 * to delete a person equals to remove this person from TreeMap miniNet
	*/
	public void deletePerson(Person person) {
		miniNet.remove(person.getName());
		//after remove the person, relationships that this person connects to 
		//other people in this net are also removed.
		for(String name : miniNet.keySet()) {
			if(miniNet.get(name).getRelation().containsKey(person)) {
				miniNet.get(name).getRelation().remove(person);
			}
		}
	}


	/**
	 * read person profile from person.txt or database
	 * name, imagename, status, age, gender, state
	*/
	public void readPersonFromFile() {

		RWFileData rw = new RWFileData();
		try {
			for(String profile: rw.readPersonData()) {
				String[] str = profile.split(", ", -1);
				String name = str[0];
				String imageName = str[1];
				String status = str[2];
				String gender = str[3];
				int age = Integer.valueOf(str[4]);
				String state = str[5];
				if(age>16) addPerson(new Adult(name, imageName, status, gender, age, state));
				else if(age < 17 && age >2) addPerson(new Child(name, imageName, status, gender, age, state));
				else if( age < 3 ) addPerson(new YoungChild(name, imageName, status, gender, age, state));
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * read person profile from person.txt or database
	 * if there is no one profile store in the miniNet, program can consider that
	 * there is no data stored in .txt and database
	 * no matter data loss or it is exactly empty
	*/
	public void readRelationFromFile() {
		if(miniNet.size() == 0) return;
		RWFileData rw = new RWFileData();
		try {
			for(String relation : rw.readRelationData()) {
				String[] str = relation.split(", ", -1);
				if(str[2].equals("parent")) {
					if(miniNet.get(str[0]) instanceof Child) {
						miniNet.get(str[0]).addRelation(miniNet.get(str[1]), "child");
					}else miniNet.get(str[1]).addRelation(miniNet.get(str[0]), "child");
				}else miniNet.get(str[0]).addRelation(miniNet.get(str[1]), str[2]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeDataIntoFile() {
		RWFileData rw = new RWFileData();
		rw.writePersonData(miniNet);
		rw.writeRelationData(miniNet);
	}
	
	public void writeDataIntoDataBase() {
		RWDatabase db = new RWDatabase();
		db.startServer();
		db.writePersonIntoDataBase(miniNet);
		db.writeRelationIntoDataBase(miniNet);
		db.stopServer();
	}


}
