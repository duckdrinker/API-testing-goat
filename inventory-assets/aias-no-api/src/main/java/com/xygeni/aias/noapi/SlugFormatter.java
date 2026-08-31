package com.xygeni.aias.noapi;

import org.apache.commons.lang3.StringUtils;

// Plain library code — no REST/HTTP framework of any kind, no annotations
// that any of the ApiDiscoveryEngine framework detectors would recognize.
public final class SlugFormatter {

    private SlugFormatter() {
    }

    public static String toSlug(String input) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        return StringUtils.lowerCase(input).trim().replaceAll("\\s+", "-");
    }
}
