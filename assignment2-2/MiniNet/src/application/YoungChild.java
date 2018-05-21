package application;

public class YoungChild extends Person {

	/**
	 * The author is Xuesong Zeng
	 */

	public YoungChild(String name, String imageName, String status, String gender, int age, String state, Adult first, Adult second) {
		super(name, imageName, status, gender, age, state);
		// TODO Auto-generated constructor stub
		this.addRelation(first, "child");
		this.addRelation(second, "child");
	}

	public YoungChild(String name, String imageName, String status, String gender, int age, String state) {
		// TODO Auto-generated constructor stub
		super(name, imageName, status, gender, age, state);
	}

	@Override
	protected void addRelation(Person person, String type) {
		// TODO Auto-generated method stub
		//throw invalid relation type
		if(type == "child") {
			for(Person related : person.getRelation().keySet())
			{
				if(person.getRelation().get(related) == "parent"){
					this.addRelation(related, "sibling");
				}
			}
			this.getRelation().put(person, "child");
			person.getRelation().put(this, "parent");
		}else {
			this.getRelation().put(person, type);
			person.getRelation().put(this, type);
		}
	}

}
