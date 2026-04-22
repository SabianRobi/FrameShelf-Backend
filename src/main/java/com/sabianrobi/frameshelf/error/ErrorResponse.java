package com.sabianrobi.frameshelf.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    public int status;
    public String code;
    public String message;
}
