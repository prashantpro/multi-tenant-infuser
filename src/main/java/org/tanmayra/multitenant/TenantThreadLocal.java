package org.tanmayra.multitenant;

/**
 *
 * @author prashant
 */
public class TenantThreadLocal {

    public static final ThreadLocal<String> tenantThreadLocal = new ThreadLocal<String>();

    public static String getTenantId() {
        return tenantThreadLocal.get();
    }
    
    public static void setTenantId(String id) {
        tenantThreadLocal.set(id);
    }
}
