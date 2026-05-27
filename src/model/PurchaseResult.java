package model;

/**
 * Result status of a purchase request.
 */
public enum PurchaseResult {
    SUCCESS,
    NOT_FOUND,
    ALREADY_SOLD,
    OWN_PRODUCT,
    DB_ERROR
}
