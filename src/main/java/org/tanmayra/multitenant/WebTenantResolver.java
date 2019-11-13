package org.tanmayra.multitenant;

import java.util.Arrays;
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
    
    enum TenantResolverType {
        DNS, CONTEXT, THREAD;
    }

    static void setResolver(String value) {
        TenantResolverType resolverType = TenantResolverType.valueOf(value);
        extractor = TenantIdExtractorFactory.create(resolverType);
    }
    
    static Tenant resolve(final HttpServletRequest request) throws Exception {
		if (extractor == null) {
			throw new IllegalStateException("TenantIdExtractor not set");
		}
		Tenant target = lookup(extractor.extractId(request));
		if (target == null) {
			target = fallbackToContextBasedLookup(request);
		}
		return target;
	}

	private static Tenant fallbackToContextBasedLookup(final HttpServletRequest request) {
		return lookup(contextExtractor.extractId(request));
	}
    
    private static Tenant lookup(String clientIdentifierKey) {
    	if (clientIdentifierKey == null) {
			return null;
		}
    	return tenantMap.get(clientIdentifierKey);
    }

    static Set<String> registerTenants(TenantProperties properties) {
    	tenantMap.clear();
        final String tenantsValue = properties.getProperty("tenants");
        if(tenantsValue == null || tenantsValue.length() == 0) {
            logger.log(Level.WARNING, "No tenants configured in property file");
            return null;
        }
        tenants = tenantsValue.split(",");
        tenantProperties = properties;

        final Set<String> items = getTenantClientIdentifiers(tenants, 
        		tenantProperties);
        logger.log(Level.INFO,"Registered tenants " + items);
        return items;
    }

    private static Set<String> getTenantClientIdentifiers(String[] tenantKeys, 
    		final TenantProperties allProperties) {
        for (String tenantKey : tenantKeys) {
            String clientIdentifierKey = getClientIdentifierKey(allProperties, tenantKey);
            final String[] splitIDs = clientIdentifierKey.split(",");
			String[] array;
			for (int length2 = (array = splitIDs).length, j = 0; j < length2; ++j) {
				final String uniqueId = array[j];
				tenantMap.put(uniqueId, new Tenant(uniqueId, tenantKey, allProperties));
			}
        }
        return tenantMap.keySet();
    }

	private static String getClientIdentifierKey(TenantProperties allProperties, String tenantKey) {
		String clientIdentifierKey = allProperties.getPrefixedProperty(tenantKey, "ID");
		if(clientIdentifierKey == null) {
		    throw new IllegalArgumentException("Couldn't find the key [" + tenantKey +"." + "ID] in properties file");
		}
		return clientIdentifierKey;
	}
    
    public static void addIDonlyForSingleTenant(String tenantKey, String value) {
    	logger.log(Level.INFO, "Adding /travel for " + tenantKey);
    	boolean result = Arrays.stream(getTenants()).anyMatch(tenantKey::equals);
    	if(result == false) return;

    	String ID = getTenantProperties().getPrefixedProperty(tenantKey, "ID");
    	
    	if(ID == null) return;

    	logger.log(Level.FINE, "Clear /travel from all existing ID's value");
    	clearContextFromID();
    	
    	logger.log(Level.FINE, tenantKey + "'s Existing ID value " + ID);
    	ID = ID + "," + value;
    	logger.log(Level.FINE, tenantKey + "'s To Update ID value " + ID);
    	getTenantProperties().setPrefixedProperty(tenantKey, "ID", ID);
    	logger.log(Level.INFO, tenantKey + "'s Updated ID value " + getTenantProperties().getPrefixedProperty(tenantKey, "ID"));
    	
    	registerTenants(getTenantProperties());
    }

	private static void clearContextFromID() {
		for(String key : getTenants()) {
			String ID = getTenantProperties().getPrefixedProperty(key, "ID");
			if(ID.contains("/travel")) {
				if(ID.contains(",/travel")) {
					ID = ID.replace(",/travel", "");
				}else {
					ID = ID.replace("/travel", "");
				}
				logger.log(Level.INFO, "Clearing /travel from earlier " + key);
				getTenantProperties().setPrefixedProperty(key, "ID", ID);
			}
		}
	}

	public static String[] getTenants() {
		return tenants;
	}

	public static TenantProperties getTenantProperties() {
		return tenantProperties;
	}
    
    
}
