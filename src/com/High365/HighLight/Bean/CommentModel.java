package com.High365.HighLight.Bean;

import java.sql.Timestamp;

/**
 * 评论对象模型
 */
public class CommentModel {
    // Fields

    private Integer commentId;
    private String commentText;
    private Timestamp commentTime;
    private Integer circleId;
    private String userId;
    private String userPhoto;

    // Constructors

    /** default constructor */
    public CommentModel() {
    }

    /** full constructor */
    public CommentModel(String commentText, Timestamp commentTime, Integer circleId,
                   String userId, String userPhoto) {
        this.commentText = commentText;
        this.commentTime = commentTime;
        this.circleId = circleId;
        this.userId = userId;
        this.userPhoto = userPhoto;
    }

    // Property accessors

    public Integer getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getCommentTime() {
        return this.commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return this.userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
