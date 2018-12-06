
public class Menu 
{
	public Menu()
	{
	}
	
	public void printAdminMenu()
	{
		System.out.println("\n\n                                 ----- Menu -----\n\n"
				+ "1.  Make a reservation                       11. List guests who owe money\n"
				+ "2.  Change rooms                             12. List reservations for room\n"
				+ "3.  Checkout                                 13. Get guest by room number & date\n"
				+ "4.  Cancel reservation                       14. List guests arriving on a specific date\n"
				+ "5.  Update reservation                       15. List guests leaving on a specific date\n"
				+ "6.  List Empty rooms and their costs         16. Register a new guest (** basic function **)\n"
				+ "7.  List all the reservations for a guest    17. Register a new Admin (** basic function **)\n"
				+ "8.  Delete room                              18. Show the rooms which rate is higher than average rate\n"
				+ "9.  Pay for a room using reservation ID      19. Archive reservations\n"
				+ "10. Update payment using reservation number  20. List all coustomers who booked rooms  \n\n"
				+ "* Enter \"exit\" to exit the system\n"
				);
	}
	
	public void printGuestMenu()
	{
		System.out.println("\n\n        ----- Menu -----\n\n"
				+ "1.  Make a reservation\n"
				+ "2.  List My Reservations\n"
				+ "3.  Make Payment\n"
				+ "4.  Update Payment\n\n"
				+ "* Enter \"exit\" to exit the system\n"
				);
	}
	
}
