package org.tanmayra.multitenant;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author prashant
 */
public class WebTenantResolver {

    private static final Logger logger = Logger.getLogger(WebTenantResolver.class.getName());

    public static final String CURRENT_TENANT = "CURRENT_TENANT";

    private static final Map<String, Tenant> tenantMap = new ConcurrentHashMap<String, Tenant>();
    private static String[] tenants;
    private static TenantProperties tenantProperties;
    private static TenantIdExtractor extractor;
    private static TenantIdExtractor contextExtractor = TenantIdExtractorFactory.create(TenantResolverType.CONTEXT);

    public static void setResolver(String value) {
        final TenantResolverType resolverType = TenantResolverType.valueOf(value);
        WebTenantResolver.extractor = TenantIdExtractorFactory.create(resolverType);
    }

    static Tenant resolve(final HttpServletRequest request) throws Exception {
        if (WebTenantResolver.extractor == null) {
            throw new IllegalStateException("TenantIdExtractor not set");
        }
        Tenant target = lookup(WebTenantResolver.extractor.extractId(request));
        if (target == null) {
            target = fallbackToContextBasedLookup(request);
        }
        return target;
    }

    private static Tenant fallbackToContextBasedLookup(final HttpServletRequest request) {
        return lookup(WebTenantResolver.contextExtractor.extractId(request));
    }
    
    private static Tenant lookup(final String clientIdentifierKey) {
        if (clientIdentifierKey == null) {
            return null;
        }
        return WebTenantResolver.tenantMap.get(clientIdentifierKey);
    }

    static Set<String> registerTenants(final TenantProperties properties) {
        String tenantsValue = properties.getProperty("tenants");
        if(tenantsValue == null || tenantsValue.length() == 0) {
            logger.log(Level.WARNING, "No tenants configured in property file");
            return null;
        }
        tenants = tenantsValue.split(",");
        tenantProperties = properties;

        Set<String> items = getTenantClientIdentifiers(tenants, tenantProperties);
        logger.log(Level.INFO,"Registered tenants " + items);
        return items;
    }

    private static Set<String> getTenantClientIdentifiers(String[] tenantKeys, TenantProperties allProperties) {
        for (String tenantKey : tenantKeys) {
            String clientIdentifierKey = allProperties.getPrefixedProperty(tenantKey, "ID");
            if(clientIdentifierKey == null) {
                throw new IllegalArgumentException("Couldn't find the key [" + tenantKey +"." + "ID] in properties file");
            }
            final String[] splitIDs = clientIdentifierKey.split(",");
            String[] array;
            for (int length2 = (array = splitIDs).length, j = 0; j < length2; ++j) {
                final String uniqueId = array[j];
                WebTenantResolver.tenantMap.put(uniqueId, new Tenant(uniqueId, tenantKey, allProperties));
            }
        }
        return tenantMap.keySet();
    }

    enum TenantResolverType {
        DNS("DNS", 0), 
        CONTEXT("CONTEXT", 1), 
        THREAD("THREAD", 2);
        
        private TenantResolverType(final String name, final int ordinal) {
        }
    }
}
