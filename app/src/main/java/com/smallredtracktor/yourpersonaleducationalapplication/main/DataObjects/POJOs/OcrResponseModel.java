package com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OcrResponseModel {

    @SerializedName("ParsedResults")
    @Expose
    private List<ParsedResult> parsedResults = null;
    @SerializedName("OCRExitCode")
    @Expose
    private String oCRExitCode;
    @SerializedName("IsErroredOnProcessing")
    @Expose
    private Boolean isErroredOnProcessing;
    @SerializedName("ErrorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("ErrorDetails")
    @Expose
    private Object errorDetails;
    @SerializedName("SearchablePDFURL")
    @Expose
    private String searchablePDFURL;
    @SerializedName("ProcessingTimeInMilliseconds")
    @Expose
    private String processingTimeInMilliseconds;

    public List<ParsedResult> getParsedResults() {
        return parsedResults;
    }

    public void setParsedResults(List<ParsedResult> parsedResults) {
        this.parsedResults = parsedResults;
    }

    public String getOCRExitCode() {
        return oCRExitCode;
    }

    public void setOCRExitCode(String oCRExitCode) {
        this.oCRExitCode = oCRExitCode;
    }

    public Boolean getIsErroredOnProcessing() {
        return isErroredOnProcessing;
    }

    public void setIsErroredOnProcessing(Boolean isErroredOnProcessing) {
        this.isErroredOnProcessing = isErroredOnProcessing;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(Object errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getSearchablePDFURL() {
        return searchablePDFURL;
    }

    public void setSearchablePDFURL(String searchablePDFURL) {
        this.searchablePDFURL = searchablePDFURL;
    }

    public String getProcessingTimeInMilliseconds() {
        return processingTimeInMilliseconds;
    }

    public void setProcessingTimeInMilliseconds(String processingTimeInMilliseconds) {
        this.processingTimeInMilliseconds = processingTimeInMilliseconds;
    }

    public String getText()
    {
        return this.getParsedResults().get(0).getParsedText().toString();
    }
}


