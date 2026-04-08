package net.avallion.petTeleport;

// Custom exception for the case where a WorldGuard Flag fails to register.
public class FlagRegistrationException extends Exception {

    private static final long serialVersionUID = 1L;

    public FlagRegistrationException(String message) {

        super(message);

    }

    public FlagRegistrationException(String message, Throwable cause) {

        super(message, cause);

    }

}