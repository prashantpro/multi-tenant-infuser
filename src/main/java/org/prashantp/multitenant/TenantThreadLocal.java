package org.prashantp.multitenant;

/**
 *
 * @author prashant
 */
class TenantThreadLocal {

    public static final ThreadLocal<String> tenantThreadLocal = new ThreadLocal<String>();

    public static String getTenantId() {
        return tenantThreadLocal.get();
    }
    
    public static void setTenantId(String id) {
        tenantThreadLocal.set(id);
    }
}
