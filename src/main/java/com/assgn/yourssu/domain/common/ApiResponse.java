package com.assgn.yourssu.domain.common;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"statusCode", "message", "content"})
public class ApiResponse<T> {

    @JsonProperty("statusCode")
    @NonNull
    private final String statusCode;

    @JsonProperty("message")
    @NonNull
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("content")
    private T content;

    public static <T> ApiResponse<T> onSuccess(T content) {
        return new ApiResponse<>(HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase(), content);
    }

    public static <T> ApiResponse<T> of(String message,T result){
        return new ApiResponse<>(HttpStatus.OK.name(), message, result);
    }

    public static <T> ApiResponse<T> onFailure(String statusCode, String message, T content) {
        return new ApiResponse<>(statusCode, message, content);
    }

    // Json serialize
    public String toJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}