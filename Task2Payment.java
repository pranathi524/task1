import java.util.*; 
import java.text.SimpleDateFormat;
class Room { 
	private int roomNumber; 
	private String category; 
	private double price; 
	private boolean isAvailable;
	public Room(int roomNumber, String category, double price) 
	{
   		 this.roomNumber = roomNumber;
   		 this.category = category;
   		 this.price = price;
    		 this.isAvailable = true;
	}
	public int getRoomNumber() { return roomNumber; }
	public String getCategory() { return category; }
	public double getPrice() { return price; }
	public boolean isAvailable() { return isAvailable; }
	public void setAvailable(boolean available) { isAvailable = available; }
	@Override
	public String toString() {
    		return "Room " + roomNumber + " (" + category + ") - ₹" + price + "/night";
	}
 }
class Reservation { 
	private static int nextId = 1;
	 private int reservationId; 
	 private int roomNumber; 
	private String guestName; 
	private Date checkIn;
	 private Date checkOut; 
	private double totalPrice;
	 private boolean isPaid;
	public Reservation(int roomNumber, String guestName, Date checkIn, Date checkOut, double totalPrice) 
	{
    		this.reservationId = nextId++;
    		this.roomNumber = roomNumber;
    		this.guestName = guestName;
    		this.checkIn = checkIn;
    		this.checkOut = checkOut;
    		this.totalPrice = totalPrice;
    		this.isPaid = false;
	}
	public int getReservationId()
	{
    		return reservationId; 
	}
	public int getRoomNumber() { return roomNumber; }
	public String getGuestName() { return guestName; }
	public Date getCheckIn() { return checkIn; }
	public Date getCheckOut() { return checkOut; }
	public double getTotalPrice() { return totalPrice; }
	public boolean isPaid() { return isPaid; }
	public void setPaid(boolean paid) { isPaid = paid; }
	@Override
	public String toString() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		return "Reservation " + reservationId + " Room " + roomNumber + ", Guest: " + guestName + ", Check-in: " + sdf.format(checkIn) + 
           ", Check-out: " + sdf.format(checkOut) + ", Total: ₹ " + totalPrice + 
           ", Paid: " + (isPaid ? "Yes" : "No");
	}
}
class Hotel{ 
	private List rooms;
	 private List reservations;
	public Hotel() 
	{
   		rooms = new ArrayList<>();
    		reservations = new ArrayList<>();
    		initializeRooms();
	}
	private void initializeRooms() 
	{
    		rooms.add(new Room(101, "Standard", 2000));
    		rooms.add(new Room(102, "Standard", 2000));
    		rooms.add(new Room(201, "Deluxe", 3500));
    		rooms.add(new Room(202, "Deluxe", 3500));
    		rooms.add(new Room(301, "Suite", 5000));
	}
	public List<Room> searchAvailableRooms(Date checkIn, Date checkOut, String category) {
    	List<Room> availableRooms = new ArrayList<>();
    	for(Room room : rooms) {
        if (room.getCategory().equalsIgnoreCase(category) && isRoomAvailable(room, checkIn, checkOut)) {
            availableRooms.add(room);
        }
    	}
    		return availableRooms;
	}
	private boolean isRoomAvailable(Room room, Date checkIn, Date checkOut) {
    	for (Reservation reservation : reservations) {
        if (reservation.getRoomNumber() == room.getRoomNumber()) {
            if (!(checkOut.before(reservation.getCheckIn()) || checkIn.after(reservation.getCheckOut()))) {
                return false;
            	}
        	}
   	   }
   	 return true;
	}
	public Reservation makeReservation(int roomNumber, String guestName, Date checkIn, Date checkOut) {
    	Room room = findRoomByNumber(roomNumber);
   	 if (room != null && isRoomAvailable(room, checkIn, checkOut)) {
        long days = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        double totalPrice = room.getPrice() * days;
        Reservation reservation = new Reservation(roomNumber, guestName, checkIn, checkOut, totalPrice);
        reservations.add(reservation);
        return reservation;
    	}
    		return null;
	}
	private Room findRoomByNumber(int roomNumber) {
    	for (Room room : rooms) {
        if (room.getRoomNumber() == roomNumber) {
            return room;
        	}
  	  }
 	   return null;
	}
	public boolean processPayment(int reservationId) {
    	Reservation reservation = getReservationDetails(reservationId);
    	if (reservation != null && !reservation.isPaid()) {
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        if (paymentProcessor.processPayment(reservation.getTotalPrice())) {
            reservation.setPaid(true);
            return true;
        	}
   	 }
   	 return false;
	}
	public Reservation getReservationDetails(int reservationId) {
    	for (Reservation reservation : reservations) {
        if (reservation.getReservationId() == reservationId) {
            return reservation;
       		 }
     	}
    	return null;
	}
}
class PaymentProcessor {
 public boolean processPayment(double amount) { return Math.random() < 0.9; } 
}
public class Task2Payment { 
public static void main(String[] args) { 
	Hotel hotel = new Hotel(); 
	Scanner scanner = new Scanner(System.in); 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	while (true) {
        System.out.println("\n1. Search for rooms");
        System.out.println("2. Make a reservation");
        System.out.println("3. View reservation details");
        System.out.println("4. Process payment");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
	int choice = scanner.nextInt();
        scanner.nextLine(); 
	switch (choice) {
            case 1:
                try {
                    System.out.print("Enter check-in date (yyyy-MM-dd): ");
                    Date checkIn = sdf.parse(scanner.nextLine());
                    System.out.print("Enter check-out date (yyyy-MM-dd): ");
                    Date checkOut = sdf.parse(scanner.nextLine());
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String category = scanner.nextLine();

                    List<Room> availableRooms = hotel.searchAvailableRooms(checkIn, checkOut, category);
                    if (availableRooms.isEmpty()) {
                        System.out.println("No available rooms found.");
                    } else {
                        System.out.println("Available rooms:");
                        for (Room room : availableRooms) {
                            System.out.println(room);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again.");
                }
                break;

            case 2:
                try {
                    System.out.print("Enter room number: ");
                    int roomNumber = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Enter guest name: ");
                    String guestName = scanner.nextLine();
                    System.out.print("Enter check-in date (yyyy-MM-dd): ");
                    Date checkIn = sdf.parse(scanner.nextLine());
                    System.out.print("Enter check-out date (yyyy-MM-dd): ");
                    Date checkOut = sdf.parse(scanner.nextLine());

                    Reservation reservation = hotel.makeReservation(roomNumber, guestName, checkIn, checkOut);
                    if (reservation != null) {
                        System.out.println("Reservation created successfully:");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Failed to create reservation. Room might be unavailable.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again.");
                }
                break;

            case 3:
                System.out.print("Enter reservation ID: ");
                int reservationId = scanner.nextInt();
                Reservation reservation = hotel.getReservationDetails(reservationId);
                if (reservation != null) {
                    System.out.println(reservation);
                } else {
                    System.out.println("Reservation not found.");
                }
                break;

            case 4:
                System.out.print("Enter reservation ID for payment: ");
                reservationId = scanner.nextInt();
                boolean paymentSuccess = hotel.processPayment(reservationId);
                if (paymentSuccess) {
                    System.out.println("Payment processed successfully.");
                } else {
                    System.out.println("Payment processing failed.");
                }
                break;

            case 5:
                System.out.println("Thank you for using the Hotel Reservation System.");
                System.exit(0);

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
}