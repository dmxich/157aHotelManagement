package Java;

import java.util.ArrayList;

/*
 * Class that hold info about 
 * guests
 */
public class Guest 
{
	private int guestID;
	private String firstName;
	private String lastName;
	private String email;
	private ArrayList<Reservation> reservations;
	
	/**
	 * Default constructor
	 * @param guestID
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public Guest(int guestID, String firstName, String lastName, String email)
	{
		this.guestID = guestID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		reservations = new ArrayList<Reservation>();
	}
	
	/**
	 * Setter of guestID private variable
	 * @param guestID
	 */
	public void setGuestID(int guestID)
	{
		this.guestID = guestID;
	}
	
	/**
	 * Setter for firstName private variable
	 * @param firstName
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	/**
	 * Setter for lastName private variable
	 * @param lastName
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	/**
	 * Setter for email private variable
	 * @param email
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Getter of guestID private variable 
	 * @return guestID
	 */
	public int getGuestID()
	{
		return guestID;
	}
	
	
	/**
	 * Getter of firstName private variable 
	 * @return firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * Getter of lastName private variable 
	 * @return lastName
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * Getter of email private variable 
	 * @return email
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Getter for reservation private variable
	 * @return
	 */
	public ArrayList<Reservation> getReservation() {
		return reservations;
	}
	
}
