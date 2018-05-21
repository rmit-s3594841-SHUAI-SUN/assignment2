package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hsqldb.Server;

import java.util.ArrayList;
import java.util.TreeMap;


public class RWDatabase {
	/**
	 * The author is Shuai Sun
	 */
	Server hsqlServer = new Server();
	Connection connection = null;
	
	public void startServer(){
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);
		hsqlServer.setDatabaseName(0, "MiniNet");
		hsqlServer.setDatabasePath(0, "file:MiniNet_Path");
		hsqlServer.start();
	}

	public  void stopServer(){
		hsqlServer.stop();
	}



	/**
	 * write the data of people profile into Database
	*/
	public void writePersonIntoDataBase(TreeMap<String, Person> sortedPerson) {
		// making a connection
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:MiniNet", "sa", "123");
			connection.prepareStatement("drop table person if exists;").execute();
			connection.prepareStatement(
					"create table person "
					+ "(name varchar(20) not null, "
					+ "imageName varchar(20),"
					+ "status varchar(40),"
					+ "gender varchar(2),"
					+ "age integer,"
					+ "state varchar(8));").execute();
			String name = "";
			String imageName = "";
			String status = "";
			String gender = "";
			int age = 0;
			String state = "";
			for(String key : sortedPerson.keySet()) {
				Person person = sortedPerson.get(key);
				name = person.getName();
				imageName = person.getImageName();
				status = person.getStatus();
				age = person.getAge();
				gender = person.getGender();
				state = person.getState();
				connection.prepareStatement(
						"insert into person (name, imageName, status, gender, age, state)"
						+ "values ('"+name+"', '"+imageName+"', "
						+ "'"+status+"', '"+gender+"', "+age+", '"+state+"');").execute();
				System.out.println(person.getName()+" insert into database");
			}
			connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			System.out.println("Fail to save data into database due to fail to connect to the database");
		}
		// end of stub code for in/out stub
	}

	/**
	 * write the Relation info into Database
	 * @param sortedPerson
	 */
	public void writeRelationIntoDataBase(TreeMap<String, Person> sortedPerson) {
		// making a connection
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:MiniNet", "sa", "123");
			connection.prepareStatement("drop table relation if exists;").execute();
			connection.prepareStatement(
					"create table relation "
					+ "(firstname varchar(20) not null, "
					+ "secondname varchar(20) not null,"
					+ "type varchar(20) not null);").execute();
			
			for(String firstname : sortedPerson.keySet()) {
				Person first = sortedPerson.get(firstname);
				TreeMap<String, String> relation = new TreeMap<>();
				for(Person person : first.getRelation().keySet()) {
					if(person.getRelation().get(first) == "child") {
						relation.put(person.getName(), "parent");
					}else relation.put(person.getName(), person.getRelation().get(first));
				}
				//replace child into parent
				for(String secondname : relation.keySet()) {
					connection.prepareStatement(
							"insert into person (firstname, secondname, type)"
							+ "values ('"+firstname+"', '"+secondname+"', "
							+ "'"+relation.get(secondname)+"');").execute();
				}
				//sortedPerson.remove(first.getName());
				for(String s : sortedPerson.keySet()) {
					if(sortedPerson.get(s).getRelation().containsKey(first)) {
						sortedPerson.get(s).getRelation().remove(first);
					}
				}
				connection.close();
				//hsqlServer.stop();
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		// end of stub code for in/out stub
	}

	/**load people profile from database
	 * @return ArrayList<String>
	 */
	public ArrayList<String> readPersonFromDataBase() {
		ArrayList<String> result = new ArrayList<>();
		ResultSet rs = null;
		// making a connection
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:MiniNet", "sa", "123");
			//query from the db
			rs = connection.prepareStatement("select * from person;").executeQuery();
			String line = "";
			while(rs.next()) {
			line = rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+", "+rs.getInt(5)+", "+rs.getString(6);
				result.add(line);
			}
			rs.close();
			//connection.commit();
			connection.close();
		} catch (SQLException e2) {
			//use the SQLException to get the information whether there is data
			//stored in the database
			System.out.println("Fail to load data from database because there is no table "
					+ "person in database");		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Fail to connect database.");
		}
		return result;
}


	/**load relation info from database
	 * @return ArrayList<String>
	 */
	public ArrayList<String> readRelationFromDataBase(){
		ArrayList<String> result = new ArrayList<>();
		ResultSet rs = null;
		// making a connection
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:MiniNet", "sa", "123");
			// // query from the db
			rs = connection.prepareStatement("select * from relation;").executeQuery();
			String line = "";
			while(rs.next()) {
				line = rs.toString();
				result.add(line);
			}
			//connection.commit();
			rs.close();
			connection.close();
			} catch (SQLException e2) {
				//use the SQLException to get the information whether there is data
				//stored in the database
				System.out.println("Fail to load data from database because there is no table "
						+ "relation in database");
			} catch (ClassNotFoundException e2) {
				System.out.println("Fail to connect the database");
			}
		return result;
	}
}
	
	
