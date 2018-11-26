
public class Menu 
{
	public Menu()
	{
		printMenu();
	}
	
	public void printMenu()
	{
		System.out.println("\n\n                                 ----- Menu -----\n\n"
				+ "1.  Make a reservation                       10. Update payment using reservation number\n"
				+ "2.  Change rooms                             11. List guests who owe money\n"
				+ "3.  Checkout                                 12. List reservations for room \n"
				+ "4.  Cancel reservation                       13. Get guest by room number & date\n"
				+ "5.  Update reservation                       14. List guests arriving on a specific date\n"
				+ "6.  List Empty rooms and their costs         15. List guests leaving on a specific date\n"
				+ "7.  List all the reservations for a guest    16. Register a new guest (** basic function **)\n"
				+ "8.  Delete room                              17. Register a new Admin (** basic function **)\\n\n"
				+ "9.  Pay for a room using reservation ID      18. \n\n"
				+ "* Enter \"exit\" to exit the system\n"
				);
	}
}
