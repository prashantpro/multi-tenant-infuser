package org.tanmayra.multitenant.extractor;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.tanmayra.multitenant.TenantIdExtractor;

public class DNSIdExtractor implements TenantIdExtractor {
    private static final Logger logger = LoggerFactory.getLogger(DNSIdExtractor.class.getName());

    @Override
    public String extractId(final HttpServletRequest request) {
        final String domain = request.getHeader("X-forwarded-host");
        logger.debug("DNS tenant - {}", domain);
        return domain;
    }
}
