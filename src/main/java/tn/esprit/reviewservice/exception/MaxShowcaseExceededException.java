package tn.esprit.reviewservice.exception;

public class MaxShowcaseExceededException extends RuntimeException {

    public MaxShowcaseExceededException(String message) {
        super(message);
    }
}