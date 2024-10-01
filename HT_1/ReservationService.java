package HT_1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ReservationService {
    private RoomDAO roomDAO;

    public ReservationService() {
        this.roomDAO = new RoomDAO();
    }

    // Method to add a reservation to the database
    public void addReservation(Reservation reservation) throws SQLException {
        Connection connection = null;
        Statement statement = null;
    
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "INSERT INTO reservations (room_number, guest_name, guest_contact, reservation_date, cost, cancelled) " +
                           "VALUES (" + reservation.getRoom().getRoomNumber() + ", '" + reservation.getGuest().getName() + "', '" +
                           reservation.getGuest().getContact() + "', '" + new java.sql.Date(reservation.getReservationDate().getTime()) + 
                           "', " + reservation.getCost() + ", " + (reservation.isCancelled() ? 1 : 0) + ")";
            statement.executeUpdate(query);
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    // Method to retrieve a reservation from the database by room number
    public Reservation getReservation(int roomNumber) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM reservations WHERE room_number = " + roomNumber;
            resultSet = statement.executeQuery(query);
    
            if (resultSet.next()) {
                Room room = roomDAO.getRoom(roomNumber);
                Guest guest = new Guest(resultSet.getString("guest_name"), resultSet.getString("guest_contact"));
                Date reservationDate = resultSet.getDate("reservation_date");
                double cost = resultSet.getDouble("cost");
                boolean cancelled = resultSet.getBoolean("cancelled");
    
                return new Reservation(room, guest, reservationDate, cost, cancelled);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return null;
    }
    
    // Method to book a room
    public void bookRoom(int roomNumber, String guestName, String guestContact, String roomType, Date date, int numberOfDays) throws SQLException {
        Room room;
        if (roomType.equalsIgnoreCase("Single")) {
            room = new SingleRoom(roomNumber);
        } else if (roomType.equalsIgnoreCase("Double")) {
            room = new DoubleRoom(roomNumber);
        } else {
            throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
    
        // Check if the room already exists and is not booked
        Room existingRoom = roomDAO.getRoom(roomNumber);
        if (existingRoom != null && existingRoom.isBooked()) {
            throw new SQLException("Room is already booked.");
        }
    
        // Mark the room as booked
        room.setBooked(true);
    
        // If the room does not exist, add it to the database
        if (existingRoom == null) {
            roomDAO.addRoom(room, roomType);
        } else {
            // Update the room status to booked
            roomDAO.updateRoomStatus(room);
        }
    
        double totalCost = room.getRate() * numberOfDays;
    
        // Create a new reservation and add it to the database
        Reservation reservation = new Reservation(room, new Guest(guestName, guestContact), date, totalCost, false);
        addReservation(reservation);
    }
    

    // Method to cancel a reservation
    public void cancelReservation(int roomNumber) throws SQLException {
        Connection connection = null;
        Statement statement = null;
    
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "UPDATE reservations SET cancelled = TRUE WHERE room_number = " + roomNumber;
            statement.executeUpdate(query);
    
            Room room = roomDAO.getRoom(roomNumber);
            if (room != null) {
                room.setBooked(false);
                roomDAO.updateRoomStatus(room); // Update room status directly
            }
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

     
    // Method to retrieve a reservation from the database by guest name
    public Reservation getReservationByGuestName(String guestName) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM reservations WHERE guest_name = '" + guestName + "' AND cancelled = FALSE";
            resultSet = statement.executeQuery(query);
    
            if (resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                Room room = roomDAO.getRoom(roomNumber);
                Guest guest = new Guest(resultSet.getString("guest_name"), resultSet.getString("guest_contact"));
                Date reservationDate = resultSet.getDate("reservation_date");
                double cost = resultSet.getDouble("cost");
                boolean cancelled = resultSet.getBoolean("cancelled");
    
                return new Reservation(room, guest, reservationDate, cost, cancelled);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return null;
    }  
    

    // Method to get the cost of stay by guest name
    public double getCostOfStay(String guestName) throws SQLException {
        Reservation reservation = getReservationByGuestName(guestName);
        if (reservation != null && reservation.isActive()) {
            return reservation.getCost();
        } else {
            throw new SQLException("No active reservation found for the guest.");
        }
    }

    // Method to view a reservation by guest name
    public Reservation viewReservationByGuestName(String guestName) throws SQLException {
        Reservation reservation = getReservationByGuestName(guestName);
        if (reservation != null && !reservation.isCancelled()) {
            return reservation;
        } else {
            throw new SQLException("No active reservation found for the guest.");
        }
    }
    
}
