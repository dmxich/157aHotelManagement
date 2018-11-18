package Java;
/*
 * Class Room that hold info
 * about Room
 */
public class Room 
{
	private int roomID;
	private String roomType;
	private Boolean hasTV = true;
	private Boolean hasTub = false;
	private double rate;
	
	
	/**
	 * Default Constructor for Room class
	 * @param roomID
	 * @param roomType
	 * @param hasTV
	 * @param hasTub
	 */
	public Room (int roomID, String roomType, Boolean hasTV, Boolean hasTub, double rate)
	{
		this.roomID = roomID;
		this.roomType = roomType;
		this.hasTub = hasTub;
		this.hasTV = hasTV;
		this.rate = rate;
	}
	
	/**
	 * Getter for rate private variable
	 * @return rate
	 */
	public double getRate()
	{
		return rate;
	}
	
	/**
	 * Getter for roomID private variable
	 * @return roomID
	 */
	public int getRoomId()
	{
		return roomID;
	}
	
	
	/**
	 * Getter for roomType private variable
	 * @return roomType
	 */
	public String getRoomType()
	{
		return roomType;
	}
	
	/**
	 * Getter for hasTV private variable
	 * @return hasTV
	 */
	public Boolean getHasTV()
	{
		return hasTV;
	}
	
	/**
	 * Getter for hasTub private variable
	 * @return hasTub
	 */
	public Boolean getHasTub()
	{
		return hasTub;
	}
	
	/**
	 * Setter for roomType private variable
	 * @param roomType
	 */
	public void setRoomType(String roomType)
	{
		this.roomType = roomType;
	}
	
	/**
	 * Setter for roomID private variable
	 * @param roomID
	 */
	public void setRoomID(int roomID)
	{
		this.roomID = roomID;
	}
	
	/**
	 * Setter for rate private variable
	 * @param rate
	 */
	public void setRate(double rate)
	{
		this.rate = rate;
	}
	
	
	/**
	 * Setter for hasTV private variable
	 * @param hasTV
	 */
	public void setHasTV(Boolean hasTV)
	{
		this.hasTV = hasTV;
	}
	
	/**
	 * Setter for hasTub private variable
	 * @param hasTub
	 */
	public void setHasTub(Boolean hasTub)
	{
		this.hasTub = hasTub;
	}
	
	public String toString() {
		return roomType + "\n" + "$" + rate + "\night";
	}
	
	
}



