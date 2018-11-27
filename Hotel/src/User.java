
public class User {
	int user_id;
	String first_name;
	String last_name;
	String email;
	boolean isAdmin;
	String password;
	
	public User(int u_id, String f_name, String l_name, String email, boolean isAdmin, String password) {
		this.user_id = u_id;
		this.first_name = f_name;
		this.last_name = l_name;
		this.email = email;
		this.isAdmin = isAdmin;
		this.password = password;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
