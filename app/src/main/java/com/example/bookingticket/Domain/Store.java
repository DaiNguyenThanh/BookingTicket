package com.example.bookingticket.Domain;

public class Store {
    private String title;
    private String address;
    private String phone;
    private String imageUrl;

    public Store() {
        // Default constructor required for Firestore
    }

    public Store(String title, String address, String phone, String imageUrl) {
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Add setters if needed

    @Override
    public String toString() {
        return "Store{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
