package Tools;

public class Account {
	private int id;
	private String head;
	private String name;
	private String messgae;
	private String title;
	private String photo;
	

	public Account(int id,String name2, String head2, String title2, String mess, String photo2) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.name=name2;
		this.title=title2;
		this.messgae=mess;
		this.photo=photo2;
		this.head=head2;
	}
	public int getId() {
		return id;
	}
	public void setId(int i) {
		this.id = i;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getMessgae() {
		return messgae;
	}
	public void setMessgae(String messgae) {
		this.messgae = messgae;
	}

	public void setHead(String head) {
		this.head = head;
	}
	public String getHead() {
		// TODO Auto-generated method stub
		return head;
	}
	
	
}
