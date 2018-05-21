package application;
import java.util.HashMap;

public abstract class Person {
	/**
	 * The author is Shuai Sun
	 */

    private String name;
    private String imageName;
    private String status;
    private String gender;
    private int age;
    private String state;
    
	private HashMap<Person, String> relation = new HashMap<>();
	
	public Person(String name, String imageName, String status, String gender, int age, String state) {
		this.name = name;
		this.imageName = imageName;
		this.status = status;
		this.gender = gender;
		this.age = age;
		this.state = state;
	}
	

	public String getName() {
		return name;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getState() {
		return state;
	}
	
	public HashMap<Person, String> getRelation(){
		return relation;
	}


	protected void setName(String name) {
		this.name = name;
	}


	protected void setImageName(String imageName) {
		this.imageName = imageName;
	}


	protected void setStatus(String status) {
		this.status = status;
	}


	protected void setGender(String gender) {
		this.gender = gender;
	}


	protected void setAge(int age) {
		this.age = age;
	}


	protected void setState(String state) {
		this.state = state;
	}
	
	protected abstract void addRelation(Person person, String type);

}
