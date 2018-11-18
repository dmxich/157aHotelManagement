package Java;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
	
	private int reservationID;
	private Date arriveDate;
	private Date departDate;
	private Date paymentDue;
	private String phoneNum;
	private double cost;
	private int numberOfDays;
	private boolean available;
	private Room roomID;
	private Guest guestID;
	private int paymentID;
	
	public Reservation(int reservationID, int numberOfDays, Room roomID, Date arriveDate, Date departDate, Date paymentDue,
			String phoneNum, double cost, boolean available, Guest guestID, int paymentId)	{
		this.reservationID = reservationID;
		this.roomID = roomID;
		this.arriveDate = arriveDate;
		this.departDate = departDate;
		this.phoneNum = phoneNum;
		this.cost = cost;
		this.available = available;
		this.guestID = guestID;
		this.paymentDue = paymentDue;
		this.numberOfDays = numberOfDays;
		this.paymentID = paymentId;
	}
	
	/**
	 * Getter for reservationID private variable
	 * @return reservationID
	 */
	public int getReservationID() {
		return reservationID;
	}
	
	/**
	 * Getter for paymentID private variable
	 * @return paymentID
	 */
	public int getPaymentID() {
		return paymentID;
	}
	
	
	/**
	 * Getter for numberOfDays private variable
	 * @return numbrOfDays
	 */
	public int getNumberOfDays() {
		return numberOfDays;
	}
	
	
	/**
	 * Getter for arriveDate private variable
	 * @return arriveDate
	 */
	public Date getArriveDate() {
		return arriveDate;
	}
	
	/**
	 * Getter for departDate private variable
	 * @return departDate
	 */
	public Date getDepartDate() {
		return departDate;
	}
	

	/**
	 * Getter for cost private variable
	 * @return cost
	 */
	public double getCost() {
		return cost;
	}
	
	/**
	 * Getter for available private variable
	 * @return available
	 */
	public boolean getAvailable() {
		return available;
	}
	
	/**
	 * Getter for roomID private variable
	 * @return roomID
	 */
	public Room getRoomID() {
		return roomID;
	}
	
	/**
	 * Getter for guestID private variable
	 * @return guestID
	 */
	public Guest getGuestID() {
		return guestID;
	}
	
	/**
	 * Getter for paymentDue private variable
	 * @return paymentDue
	 */
	public Date getPaymentDue()
	{
		return paymentDue;
	}
	
	/**
	 * Getter for phoneNum private variable
	 * @return phoneNum
	 */
	public String getPhoneNum()
	{
		return phoneNum;
	}
	
	
	/**
	 * Setter for phoneNum private variable
	 * @param phoneNum
	 */
	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}
	
	/**
	 * Setter for paymentDue private variable
	 * @param paymentDue
	 */
	public void setPaymentDue(Date paymentDue)
	{
		this.paymentDue = paymentDue;
	}
	
	
	/**
	 * Setter for paymentDue private variable
	 * @param paymentDue
	 */
	public void setNumberOfDays(int numberOfDays)
	{
		this.numberOfDays = numberOfDays;
	}
	
	/**
	 * Setter for reservationID private variable
	 * @param reservationId
	 */
	public void setReservationID(int reservationId) {
		 this.reservationID = reservationId;
	}
	
	/**
	 * Setter for arriveDate private variable
	 * @param arriveDate
	 */
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}
	
	/**
	 * Setter for departDate private variable
	 * @param departDate
	 */
	public void setDepartDate(Date departDate) {
		this.departDate = departDate;
	}
	

	/**
	 * Setter for cost private variable
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	/**
	 * Setter for available private variable
	 * @param available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * Setter for roomID private variable
	 * @param roomID
	 */
	public void setRoomID(Room roomId) {
		this.roomID =  roomId;
	}
	
	/**
	 * Setter for guestID private variable
	 * @param guestID
	 */
	public void setGuestID( Guest guestId) {
		this.guestID = guestId;
	}
	
	
	public String toString() {
		
		return String.format("%s \n  to %s \nTotal Cost: %f"
				, (String)new SimpleDateFormat("MM/dd/yyyy").format(arriveDate),
				  (String)new SimpleDateFormat("MM/dd/yyyy").format(departDate), 
				  numberOfDays * roomID.getRate());
				
	}
	

}
