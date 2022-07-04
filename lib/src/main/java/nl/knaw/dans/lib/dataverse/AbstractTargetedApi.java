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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

abstract class AbstractTargetedApi extends AbstractApi {

    protected static final String persistendId = ":persistentId/";
    protected static final String publish = "actions/:publish";

    protected final Path targetBase;
    protected final String id;
    protected final boolean isPersistentId;

    protected AbstractTargetedApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, Path targetBase) {
        super(httpClientWrapper);
        this.id = id;
        this.isPersistentId = isPersistentId;
        this.targetBase = targetBase;
    }

    protected Map<String, List<String>> params(Map<String, List<String>> queryParams) {
        if (!isPersistentId)
            return queryParams;
        HashMap<String, List<String>> parameters = new HashMap<>();
        parameters.put("persistentId", singletonList(id));
        parameters.putAll(queryParams);
        return parameters;
    }

    protected Path subPath(String endPoint) {
        if (isPersistentId)
            return buildPath(targetBase, persistendId, endPoint);
        else
            return buildPath(targetBase, id, endPoint);
    }

    protected Path versionedSubPath(String endPoint, String version) {
        if (isPersistentId)
            return buildPath(targetBase, persistendId, "versions", version, endPoint);
        else
            return buildPath(targetBase, id, "versions", version, endPoint);
    }
}
