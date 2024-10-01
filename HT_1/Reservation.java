package HT_1;

import java.util.Date;

public class Reservation {
    private Room room;
    private Guest guest;
    private Date reservationDate;
    private double cost;
    private boolean cancelled;

    public Reservation(Room room, Guest guest, Date reservationDate, double cost, boolean cancelled) {
        this.room = room;
        this.guest = guest;
        this.reservationDate = reservationDate;
        this.cost = cost;
        this.cancelled = cancelled;
    }
    public Room getRoom() {
        return room;
    }

    public Guest getGuest() {
        return guest;
    }

    public double getCost() {
        return cost;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public boolean isActive() {
        return !cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
