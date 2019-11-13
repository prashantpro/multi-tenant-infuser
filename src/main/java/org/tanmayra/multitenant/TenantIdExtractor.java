package org.tanmayra.multitenant;

import javax.servlet.http.HttpServletRequest;

public interface TenantIdExtractor {
    String extractId(final HttpServletRequest request);
}