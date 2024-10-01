package HT_1;

public abstract class Room {
    private int roomNumber;
    private boolean isBooked;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isBooked = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public abstract double getRate();
}

class SingleRoom extends Room {
    public SingleRoom(int roomNumber) {
        super(roomNumber);
    }

    @Override
    public double getRate() {
        return 1000.0;
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber) {
        super(roomNumber);
    }

    @Override
    public double getRate() {
        return 2500.0;
    }
}
