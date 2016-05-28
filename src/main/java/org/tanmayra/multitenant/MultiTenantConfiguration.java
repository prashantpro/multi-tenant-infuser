package org.tanmayra.multitenant;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.tanmayra.multitenant.WebTenantResolver.TenantResolver;

/**
 *
 * @author prashant
 */
class MultiTenantConfiguration {

    private static final Logger LOGGER = Logger.getLogger(MultiTenantConfiguration.class.getName());

    private MultiTenantConfiguration() {}
    
    static void setup(FacesContext context) {
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        initTenantProperties(servletContext);
    }

    static void setup(ServletContext servletContext) {
        initTenantProperties(servletContext);
    }

    private static void initTenantProperties(ServletContext context) {
        String value = null;
        value = context.getInitParameter(TenantContextParams.TENANT_RESOLVER);
        value = (value != null) ? value.toUpperCase() : TenantResolver.THREAD.toString();

        TenantResolver resolverStrategy = WebTenantResolver.TenantResolver.valueOf(value);
        WebTenantResolver.setResolverStrategy(resolverStrategy);

        value = context.getInitParameter(TenantContextParams.TENANT_PROPERTIES);
        value = (value != null) ? value : ("/" + TenantContextParams.TENANT_PROPERTIES_FILE);
        
        TenantProperties props = loadTenantProperties(value);
        WebTenantResolver.registerTenants(props);        
    }

    private static TenantProperties loadTenantProperties(String value) {
        TenantProperties tenantProperties = new TenantProperties();
        InputStream is = MultiTenantConfiguration.class.getResourceAsStream(value);
        try {
            tenantProperties.load(is);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Couldn't load property resource " + value, ex);
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return tenantProperties;
    }

}
