package application;

public class Adult extends Person {

	public Adult(String name, String imageName, String status, String gender, int age, String state) {
		super(name, imageName, status, gender, age, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addRelation(Person person, String type) {
		// TODO Auto-generated method stub
		//throw invalid relation type
		this.getRelation().put(person, type);
		person.getRelation().put(this, type);
	}


}
