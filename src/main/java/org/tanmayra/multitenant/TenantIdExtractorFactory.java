package org.tanmayra.multitenant;

import org.tanmayra.multitenant.extractor.ThreadLocalIdExtractor;
import org.tanmayra.multitenant.extractor.ContextIdExtractor;
import org.tanmayra.multitenant.extractor.DNSIdExtractor;

class TenantIdExtractorFactory {
    public static TenantIdExtractor create(final WebTenantResolver.TenantResolverType resolverType) {
        switch (resolverType) {
            case DNS: {
                return new DNSIdExtractor();
            }
            case CONTEXT: {
                return new ContextIdExtractor();
            }
            case THREAD: {
                return new ThreadLocalIdExtractor();
            }
            default: {
                return null;
            }
        }
    }
}