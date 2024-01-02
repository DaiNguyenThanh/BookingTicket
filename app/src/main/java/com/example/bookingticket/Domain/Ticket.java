package com.example.bookingticket.Domain;

import java.util.Date;
import java.util.List;

public class Ticket {
    private String ticketId;
    private String userId;
    private String filmId;
    private String film;
    private String address;
//    private Date date;
    private String time;
    private String image;
    private String cinema;
    private List<Integer> seatArray;
    private Integer price;

    // No-argument constructor required by Firebase Firestore
    public Ticket() {
        // Default constructor is needed for Firebase Firestore to deserialize objects
    }

    // Constructor with all fields
    public Ticket(String ticketId, String userId, String filmId, String film, String address, Date date, String time, String image, String cinema, List<Integer> seatId, Integer price) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.filmId = filmId;
        this.film = film;
        this.address = address;
//        this.date = date;
        this.time = time;
        this.image = image;
        this.cinema = cinema;
        this.seatArray = seatId;
        this.price = price;
    }

    // Getters and setters for all fields

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return film;
    }

    public void setTitle(String title) {
        this.film = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public List<Integer> getSeatId() {
        return seatArray;
    }

    public void setSeatId(List<Integer>seatId) {
        this.seatArray = seatId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
