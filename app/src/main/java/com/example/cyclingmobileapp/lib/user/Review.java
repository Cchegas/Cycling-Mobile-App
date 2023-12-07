package com.example.cyclingmobileapp.lib.user;

public class Review {

    public static final String COLLECTION_NAME = "Reviews";

    private String reviewer;
    private String reviewee;
    private  int rating;
    private String comment;

    public Review(){

    }

    public Review(String reviewer, String reviewee, int rating, String comment){
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewer   (){
        return reviewer;
    }
    public String getReviewee(){
        return reviewee;
    }
    public int getRating(){
        return rating;
    }
    public String getComment(){
        return comment;
    }

    public boolean setReviewer(String reviewer){
        this.reviewer = reviewer;
        return true;
    }
    public boolean setReviewee(String reviewee){
        this.reviewee = reviewee;
        return true;
    }
    public boolean setRating(int rating){
        this.rating = rating;
        return true;
    }
    public boolean setComment(String comment){
        this.comment = comment;
        return true;
    }
}
