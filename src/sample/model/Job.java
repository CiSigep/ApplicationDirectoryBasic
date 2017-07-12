package sample.model;

import java.util.ArrayList;
import java.util.List;

public class Job {
	
	private int id;
	private String jobTitle;
	private String experience;
	private List<Skill> skills;
	
	public Job(int id, String jobTitle, String experience)
	{
		this.id = id;
		this.jobTitle = jobTitle;
		this.experience = experience;
		skills = new ArrayList<Skill>();
	}

	public int getId() {
		return id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getExperience() {
		return experience;
	}

	public List<Skill> getSkills() {
		return skills;
	}
	
	public void addSkill(Skill skill) {
		skills.add(skill);
	}
	

}
