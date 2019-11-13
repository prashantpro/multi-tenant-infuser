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
package org.tanmayra.multitenant.extractor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.tanmayra.multitenant.TenantIdExtractor;

/**
 *
 * @author prashantp <http://www.prashantp.org>
 */
public class DNSIdExtractor implements TenantIdExtractor {

    private static final Logger logger = Logger.getLogger(DNSIdExtractor.class.getName());

    @Override
    public String extractId(HttpServletRequest request) {
//        String domain = new URL(request.getRequestURL().toString()).getHost();
//        String domain = request.getServerName();
        String domain = request.getHeader("X-forwarded-host");
        logger.log(Level.FINE, "DNS tenant - {}", domain);
        return domain;
    }

}
