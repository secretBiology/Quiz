package com.secretbiology.minimalquiz.activities.lobby;

import com.secretbiology.helpers.general.General;
import com.secretbiology.minimalquiz.background.OpenDBCategory;
import com.secretbiology.minimalquiz.common.Database;

public class GameSettings {
    private boolean isSinglePlayer;
    private boolean isOwner;
    private String ownerName;
    private String userName;
    private String createdOn;
    private int noOfQuestions;
    private int timePerQuestion;
    private int databaseType;
    private int categoryType;

    public GameSettings() {
        isSinglePlayer = true;
        isOwner = true;
        createdOn = General.timeStamp();
        noOfQuestions = 10;
        timePerQuestion = 15;
        databaseType = Database.OPENDB.getType();
        categoryType = OpenDBCategory.randomCategory().getCategory();
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        isSinglePlayer = singlePlayer;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    public int getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(int databaseType) {
        this.databaseType = databaseType;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }
}
