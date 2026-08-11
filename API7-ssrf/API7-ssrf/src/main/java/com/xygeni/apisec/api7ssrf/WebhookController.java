package com.xygeni.apisec.api7ssrf;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * API7:2023 - Server-Side Request Forgery fixture.
 *
 * "url" is a required, URL-shaped parameter name, and the endpoint's path
 * ("/fetch") plus its behaviour (fetching the given URL server-side and
 * returning the response) matches the fetch/import/preview/scrape kind
 * that the SSRF detector treats as High severity when combined with a
 * required URL-shaped parameter.
 */
@RestController
@RequestMapping("/api")
public class WebhookController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/fetch")
    @PreAuthorize("isAuthenticated()")
    public String fetchUrl(@RequestParam String url) {
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * ASEC-106: URL-shaped parameter present, but Phase B finds no actual
     * fetch/HTTP-client call using it - per the real detector logic
     * (SsrfDetector.java: "Severity sev = pb.fetchFound ? Severity.high :
     * Severity.low;"), severity depends solely on whether a fetch call is
     * found, not on whether the parameter is required or optional. This is
     * the genuine lower-signal shape.
     */
    @GetMapping("/echo")
    @PreAuthorize("isAuthenticated()")
    public String echoUrl(@RequestParam String url) {
        return url;
    }
}
