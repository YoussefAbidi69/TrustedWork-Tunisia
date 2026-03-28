package tn.esprit.reviewservice.exception;

public class TrustScoreNotFoundException extends RuntimeException {

    public TrustScoreNotFoundException(String message) {
        super(message);
    }
}