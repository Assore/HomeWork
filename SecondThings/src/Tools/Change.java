package Tools;

public class Change {
	private int id,readcode;
	
	private String name,message;

	public Change(int id2, String name2, String mess, int readcode2) {
		// TODO Auto-generated constructor stub
		this.id=id2;
		this.name=name2;
		this.message=mess;
		this.readcode=readcode2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReadcode() {
		return readcode;
	}

	public void setReadcode(int readcode) {
		this.readcode = readcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
