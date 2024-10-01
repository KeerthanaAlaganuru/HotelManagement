package HT_1;

import java.util.Scanner;
import java.util.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationService reservationService = new ReservationService();

        String boldStart = "\033[1m"; // ANSI escape code for bold start
        String boldEnd = "\033[0m"; // ANSI escape code for bold end

        System.out.println();
        System.out.println("==============================================================================");
        System.out.println(boldStart + "            WELCOME TO HOTEL RESERVATION SYSTEM         " + boldEnd);
        System.out.println("==============================================================================");
        
        System.out.println();
        System.out.println(boldStart + "Operations you can perform in this system." + boldEnd);
        System.out.println();

        while (true) {
            System.out.println();
            System.out.println("1. Book Room");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. View Reservation");
            System.out.println("4. Get Cost of Stay");
            System.out.println("5. Exit");
            System.out.println();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter room number: ");
                    int roomNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter room type (Single/Double): ");
                    String roomType = scanner.nextLine();
                    System.out.print("Enter guest name: ");
                    String guestName = scanner.nextLine();
                    System.out.print("Enter guest contact: ");
                    String guestContact = scanner.nextLine();
                    System.out.print("Enter number of days: ");
                    int numberOfDays = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    try {
                        reservationService.bookRoom(roomNumber, guestName, guestContact, roomType, new Date(), numberOfDays);
                        System.out.println(boldStart + "****** Room booked successfully! ******" + boldEnd);
                    } catch (SQLException e) {
                        System.err.println("Error booking room: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter room number to cancel reservation: ");
                    int cancelRoomNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    try {
                        reservationService.cancelReservation(cancelRoomNumber);
                        System.out.println(boldStart + "****** Reservation cancelled successfully! ******" + boldEnd);
                    } catch (SQLException e) {
                        System.err.println("Error cancelling reservation: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter guest name to view reservation: ");
                    String viewGuestName = scanner.nextLine();

                    try {
                        Reservation reservation = reservationService.viewReservationByGuestName(viewGuestName);
                        if (reservation != null) {
                            System.out.println(boldStart + "Reservation Details: " + boldEnd);
                            System.out.println("Room Number: " + reservation.getRoom().getRoomNumber());
                            System.out.println("Guest Name: " + reservation.getGuest().getName());
                            System.out.println("Guest Contact: " + reservation.getGuest().getContact());
                            System.out.println("Reservation Date: " + reservation.getReservationDate());
                            System.out.println("Cost: " + reservation.getCost());
                            System.out.println("Status: " + (reservation.isCancelled() ? "Cancelled" : "Active"));
                        } else {
                            System.out.println(boldStart + "******* No reservation found for the guest. ******" + boldEnd);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error viewing reservation: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter guest name to get cost of stay: ");
                    String guestNameForCost = scanner.nextLine();

                    try {
                        double cost = reservationService.getCostOfStay(guestNameForCost);
                        System.out.println(boldStart + "****** Cost of Stay: " + cost + " ******" + boldEnd);
                    } catch (SQLException e) {
                        System.err.println("Error getting cost of stay: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
            }
        }
    }
}
