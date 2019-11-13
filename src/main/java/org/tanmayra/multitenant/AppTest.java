package org.tanmayra.multitenant;

import java.io.InputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class AppTest
{
    private static final Map<String, Tenant> tenantMap;
    private static String[] tenants;
    private static TenantProperties tenantProperties;
    
    static {
        tenantMap = new ConcurrentHashMap<String, Tenant>();
    }
    
    public static void main(final String[] args) {
        final TenantProperties props = loadTenantProperties("/tenant.properties");
        final Set<String> tenants = registerTenants(props);
        System.out.println(tenants);
        Tenant tenant = AppTest.tenantMap.get("stage.clickmego.com");
        String appId = tenant.getProperty("APP_ID");
        System.out.println(appId);
        tenant = AppTest.tenantMap.get("/travel");
        appId = tenant.getProperty("APP_ID");
        System.out.println(appId);
    }
    
    private static TenantProperties loadTenantProperties(final String value) {
        final TenantProperties tenantProperties = new TenantProperties();
        final InputStream is = MultiTenantConfiguration.class.getResourceAsStream(value);
        try {
            tenantProperties.load(is);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if (is != null) {
            try {
                is.close();
            }
            catch (IOException ex2) {}
        }
        return tenantProperties;
    }
    
    static Set<String> registerTenants(final TenantProperties properties) {
        final String tenantsValue = properties.getProperty("tenants");
        if (tenantsValue == null || tenantsValue.length() == 0) {
            return null;
        }
        AppTest.tenants = tenantsValue.split(",");
        AppTest.tenantProperties = properties;
        final Set<String> items = getTenantClientIdentifiers(AppTest.tenants, AppTest.tenantProperties);
        return items;
    }
    
    private static Set<String> getTenantClientIdentifiers(final String[] tenantKeys, final TenantProperties allProperties) {
        for (final String tenantKey : tenantKeys) {
            final String clientIdentifierKey = allProperties.getPrefixedProperty(tenantKey, "ID");
            if (clientIdentifierKey == null) {
                throw new IllegalArgumentException("Couldn't find the key [" + tenantKey + "." + "ID] in properties file");
            }
            final String[] splitIDs = clientIdentifierKey.split(",");
            String[] array;
            for (int length2 = (array = splitIDs).length, j = 0; j < length2; ++j) {
                final String uniqueId = array[j];
                AppTest.tenantMap.put(uniqueId, new Tenant(uniqueId, tenantKey, allProperties));
            }
        }
        return AppTest.tenantMap.keySet();
    }
}