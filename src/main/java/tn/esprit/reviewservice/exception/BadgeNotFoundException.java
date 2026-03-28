package tn.esprit.reviewservice.exception;

public class BadgeNotFoundException extends RuntimeException {

    public BadgeNotFoundException(String message) {
        super(message);
    }
}