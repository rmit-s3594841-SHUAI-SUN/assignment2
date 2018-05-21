package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;

public class Controller {

	/**
	 * Only this class , we have two authors
	 * The authors are Shuai Sun and Xuesong Zeng
	 * Because there are lots of pages needed to be set up
	 */
	// load data
	Driver admin = preparedData();

	/**
	 * prepared data
	 * @return Driver
	 */
	private Driver preparedData() {
		Driver admin = new Driver();
		admin.readPersonFromFile();
		admin.readRelationFromFile();
		return admin;
	}

	@FXML
	private Button listA;
	@FXML
	private Button exit;
	@FXML
	private Button selectP;
	@FXML
	private Button addP;
	@FXML
	private Button identifyR;
	@FXML
	private AnchorPane showPane;
	@FXML
	private Text detailTitle;
	@FXML
	private BorderPane operatePane;
	@FXML
	private TextArea displayTx;


	/**
	 * list page and arrange the layout
	 * @param event
	 */
	public void listPage(ActionEvent event) {

		detailTitle.setText("- Everyone in MiniNet is listed here - ");
		// detailTitle.setFont(Font.font("Courier", FontWeight.BOLD, 16));

		Button list = new Button("List EveryOne");
		operatePane.setCenter(list);

		list.setOnAction(refresh -> {
			displayTx.setText(admin.listEveryone());
		});

	}

	/**
	 * Add new person in this page，the majority of the code is
	 * to set up the page
	 * @param event
	 */
	public void addPPage(ActionEvent event) {
		detailTitle.setText("- Please input the person's information to the profile - ");
		// Create a grid pane to allow user enter persons' profile
		GridPane addGri = new GridPane();
		addGri.setAlignment(Pos.CENTER);
		addGri.setPadding(new Insets(15, 15, 15, 15));
		addGri.setHgap(5.5);
		addGri.setVgap(5.5);
		// Place nodes in the pane
		addGri.add(new Label("Name:"), 0, 1);
		TextField nameIn = new TextField();
		addGri.add(nameIn, 1, 1);
		
		addGri.add(new Label("ImageName: "), 0, 2);
		TextField imageIn = new TextField();
		addGri.add(imageIn, 1, 2);
		
		addGri.add(new Label("Gender:"), 0, 3);
		// Create toggle group to select only one gender
		ToggleGroup group = new ToggleGroup();
		RadioButton m = new RadioButton();
		m.setText("Male");
		m.setToggleGroup(group);
		RadioButton f = new RadioButton();
		f.setText("Female");
		f.setToggleGroup(group);
		addGri.add(m, 1, 3);
		addGri.add(f, 1, 4);
		addGri.add(new Label("Age:"), 0, 5);
		TextField Age = new TextField();
		addGri.add(Age, 1, 5);
		addGri.add(new Label("Status:"), 0, 6);
		TextField statusIn = new TextField();
		addGri.add(statusIn, 1, 6);
		addGri.add(new Label("States:"), 0, 7);
		// Define states for user to choose
		String[] st = { "ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA" };
		// Create the combo box
		ComboBox<String> stateIn = new ComboBox<String>();
		stateIn.getItems().addAll(st);
		addGri.add(stateIn, 1, 7);
		addGri.add(new Text("Type: "), 0, 8);
		ComboBox<String> type = new ComboBox<>();
		type.getItems().add("Adult");
		type.getItems().add("Child");
		type.getItems().add("Young Child");
		addGri.add(type, 1, 8);
		
		Button saveNewPerson = new Button("Confirm");
		addGri.add(saveNewPerson, 1, 10);
		saveNewPerson.setOnAction(addP -> {
			//throw exception that new person has same name with someone in the Net
			addNewPersonPageSet(nameIn, imageIn, m, f, type, Age, statusIn, stateIn);
		});
		operatePane.setCenter(addGri);
	}

	/**
	 * This method is to use different conditions to estimate the status of this person
	 * belong which age groups. If you input invalid value，it will throw exception from
	 * different conditions
	 * @param nameIn
	 * @param imageIn
	 * @param m
	 * @param f
	 * @param type
	 * @param ageIn
	 * @param statusIn
	 * @param stateIn
	 */

	private void addNewPersonPageSet(TextField nameIn, TextField imageIn, RadioButton m, RadioButton f,
			ComboBox<String> type, TextField ageIn, TextField statusIn, 
			ComboBox<String> stateIn) {
		String name = "";
		String image = "";
		String gender = "";
		int age = 0;
		String status = "";
		String state = "";
		try {
			if(nameIn.getText() == null) {
				throw new NoValidInputException("Please Enter name for new person.");
			}else name = nameIn.getText();

			if (f.isSelected()) gender = "F";
			else if(m.isSelected()) gender = "M";
			else throw new NoValidInputException("Please choose gender for new person.");
			
			image = imageIn.getText();
			//use the System Exception
			age = Integer.valueOf(ageIn.getText());
			
			if(statusIn.getText() == null) {
				throw new NoValidInputException("Please Enter Status for new person.");
			}else status = statusIn.getText();
			
			if(stateIn.getValue() == null) {
				throw new NoValidInputException("Please choose State for new person.");
			}else state = stateIn.getValue();

			if(age > 150 || age < 0) {
				throw new NoSuchAgeException("Please enter a valid number for age");
			}
			if(type.getValue() == "Adult") {
				//throw exception on age
				if(age < 17) {
					throw new NoSuchAgeException("Age of an Adult should be more than 16.");
				}
				Adult adult = new Adult(name, image, status, gender, age, state);
				admin.addPerson(adult);
				displayTx.setText(adult.getName() + " has joined into MiniNet");
			}else if(type.getValue() == "Child") {
				if(age < 3 || age > 16) {
					throw new NoSuchAgeException("Age of a Child should be more than 2 and less than 16.");
				}
				addNewChildPageSet(name, image, gender, status, age, state);
			}else if(type.getValue() == "Young Child") {
				if(age > 2) {
					throw new NoSuchAgeException("Age of a Young Child should be less than 3.");
				}
				addNewYoungChildPageSet(name, image, gender, status, age, state);
			}else throw new NoValidInputException("Please Choose a type for new person.");
			} catch (NoValidInputException e) {
			// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			} catch (NoSuchAgeException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				displayTx.setText("Please Enter a Number into Age.");
			}
	}


	/**
	 * Set up the the page of adding new Young Child
	 * If you want to add a new Young Child, firstly, you need to arrange two adult who are couple
	 * becoming their parents
	 * @param name
	 * @param image
	 * @param gender
	 * @param status
	 * @param age
	 * @param state
	 */
	private void addNewYoungChildPageSet(String name, String image, String gender,
			String status, int age, String state) {
		GridPane addYoungChild = new GridPane();
		addYoungChild.setAlignment(Pos.CENTER);
		addYoungChild.setPadding(new Insets(15, 15, 15, 15));
		addYoungChild.setHgap(5.5);
		addYoungChild.setVgap(5.5);
		// Place nodes in the pane
		detailTitle.setText("- Please select 2 parents for the child -");
		addYoungChild.add(new Label("Parent One:"), 0, 1);
		addYoungChild.add(new Label("Parent Two:"), 0, 2);
		
		ComboBox<String> parentOne = new ComboBox<>();
		for(String parent : admin.miniNet.keySet()) {
			parentOne.getItems().add(parent);
		}
		ComboBox<String> parentTwo = new ComboBox<>();
		for(String parent : admin.miniNet.keySet()) {
			parentTwo.getItems().add(parent);
		}
		addYoungChild.add(parentOne, 1, 1);
		addYoungChild.add(parentTwo, 1, 2);
		Button newYoungChild = new Button("Confirm");
		addYoungChild.add(newYoungChild, 1, 3);
		
		newYoungChild.setOnAction(newyoungchild ->{
			try {
				if(parentOne.getValue() == null || parentTwo.getValue() == null) {
					throw new NoValidInputException("Please Select Parent For this Young Child");
				}
				else {
					Adult first, second;
					first = (Adult) admin.miniNet.get(parentOne.getValue());
					second = (Adult) admin.miniNet.get(parentTwo.getValue());
					if(first.getRelation().containsKey(second) 
							|| first.getRelation().get(second) == "couple"  ) {
						YoungChild youngchild = new YoungChild(name, image, status, gender, age, state, first, second );
						admin.addPerson(youngchild);
						displayTx.setText(youngchild.getName() + " has joined into MiniNet");
					}else throw new NoValidInputException("They are not couples");
				}
			} catch (NoValidInputException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			}
		});
		operatePane.setCenter(addYoungChild);
	}


	/**
	 * Set up the the page of adding new Child
	 * If you want to add a new Child, firstly, you need to arrange two adult who are couple
	 * becoming their parents
	 * @param name
	 * @param image
	 * @param gender
	 * @param status
	 * @param age
	 * @param state
	 */
	private void addNewChildPageSet(String name, String image, String gender,
			String status, int age, String state) {
		GridPane addChild = new GridPane();
		addChild.setAlignment(Pos.CENTER);
		addChild.setPadding(new Insets(15, 15, 15, 15));
		addChild.setHgap(5.5);
		addChild.setVgap(5.5);
		// Place nodes in the pane
		detailTitle.setText("- Please select 2 parents for the child -");
		addChild.add(new Label("Parent One:"), 0, 1);
		addChild.add(new Label("Parent Two:"), 0, 2);
		
		ComboBox<String> parentOne = new ComboBox<>();
		for(String parent : admin.miniNet.keySet()) {
			parentOne.getItems().add(parent);
		}
		ComboBox<String> parentTwo = new ComboBox<>();
		for(String parent : admin.miniNet.keySet()) {
			parentTwo.getItems().add(parent);
		}
		addChild.add(parentOne, 1, 1);
		addChild.add(parentTwo, 1, 2);
		Button newChild = new Button("Confirm");
		addChild.add(newChild, 1, 3);
		
		newChild.setOnAction(newchild ->{
			try {
				if(parentOne.getValue() == null || parentTwo.getValue() == null) {
					throw new NoValidInputException("Please Select Parent For this Child");
				}
				else {
					Adult first, second;
					first = (Adult) admin.miniNet.get(parentOne.getValue());
					second = (Adult) admin.miniNet.get(parentTwo.getValue());
					if(first.getRelation().containsKey(second) 
							|| first.getRelation().get(second) == "couple"  ) {
						Child child = new Child(name, image, status, gender, age, state, first, second );
						admin.addPerson(child);
						displayTx.setText(child.getName() + " has joined into MiniNet");
					}else throw new NoValidInputException("They are not couples");
				}
			} catch (NoValidInputException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			}
		});
		
		operatePane.setCenter(addChild);
	}


	/**
	 * This method can let people choose different pages
	 * in order to access different function
	 * @param event
	 */
	public void selectPage(ActionEvent event) {
		GridPane addGri = new GridPane();
		addGri.setAlignment(Pos.CENTER);
		addGri.setPadding(new Insets(15, 15, 15, 15));
		addGri.setHgap(5.5);
		addGri.setVgap(5.5);
		// Place nodes in the pane
		addGri.add(new Label("Select a Person: "), 0, 1);
		ComboBox<String> peopleList = new ComboBox<String>();
		ArrayList<String> people = new ArrayList<>();
		for(String name : admin.miniNet.keySet()) {
			people.add(name);
		}
		peopleList.getItems().addAll(people);
		addGri.add(peopleList, 1, 1);
		addGri.add(new Label("Option:"), 0, 2);

		ToggleGroup group = new ToggleGroup();
		RadioButton modifyProfile = new RadioButton();
		RadioButton deletePerson = new RadioButton();
		RadioButton addRelation = new RadioButton();
		RadioButton deleteRelation = new RadioButton();
		RadioButton listRelation = new RadioButton();

		modifyProfile.setText("Modify Profile");
		deletePerson.setText("Delete Person");
		addRelation.setText("Add Relation");
		deleteRelation.setText("Delete Relation");
		listRelation.setText("List Relation");

		modifyProfile.setToggleGroup(group);
		deletePerson.setToggleGroup(group);
		addRelation.setToggleGroup(group);
		deleteRelation.setToggleGroup(group);
		listRelation.setToggleGroup(group);

		addGri.add(modifyProfile, 1, 2);
		addGri.add(deletePerson, 1, 3);
		addGri.add(addRelation, 1, 4);
		addGri.add(deleteRelation, 1, 5);
		addGri.add(listRelation, 1, 6);

		Button confirm = new Button("Confirm");
		addGri.add(confirm, 1, 8);
		confirm.setOnAction(cfm ->{
			try {
				confirmSelectOption(peopleList, modifyProfile, 
						deletePerson, addRelation, deleteRelation, listRelation);
			} catch (NoValidInputException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			}
		});
		operatePane.setCenter(addGri);
	}


	/**
	 *Set up the page of identifying relation between two different people
	 * @param event
	 */
	public void identifyPage(ActionEvent event) {

		detailTitle.setText("- Identify the relationship between two people -");
		// Create a grid pane to allow user enter persons' profile
		GridPane cgrid = new GridPane();
		cgrid.setAlignment(Pos.CENTER);
		cgrid.setPadding(new Insets(15, 15, 15, 15));
		cgrid.setHgap(5.5);
		cgrid.setVgap(5.5);
		// Place nodes in the pane
		cgrid.add(new Label("1st Person:"), 0, 0);
		
		ComboBox<String> firstname = new ComboBox<String>();
		cgrid.add(firstname, 1, 0);
		cgrid.add(new Label("2nd Person:"), 0, 1);
		ComboBox<String> secondname = new ComboBox<String>();
		for(String name : admin.miniNet.keySet()) {
			firstname.getItems().add(name);
			secondname.getItems().add(name);
		}

		firstname.setOnAction(e ->{
			for(String name : admin.miniNet.keySet()) {
				if(!secondname.getItems().contains(name))
				secondname.getItems().add(name);
			}
			secondname.getItems().remove(firstname.getValue());	
		});
		secondname.setOnAction(e ->{
			for(String name : admin.miniNet.keySet()) {
				if(!firstname.getItems().contains(name))
				firstname.getItems().add(name);
			}
			firstname.getItems().remove(secondname.getValue());
		});


			
		cgrid.add(secondname, 1, 1);
		Button btc = new Button("Check");
		cgrid.add(btc, 1, 4);
		// Place grid pane in the center
		btc.setOnAction( btcc -> {
			try {
				if(firstname.getValue() != null && secondname.getValue() != null) {
					Person first, second;
					first = admin.miniNet.get(firstname.getValue());
					second = admin.miniNet.get(secondname.getValue());
					String relation = admin.identifyRelation(first, second);
					displayTx.setText(relation);
				}else throw new NoValidInputException("Please Select Person");
			} catch (NoValidInputException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			}
	
		});
		operatePane.setCenter(cgrid);
	}


	/**
	 * To exit this software
	 * write data into file
	 * write data into database
	 * @param event
	 */
	public void exit(ActionEvent event) {
		admin.writeDataIntoFile();
		admin.writeDataIntoDataBase();
		System.exit(0);
	}


	/**
	 * This method is to save all changes of profile of person
	 * Using this function can change the information of a person
	 * @param nameIn
	 * @param peopleList
	 * @param m
	 * @param f
	 * @param Age
	 * @param statusIn
	 * @param stateIn
	 */
	private void confirmModifyProfile(TextField nameIn, ComboBox<String> peopleList,
			RadioButton m, RadioButton f, TextField Age, TextField statusIn, 
			ComboBox<String> stateIn) {
		try {
			Person person;
			person = admin.miniNet.get(peopleList.getValue());
			String name = peopleList.getValue();
			if(!nameIn.getText().isEmpty()) {
				if(admin.miniNet.containsKey(nameIn.getText())){
					throw new SameNameException(nameIn.getText()+" has existed in MiniNet.");
				}
				person.setName(nameIn.getText());
				admin.miniNet.put(nameIn.getText(), person);
				admin.miniNet.remove(peopleList.getValue());
			}

			if(m.isSelected()) {
				person.setGender("M");
			}else if(f.isSelected()) {
				person.setGender("F");
			} 
			if(!Age.getText().isEmpty()) {
				int age = Integer.valueOf(Age.getText());
				if(age > 150 || age < 0) {
					throw new NoSuchAgeException("Please enter a valid number for age");
				}
				if(person instanceof Adult && age <17) {
					//throw exception on age
					throw new NoSuchAgeException("Age of an Adult should be more than 16.");
				}else if(person instanceof Child && (age < 3 || age > 16)) {
					throw new NoSuchAgeException("Age of a Child should be more than 2 and less than 16.");
				}else if(person instanceof YoungChild && age > 2) {
					throw new NoSuchAgeException("Age of a Young Child should be less than 3.");
				}else person.setAge(age);
			}
			
						
			if(!statusIn.getText().isEmpty()) {
				person.setStatus(statusIn.getText());
			}
			if(stateIn.getValue() != null) {
				person.setState(stateIn.getValue());
			}
			selectPage(null);
			displayTx.setText(name+"'s profile has been modified.");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			displayTx.setText("Please Enter a Number into age.");
		} catch (NoSuchAgeException e) {
			displayTx.setText(e.getMessage());
		} catch (SameNameException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		}
	}


	/**
	 * Set up the page of modification Profile
	 * @param peopleList
	 */
	private void modifyProfilePageSet(ComboBox<String> peopleList) {
		displayTx.setText("Enter new content in the place "
				+ "\nwhere you want to make changes and "
				+ "\nkeep blank in the place "
				+ "\nwhere you do not make changes");
		GridPane modifyPage = new GridPane();
		modifyPage.setAlignment(Pos.CENTER);
		modifyPage.setPadding(new Insets(15, 15, 15, 15));
		modifyPage.setHgap(5.5);
		modifyPage.setVgap(5.5);
		// Place nodes in the pane
		Label selectedName = new Label(peopleList.getValue());
		selectedName.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		modifyPage.add(new Label("Seleted Person:"), 0, 0);
		modifyPage.add(selectedName, 1, 0);

		modifyPage.add(new Label("Name:"), 0, 1);
		TextField nameIn = new TextField();
		modifyPage.add(nameIn, 1, 1);
		modifyPage.add(new Label("Gender:"), 0, 2);
		// Create toggle group to select only one gender
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton m = new RadioButton();
		m.setText("Male");
		m.setToggleGroup(genderGroup);
		RadioButton f = new RadioButton();
		f.setText("Female");
		f.setToggleGroup(genderGroup);
		modifyPage.add(m, 1, 2);
		modifyPage.add(f, 1, 3);
		modifyPage.add(new Label("Age:"), 0, 4);
		TextField Age = new TextField();
		modifyPage.add(Age, 1, 4);
		modifyPage.add(new Label("Status:"), 0, 5);
		TextField statusIn = new TextField();
		modifyPage.add(statusIn, 1, 5);
		modifyPage.add(new Label("States:"), 0, 6);
		//Define states for user to choose
		String[] st = { "ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA" };
		//Create the combo box
		ComboBox<String> stateIn = new ComboBox<String>();
		stateIn.getItems().addAll(st);
		modifyPage.add(stateIn, 1, 6);
		Button confirmModification = new Button("Confirm Modification");
		modifyPage.add(confirmModification, 1, 9);
		confirmModification.setOnAction(cfmm ->{
			confirmModifyProfile(nameIn, peopleList, m, f, Age, statusIn, stateIn);
		});
		operatePane.setCenter(modifyPage);
	}


	/**
	 * Set up page layout
	 * To add relation for a person
	 * @param peopleList
	 */
	private void addRelationPageSet(ComboBox<String> peopleList) {
		GridPane addRPage = new GridPane();
		addRPage.setAlignment(Pos.CENTER);
		addRPage.setPadding(new Insets(15, 15, 15, 15));
		addRPage.setHgap(5.5);
		addRPage.setVgap(5.5);
		// Place nodes in the pane
		Label selectedName = new Label(peopleList.getValue());
		selectedName.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		addRPage.add(new Label("Seleted Person:"), 0, 0);
		addRPage.add(selectedName, 1, 0);

		addRPage.add(new Label("Second Person"), 0, 1);
		displayTx.setText("The list of Second Person only contains people who have no relation with selected person.");
		ComboBox<String> secondPerson = new ComboBox<>();
		for(String name : admin.miniNet.keySet()){
			secondPerson.getItems().add(name);
		}
		secondPerson.getItems().remove(peopleList.getValue());
		for(Person person : admin.miniNet.get(peopleList.getValue()).getRelation().keySet()){
			secondPerson.getItems().remove(person.getName());
		}
		addRPage.add(secondPerson, 1, 1);
		
		addRPage.add(new Label("Relation: "), 0, 2);
		ComboBox<String> relationList = new ComboBox<>();
		relationList.getItems().add("friend");
		relationList.getItems().add("colleague");
		relationList.getItems().add("classmate");
		relationList.getItems().add("couple");
		addRPage.add(relationList, 1, 2);
		
		Button confirmAddRelation = new Button("Confirm");
		addRPage.add(confirmAddRelation, 1, 3);
		
		confirmAddRelation.setOnAction(cfmaddR ->{
			confirmAddRelation(relationList, peopleList, secondPerson);
		});
		operatePane.setCenter(addRPage);
	}


	
	private void confirmAddRelation(ComboBox<String> relationList, ComboBox<String> peopleList, 
			ComboBox<String> secondPerson) {
		try {
			Person first, second;
			first = admin.miniNet.get(peopleList.getValue());
			if(secondPerson.getValue() == null){
				throw new NoValidInputException("Please Choose a Person");
			}else second = admin.miniNet.get(secondPerson.getValue());
			if(relationList.getValue() == null){
				throw new NoValidInputException("Please Choose a Type");
			}else if((relationList.getValue() == "colleague")
					&& (!(first instanceof Adult) || (!(second instanceof Adult)))) {
				throw new NotToBeColleaguesException("At least one of them is not an Adult");
			}else if((relationList.getValue() == "classmate")
					&& (first instanceof YoungChild) || (second instanceof YoungChild)) {
				throw new NotToBeClassmatesException("At least one of them is a Young Child");
			}if(relationList.getValue() == "couple"
					&& (!(first instanceof Adult)|| !(second instanceof Adult))) {
				throw new NoAvailableException("At least one of them is not an Adult");
			}else if(relationList.getValue() == "couple"
					&& (first.getRelation().containsValue("couple") 
					|| second.getRelation().containsValue("couple"))) {
				throw new NotToBeCoupledException("At least one of them has got a couple");
			}else if(relationList.getValue() == "friend"
					&& ((first instanceof YoungChild) || (second instanceof YoungChild))) {
				throw new TooYoungException("Young child cannot have a friend");
			}else if(relationList.getValue() == "friend"
					&& ((first.getClass().getName() != second.getClass().getName()))) {
				throw new NotToBeFriendsException("Adults cannot be friends beyond adults");
			}else if(relationList.getValue() == "friend"
					&& ((first.getAge() - second.getAge()) > 3)) {
				throw new NotToBeFriendsException("Age difference between them is more than 3.");
			}else {
				first.addRelation(second, relationList.getValue());
				displayTx.setText(first.getName()+" becomes "+second.getName()+"'s "+relationList.getValue());
			}
			} catch (NotToBeColleaguesException e) {
			// TODO Auto-generated catch block
			displayTx.setText("Fail to add relationship for these two people");
		} catch (NotToBeClassmatesException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		} catch (NotToBeCoupledException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		} catch (NotToBeFriendsException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		} catch (NoAvailableException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		} catch (TooYoungException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		} catch (NoValidInputException e) {
			// TODO Auto-generated catch block
			displayTx.setText(e.getMessage());
		}
	}
	
	private void deleteRelationPageSet(ComboBox<String> peopleList) {
		GridPane addRPage = new GridPane();
		addRPage.setAlignment(Pos.CENTER);
		addRPage.setPadding(new Insets(15, 15, 15, 15));
		addRPage.setHgap(5.5);
		addRPage.setVgap(5.5);
		// Place nodes in the pane
		Label selectedName = new Label(peopleList.getValue());
		selectedName.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		addRPage.add(new Label("Seleted Person:"), 0, 0);
		addRPage.add(selectedName, 1, 0);
		addRPage.add(new Label("Second Person: "), 0, 1);
		displayTx.setText("The list of Second Person only contains people "
				+ "\nwho have relation with selected person.");
		ComboBox<String> secondPerson = new ComboBox<>();
		for(Person person : admin.miniNet.get(peopleList.getValue()).getRelation().keySet()){
			secondPerson.getItems().add(person.getName());
		}
		addRPage.add(secondPerson, 1, 1);
		addRPage.add(new Label("Relation: "), 0, 2);
		Text relation = new Text();
		addRPage.add(relation, 1, 2);
		secondPerson.setOnAction(sndPerson ->{
			relation.setText(null);
			Person first, second = null;
			first = admin.miniNet.get(peopleList.getValue());
			if(secondPerson.getValue() != null) second = admin.miniNet.get(secondPerson.getValue());
			//System.out.println(first.getName()+" "+second.getName());
			if(second!=null)relation.setText(first.getRelation().get(second));
		});
		Button deleteR = new Button("Confirm");
		addRPage.add(deleteR, 1, 3);
		
		deleteR.setOnAction(dltcfm ->{
			try {
				Person first, second;
				first = admin.miniNet.get(peopleList.getValue());
				if(secondPerson.getValue() != null)
				second = admin.miniNet.get(secondPerson.getValue());
				else throw new NoValidInputException("Please Select a Person.");
				first.getRelation().remove(second);
				secondPerson.getItems().remove(second.getName());
				second.getRelation().remove(first);
				displayTx.setText(first.getName()+" now has not relation with "+second.getName());
			} catch (NoValidInputException e) {
				// TODO Auto-generated catch block
				displayTx.setText(e.getMessage());
			}
		});
		operatePane.setCenter(addRPage);
	}
	
	private void confirmSelectOption(ComboBox<String> peopleList, RadioButton modifyProfile,
			RadioButton deletePerson, RadioButton addRelation, RadioButton deleteRelation,
			RadioButton listRelation) throws NoValidInputException {
		if(peopleList.getValue() != null) {
			if(modifyProfile.isSelected()) {
				//call the methode to set modifyProfile Page
				modifyProfilePageSet(peopleList);
			
			}else if(deletePerson.isSelected()) {
				Person deleted;
				deleted = admin.miniNet.get(peopleList.getValue());
				try {
					if(deleted.getRelation().containsValue("parent")) {
						throw new NoParentException(deleted.getName()+" cannot be deleted due to their children.");
					}		
					//remove the selected person from MiniNet
					//and remove all relation of this person from rest of MiniNet
					else {
						admin.deletePerson(deleted);
						displayTx.setText(peopleList.getValue()+" has been deleted from MiniNet."+
								"\n The relations with the rest in MiniNet have also been removed.");
						peopleList.getItems().remove(peopleList.getValue());
					}
					} catch (NoParentException e) {
					// TODO Auto-generated catch block
					displayTx.setText(e.getMessage());
					}
			}else if(addRelation.isSelected()) {
				addRelationPageSet(peopleList);
			}else if(deleteRelation.isSelected()) {
				deleteRelationPageSet(peopleList);
			}else if(listRelation.isSelected()) {
				displayTx.setText(admin.listRelation(admin.miniNet.get(peopleList.getValue())));
			}
			else {
				displayTx.setText("Please select an option.");
			}
		}else if(peopleList.getValue() == null){
			throw new NoValidInputException("Please select a person from the list.");
		}
	
	}

}

