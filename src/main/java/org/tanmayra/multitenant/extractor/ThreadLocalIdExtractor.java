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
import org.tanmayra.multitenant.TenantThreadLocal;

/**
 *
 * @author prashantp <http://www.prashantp.org>
 */
public class ThreadLocalIdExtractor implements TenantIdExtractor {

    private static final Logger logger = Logger.getLogger(ThreadLocalIdExtractor.class.getName());

    @Override
    public String extractId(HttpServletRequest request) {
        logger.log(Level.FINE, "ThreadLocal tenant - ");
        TenantThreadLocal.setTenantId("ooc");
        return TenantThreadLocal.getTenantId();
    }
}
