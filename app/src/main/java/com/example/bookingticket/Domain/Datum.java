package com.example.bookingticket.Domain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("rated")
    @Expose
    private String rated;
    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("runtime")
    @Expose
    private String runtime;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("writer")
    @Expose
    private String writer;
    @SerializedName("actors")
    @Expose
    private String actors;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("awards")
    @Expose
    private String awards;
    @SerializedName("metascore")
    @Expose
    private String metascore;
    @SerializedName("imdb_rating")
    @Expose
    private String imdbRating;
    @SerializedName("imdb_votes")
    @Expose
    private String imdbVotes;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("genres")
    @Expose
    private List<String> genres;
    @SerializedName("images")
    @Expose
    private List<String> images;

    // Default constructor
    public Datum() {
        // Default constructor is needed for Gson to deserialize JSON
    }

    // Constructor with parameters
    public Datum(Integer id, String title, String poster, String year, String rated,
                 String released, String runtime, String director, String writer,
                 String actors, String plot, String country, String awards,
                 String metascore, String imdbRating, String imdbVotes,
                 String imdbId, String type, List<String> genres, List<String> images) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.country = country;
        this.awards = awards;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbId = imdbId;
        this.type = type;
        this.genres = genres;
        this.images = images;
    }


    public Integer getId() {
        return id;
    }
    public String getPlot(){return plot;}


    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
