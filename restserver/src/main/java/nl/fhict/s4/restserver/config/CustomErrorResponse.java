package nl.fhict.s4.restserver.config;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class CustomErrorResponse {

    //yyyy/MM/dd HH:mm:ss
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String timestamp;

    private int status;

    private String error;

    public CustomErrorResponse(int status, String error) {
        this.status = status;
        this.error = error;                               //24 hours
        this.timestamp =  new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.ENGLISH).format(new Date());
    }

    //Getters MUST be present to return as a ResponseEntity
    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

}
