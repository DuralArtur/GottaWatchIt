package com.example.banach.gottawatchit;

/**
 * Created by BANACH on 01.09.2016.
 */
public class Review {
    private String mAuthor;
    private String mReview;

    public Review(String author, String review){
        mAuthor=author;
        mReview=review;
    }

    public String getmAuthor(){return mAuthor;}
    public String getmReview(){return mReview;}
}
