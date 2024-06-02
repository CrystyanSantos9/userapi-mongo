package dev.cryss.mongo.userapi.controllers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InternalServerErrorException extends Exception{

    private static final long serialVersionUID = 1L;


    public InternalServerErrorException(String message){
        super(message);
    }

}
