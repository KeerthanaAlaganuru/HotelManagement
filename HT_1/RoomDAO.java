// class for interacting with a database that stores information about rooms. It includes methods to add a room to the database, retrieve a room by room number, and update a room's booking status.
package HT_1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomDAO {
    // Method to add a room to the database
    public void addRoom(Room room, String roomType) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "INSERT INTO rooms (room_number, room_type, is_booked) VALUES (" 
                            + room.getRoomNumber() + ", '" + roomType + "', " 
                            + (room.isBooked() ? 1 : 0) + ")";
            statement.executeUpdate(query);
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    // Method to retrieve a room from the database by room number
    public Room getRoom(int roomNumber) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM rooms WHERE room_number = " + roomNumber;
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String roomType = resultSet.getString("room_type");
                boolean isBooked = resultSet.getBoolean("is_booked");
                Room room = roomType.equals("Single") ? new SingleRoom(roomNumber) : new DoubleRoom(roomNumber);
                room.setBooked(isBooked);
                return room;
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return null;
    }

    // Method to update room status in the database
    public void updateRoomStatus(Room room) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String query = "UPDATE rooms SET is_booked = " + (room.isBooked() ? 1 : 0) + " WHERE room_number = " + room.getRoomNumber();
            statement.executeUpdate(query);
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}
