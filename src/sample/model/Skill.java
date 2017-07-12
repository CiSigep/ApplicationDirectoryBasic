package sample.model;

public class Skill {
	
	private int id;
	private String name;
	private boolean required;
	
	public Skill(int id, String name, boolean required){
		this.id = id;
		this.name = name;
		this.required = required;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
	}
	
}
