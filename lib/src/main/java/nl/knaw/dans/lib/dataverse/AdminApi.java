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

import nl.knaw.dans.lib.dataverse.model.user.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * API end-points that operate on a dataverse collection.
 *
 * See [Dataverse API Guide].
 *
 * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#dataverse-collections
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
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-single-user
     */
    public DataverseResponse2<AuthenticatedUser> listSingleUser(String id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "authenticatedUsers", id);
        return httpClientWrapper.get2(path, new HashMap<>(), new HashMap<>(), AuthenticatedUser.class);
    }
}
