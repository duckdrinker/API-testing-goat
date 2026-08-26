package com.xygeni.apisec.api10unsafeconsumption;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * API10:2023 - Unsafe Consumption of APIs fixture.
 *
 * This endpoint is authenticated and does NOT accept a URL-shaped parameter
 * (deliberately using "city", not "url"/"callback_url"/etc.) so it does not
 * match the ssrf detector's heuristic - the point here is different from
 * API7/SSRF.
 *
 * The upstream base URL is fixed/hardcoded. The API10 risk is that this
 * service blindly trusts and forwards whatever the third-party weather
 * provider returns, with no response validation, no schema check, no
 * redirect-following limits, and no TLS-certificate pinning for the
 * upstream call. None of this is a source-code *shape* pattern any of
 * Xygeni's 12 API Security detectors look for (they analyse this
 * project's own endpoints, not how it consumes someone else's API), so
 * no finding is expected here today.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherProxyController {

    private static final String UPSTREAM_BASE_URL = "https://third-party-weather-provider.example/v1/weather";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String getWeather(@RequestParam String city) {
        String upstreamUrl = UPSTREAM_BASE_URL + "?city=" + city;
        // Response is forwarded to the client as-is: no schema validation,
        // no sanitisation, no limits on redirects or response size.
        return restTemplate.getForObject(upstreamUrl, String.class);
    }
}
