/*
 * Copyright 2016 prashantp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanmayra.multitenant;

import org.tanmayra.multitenant.WebTenantResolver.TenantResolverType;
import org.tanmayra.multitenant.extractor.ContextIdExtractor;
import org.tanmayra.multitenant.extractor.DNSIdExtractor;
import org.tanmayra.multitenant.extractor.ThreadLocalIdExtractor;

/**
 *
 * @author prashantp <http://www.prashantp.org>
 */
class TenantIdExtractorFactory {
    
    public static TenantIdExtractor create(TenantResolverType resolverType) {
        switch(resolverType) {
            case DNS:
                return new DNSIdExtractor();
            case CONTEXT:
                return new ContextIdExtractor();
            case THREAD:
                return new ThreadLocalIdExtractor();
        }
        return null;
    }
}
