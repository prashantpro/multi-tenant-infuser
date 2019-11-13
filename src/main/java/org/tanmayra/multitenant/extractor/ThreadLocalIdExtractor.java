package org.tanmayra.multitenant.extractor;

import org.tanmayra.multitenant.TenantThreadLocal;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;
import org.tanmayra.multitenant.TenantIdExtractor;

public class ThreadLocalIdExtractor implements TenantIdExtractor {
    private static final Logger logger = Logger.getLogger(ThreadLocalIdExtractor.class.getName());

    @Override
    public String extractId(final HttpServletRequest request) {
        logger.log(Level.FINE, "ThreadLocal tenant - ");
        TenantThreadLocal.setTenantId("ooc");
        return TenantThreadLocal.getTenantId();
    }
}
