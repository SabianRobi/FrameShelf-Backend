package com.sabianrobi.frameshelf.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    public int status;
    public String code;
    public String message;
    public String trace;
}
