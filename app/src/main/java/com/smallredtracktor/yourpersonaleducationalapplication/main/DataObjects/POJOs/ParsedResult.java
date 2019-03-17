package com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParsedResult {

    @SerializedName("TextOverlay")
    @Expose
    private Object textOverlay;
    @SerializedName("FileParseExitCode")
    @Expose
    private Integer fileParseExitCode;
    @SerializedName("ParsedText")
    @Expose
    private Object parsedText;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("ErrorDetails")
    @Expose
    private String errorDetails;

    public Object getTextOverlay() {
        return textOverlay;
    }

    public void setTextOverlay(Object textOverlay) {
        this.textOverlay = textOverlay;
    }

    public Integer getFileParseExitCode() {
        return fileParseExitCode;
    }

    public void setFileParseExitCode(Integer fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    public Object getParsedText() {
        return parsedText;
    }

    public void setParsedText(Object parsedText) {
        this.parsedText = parsedText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

}
