package com.kor.switter.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {
    private boolean success;
    //private String challenge_ts; //: timestamp,  timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
    //private String hostname; //": string,         the hostname of the site where the reCAPTCHA was solved
    @JsonAlias("error-codes")
    private Set<String> errorCodes; //": [...]        // optional

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
