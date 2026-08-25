package com.xygeni.apisec.authconfidence;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Diagnostic fixture for unauthenticated_endpoint's apparent confidence-scoring
 * behaviour (see ASEC-043 note): no endpoint here has any auth annotation, and
 * there is no SecurityConfig in this project at all - the only variable across
 * endpoints is which of the 3 signals observed in the classifier's
 * "authRequiredReasons" are present:
 *   (P) path matches a resource convention (/admin /users /payments ...)
 *   (R) response DTO carries a sensitivity-tagged field
 *   (I) request accepts a sensitivity-tagged input parameter
 *
 * Combined with the two known data points already confirmed elsewhere
 * (0 signals -> UNDETERMINED/suppressed in API2-broken-authentication's
 * AccountController; all 3 signals -> AUTH_REQUIRED/confirmed in
 * API3-broken-object-property-level-authorization's UserProfileController),
 * these 6 endpoints cover the remaining combinations of the 2^3 matrix.
 */
@RestController
public class WidgetController {

    /** P only. */
    @GetMapping("/api/users/summary")
    public WidgetSummary usersSummary() {
        return new WidgetSummary("w1", "Widget One");
    }

    /** R only. */
    @GetMapping("/api/widgets/secret")
    public WidgetSecret widgetsSecret() {
        return new WidgetSecret("w1", "123-45-6789");
    }

    /** I only. */
    @GetMapping("/api/widgets/lookup")
    public WidgetSummary widgetsLookup(@RequestParam String ssn) {
        return new WidgetSummary("w1", "Widget One");
    }

    /** P + R. */
    @GetMapping("/api/users/secret")
    public WidgetSecret usersSecret() {
        return new WidgetSecret("w1", "123-45-6789");
    }

    /** P + I. */
    @GetMapping("/api/users/lookup")
    public WidgetSummary usersLookup(@RequestParam String ssn) {
        return new WidgetSummary("w1", "Widget One");
    }

    /** R + I. */
    @GetMapping("/api/widgets/full")
    public WidgetSecret widgetsFull(@RequestParam String ssn) {
        return new WidgetSecret("w1", "123-45-6789");
    }
}
