package com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects;

public class Answer {

    private int questionId;
    private int type;
    private String value;
    private int userStackNumber;

    public int getUserStackNumber() {
        return userStackNumber;
    }

    public void setUserStackNumber(int userStackNumber) {
        this.userStackNumber = userStackNumber;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
