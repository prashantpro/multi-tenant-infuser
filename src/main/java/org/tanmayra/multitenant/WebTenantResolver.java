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
    
    private static TenantResolver resolverType = TenantResolver.CONTEXT;

    enum TenantResolver {
        DNS, CONTEXT, THREAD;
    }

    public static void setResolverStrategy(TenantResolver resolver) {
        resolverType = resolver;
    }

    static Tenant resolve(HttpServletRequest request) throws Exception {
        switch (resolverType) {
            case CONTEXT: {
                logger.log(Level.FINE,"CONTEXT tenant - {}", request.getContextPath());
                return resolve(request.getContextPath());
            }
            case DNS: {
//              String domain = new URL(request.getRequestURL().toString()).getHost(); 
//              String domain = request.getServerName();
                String domain = request.getHeader("X-forwarded-host");
                logger.log(Level.FINE,"DNS tenant - {}", domain);
                return resolve(domain);
            }
            case THREAD: {
                logger.log(Level.FINE,"ThreadLocal tenant - ");
                TenantThreadLocal.setTenantId("ooc");
                return resolve(TenantThreadLocal.getTenantId());
            }
        }
        return null;
    }

    private static Tenant resolve(String clientIdentifierKey) {
    	Tenant target = tenantMap.get(clientIdentifierKey);
    	return target != null ? target : resolveByContains(clientIdentifierKey);
    }

    private static Tenant resolveByContains(String clientIdentifierKey) {
    	logger.log(Level.FINE,"Check for contains tenant ID for [" + clientIdentifierKey  +"]");
		Set<String> tenantIDkeys = tenantMap.keySet();
		for(String IDkey : tenantIDkeys) {
			if(IDkey.contains(clientIdentifierKey)) return tenantMap.get(IDkey);
		}
		return null;
	}

	static Set<String> registerTenants(TenantProperties properties) {
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
            tenantMap.put(clientIdentifierKey, new Tenant(clientIdentifierKey, tenantKey, allProperties));
        }
        return tenantMap.keySet();
    }
}
