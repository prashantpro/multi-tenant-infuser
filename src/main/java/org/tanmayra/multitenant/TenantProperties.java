package org.tanmayra.multitenant;

import java.util.Properties;

/**
 *
 * @author prashant
 */
class TenantProperties extends Properties {

    private static final String DELIMITER = ".";

    public String getPrefixedProperty(String tenantPrefix, String key) {
        return getProperty(tenantPrefix + DELIMITER + key);
    }
    
    public void setPrefixedProperty(String tenantPrefix, String key, String value) {
        setProperty(tenantPrefix + DELIMITER + key, value);
    }
}
