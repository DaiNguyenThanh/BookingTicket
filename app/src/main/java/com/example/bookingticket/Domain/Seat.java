package com.example.bookingticket.Domain;

public class Seat {
    private String seatNumber;
    private boolean isOccupied;

    public Seat(String seatNumber, boolean isOccupied) {
        this.seatNumber = seatNumber;
        this.isOccupied = isOccupied;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber='" + seatNumber + '\'' +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
