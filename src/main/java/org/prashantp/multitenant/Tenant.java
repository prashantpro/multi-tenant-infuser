package org.prashantp.multitenant;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a tenant object with all its properties loaded.
 * 
 * @author prashant
 */
public class Tenant implements Serializable {
    private final String id;
    private final String clientIdentifierValue;
    private final String tenantPrefix;
    
    private final TenantProperties properties;
    
    /**
     * @param clientIdentifier
     * @param prefix
     * @param props
     */
    Tenant(String clientIdentifier,String prefix, TenantProperties props) {
        this.id = UUID.randomUUID().toString();
        this.clientIdentifierValue = clientIdentifier;
        this.properties = props;
        this.tenantPrefix = prefix;
    }
    
    /**
     * This is a generated value which is unique for each tenant. It's assigned at time of tenant creation.
     * 
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * This is the value that identifies the client from a request.
     * Example:
     *     For a domain of abc.com the value would be abc.com
     *     For a context of /abc the value would be /abc
     *     
     * @return the clientIdentifierValue
     */
    public String getClientIdentifierValue() {
        return clientIdentifierValue;
    }

    public String getProperty(String key) {
        return this.properties.getPrefixedProperty(tenantPrefix,key);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tenant other = (Tenant) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
