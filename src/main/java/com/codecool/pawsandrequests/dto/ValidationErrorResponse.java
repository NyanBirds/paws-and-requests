package com.codecool.pawsandrequests.dto;

import java.util.Map;

/**
 * Body returned with HTTP 400 when request validation fails.
 *
 * <p>Reuses the {@code code}/{@code message} envelope of {@link ErrorResponse}
 * and adds the per-field detail. It is a sibling record rather than a change to
 * {@code ErrorResponse}, so responses on the endpoints that do not validate a
 * body keep exactly the shape they have today.
 *
 * @param code    stable machine-readable code, {@code VALIDATION_FAILED}
 * @param message summary for logs and for clients that ignore the field map
 * @param fields  offending field name to its message, sorted by field name
 */
public record ValidationErrorResponse(
        String code,
        String message,
        Map<String, String> fields
) {
}
