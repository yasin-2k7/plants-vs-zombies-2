package com.pvz2.models.network.messages;
public class AnswerSecurityQuestionRequest {
    public String username;
    public String answer;
    public AnswerSecurityQuestionRequest() {}
    public AnswerSecurityQuestionRequest(String username, String answer) { this.username = username; this.answer = answer; }
}
