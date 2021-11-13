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

import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DatasetApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(DatasetApi.class);
    private static final String persistendId = ":persistentId/";

    private final Path targetBase;
    private final String id;
    private final boolean isPersistentId;

    protected DatasetApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/datasets/");
        this.id = id;
        this.isPersistentId = isPersistentId;
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-json-representation-of-a-dataset

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-versions-of-a-dataset
     */
    public DataverseResponse<List<DatasetVersion>> getAllVersions() throws IOException, DataverseException {
        // Not specifying a version results in getting all versions.
        return getVersionedFromTarget("", "", List.class, DatasetVersion.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#get-version-of-a-dataset
     */
    public DataverseResponse<DatasetVersion> getVersion(String version) throws IOException, DataverseException {
        if (StringUtils.isBlank(version))
            throw new IllegalArgumentException("Argument 'version' may not be empty");
        return getVersionedFromTarget("", version, DatasetVersion.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-files-in-a-dataset
     */
    public DataverseResponse<List<FileMeta>> getFiles(String version) throws IOException, DataverseException {
        log.trace("ENTER");
        return getVersionedFromTarget("files", version, List.class, FileMeta.class);
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#export-metadata-of-a-dataset-in-various-formats
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#schema-org-json-ld
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#view-dataset-files-and-folders-as-a-directory-index
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-all-metadata-blocks-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-single-metadata-block-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-metadata
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#publish-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-draft
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#set-citation-date-field-type-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#revert-citation-date-field-type-to-default-for-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-role-assignments-in-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#assign-a-new-role-on-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-role-assignment-from-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-the-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-the-private-url-from-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#report-the-data-file-size-of-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-the-size-of-downloading-all-the-files-of-a-dataset-version
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#submit-a-dataset-for-review
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#return-a-dataset-to-author
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#link-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#dataset-locks
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#retrieving-total-views-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#retrieving-unique-views-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#retrieving-total-downloads-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#retrieving-unique-downloads-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#retrieving-citations-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-unpublished-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-published-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#configure-a-dataset-to-use-a-specific-file-store
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#view-the-timestamps-on-a-dataset

    /*
     * Helper methods
     */
    private <D> DataverseHttpResponse<D> getVersionedFromTarget(String endPoint, String version, Class<?>... outputClass) throws IOException, DataverseException {
        log.trace("ENTER");
        if (isPersistentId) {
            HashMap<String, List<String>> parameters = new HashMap<>();
            parameters.put("persistentId", Collections.singletonList(id));
            return httpClientWrapper.get(buildPath(targetBase, persistendId, "versions", version, endPoint), parameters, outputClass);
        }
        else {
            return httpClientWrapper.get(buildPath(targetBase, id, "versions", version, endPoint), outputClass);
        }
    }
}
