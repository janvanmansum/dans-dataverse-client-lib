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
import nl.knaw.dans.lib.dataverse.model.user.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Administration API end-points.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#admin" target="_blank">Dataverse documentation</a>
 */
public class AdminApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(AdminApi.class);
    private final Path targetBase;

    protected AdminApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        log.trace("ENTER");
        this.targetBase = Paths.get("api/admin/");
    }

    /**
     * @param id username
     * @return the user
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-single-user" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<AuthenticatedUser> listSingleUser(String id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "authenticatedUsers", id);
        return httpClientWrapper.get(path, new HashMap<>(), new HashMap<>(), AuthenticatedUser.class);
    }

    /**
     * @param key   the settings key
     * @param value the new value
     * @return the result
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/installation/config.html#database-settings" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Map<String, String>> putDatabaseSetting(String key, String value) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "settings", key);
        return httpClientWrapper.putJsonString(path, value, new HashMap<>(), new HashMap<>(), Map.class);
    }

    /**
     * @param key   the settings key
     * @return the result
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/installation/config.html#database-settings" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> getDatabaseSetting(String key) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "settings", key);
        return httpClientWrapper.get(path, DataMessage.class);
    }



}
