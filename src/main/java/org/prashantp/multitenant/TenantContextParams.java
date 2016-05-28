package org.prashantp.multitenant;

/**
 * Represents the context param name constants used in web.xml
 * <code>
 *    <context-param>
 *       <param-name>org.prashantp.multi-tenant-infuser.TENANT_RESOLVER</param-name>
 *        <param-value>CONTEXT</param-value>
 *    </context-param>
 *    
 *    <context-param>
 *        <param-name>org.prashantp.multi-tenant-infuser.TENANT_PROPERTIES</param-name>
 *        <param-value>/tenant.properties</param-value>
 *    </context-param>
 * </code>
 *    
 * @author prashant
 */
class TenantContextParams {
    public static final String TENANT_RESOLVER = "org.prashantp.multi-tenant-infuser.TENANT_RESOLVER";
    public static final String TENANT_PROPERTIES = "org.prashantp.multi-tenant-infuser.TENANT_PROPERTIES";
    public static final String TENANT_PROPERTIES_FILE = "tenant.properties";
}
