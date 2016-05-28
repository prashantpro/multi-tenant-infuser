package org.prashantp.multitenant;

import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author prashant
 *
 */
public class StartupInitializer implements ServletContainerInitializer {
    
    private static final Logger logger = Logger.getLogger(StartupInitializer.class.getName());

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        MultiTenantConfiguration.setup(ctx);

        logger.info("Running MultiTenant Infuser Library " + getClass().getPackage().getSpecificationVersion());
    }

}
