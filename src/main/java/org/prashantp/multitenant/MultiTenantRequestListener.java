package org.prashantp.multitenant;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author prashant
 */
@WebListener()
class MultiTenantRequestListener implements ServletRequestListener {
    private static final Logger logger = java.util.logging.Logger.getLogger(MultiTenantRequestListener.class.getName());
    
    public void requestDestroyed(ServletRequestEvent event) {
        logger.log(Level.FINE,"Destorying MultiTenantRequestListener - request");
        final HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        
        request.removeAttribute(WebTenantResolver.CURRENT_TENANT);
    }

    public void requestInitialized(ServletRequestEvent event) {
        logger.log(Level.FINE, "Initiating MultiTenantRequestListener - request");
        final HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        final String path = request.getServletPath();
        logger.log(Level.FINE, "Request URL Path is {}", path);
        
        loadTenant(request);
    }

    private void loadTenant(HttpServletRequest request) {
        try {

            Tenant resolvedTenant = WebTenantResolver.resolve(request);
            
            request.setAttribute(WebTenantResolver.CURRENT_TENANT, resolvedTenant);
            
            logger.log(Level.FINE,"CURRENT_TENANT {}", resolvedTenant.getId());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to resolve MultiTenantRequestListener - request", ex);
        }
    }

}
