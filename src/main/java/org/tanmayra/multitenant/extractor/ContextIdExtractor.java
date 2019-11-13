package org.tanmayra.multitenant.extractor;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.tanmayra.multitenant.TenantIdExtractor;

public class ContextIdExtractor implements TenantIdExtractor {
    private static final Logger logger = LoggerFactory.getLogger(ContextIdExtractor.class.getName());

    @Override
    public String extractId(final HttpServletRequest request) {
        logger.debug("CONTEXT tenant - {}", request.getContextPath());
        return request.getContextPath();
    }
}
