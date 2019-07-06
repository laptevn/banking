package com.laptevn.account.repository;

/**
 * Covers exceptions related to repository handling
 */
public class RepositoryException extends Exception {
    private static final long serialVersionUID = -1311613497977480795L;

    RepositoryException(String message) {
        super(message);
    }
}