package com.lilyhien.exception;

//generic Runtime exception for any 404 error (Restaurant not found, Food not found).
public class ResourceNotFoundException extends RuntimeException {

    // super(message) sends that string to the RuntimeException constructor.
    // The Chain: RuntimeException then passes it to Exception, which passes it to Throwable.
    // Storage: The Throwable class (the ultimate parent of all errors) finally stores that string in a private field called detailMessage.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
