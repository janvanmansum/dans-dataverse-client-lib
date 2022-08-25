/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.dataverse;

import nl.knaw.dans.lib.dataverse.model.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessRequestsApi extends AbstractTargetedApi {

    private static final Logger log = LoggerFactory.getLogger(DataAccessRequestsApi.class);
    private final Path subPath = subPath("allowAccessRequest/");
    private final Map<String, List<String>> params = params(new HashMap<>());
    private final HashMap<String, String> headers = new HashMap<>();

    protected DataAccessRequestsApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper, id, isPersistentId, null, Paths.get("api/access/"));
        log.trace("ENTER");
    }

    protected DataAccessRequestsApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper, id, isPersistentId, invocationId, Paths.get("api/access/"));
        log.trace("ENTER");
    }


    public DataverseResponse<DataMessage> enable() throws IOException, DataverseException {
        log.trace("ENTER");
        return toggle("true");
    }

    public DataverseResponse<DataMessage> disable() throws IOException, DataverseException {
        log.trace("ENTER");
        return toggle("false");
    }


    private DataverseResponse<DataMessage> toggle(String bool) throws IOException, DataverseException {
        return httpClientWrapper.putTextString(subPath, bool, params, headers, DataMessage.class);
    }
}
