package org.prashantp.multitenant;

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
}
