package sample.model;

public class JobApplication {
	
	private int id;
	private Job job;
	private UserCredentials user;
	private int status;
	
	public JobApplication(int id, UserCredentials user, Job job, int status) {
		this.id = id;
		this.user = user;
		this.job = job;
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	public UserCredentials getUser()
	{
		return user;
	}
	

	public Job getJob() {
		return job;
	}

	public int getStatus() {
		return status;
	}
	
	

}
