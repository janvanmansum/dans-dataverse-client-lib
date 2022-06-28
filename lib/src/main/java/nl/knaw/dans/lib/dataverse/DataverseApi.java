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
import nl.knaw.dans.lib.dataverse.model.Role;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.dataset.Dataset;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetCreationResult;
import nl.knaw.dans.lib.dataverse.model.dataverse.Dataverse;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

/**
 * API end-points that operate on a dataverse collection.
 *
 * See [Dataverse API Guide].
 *
 * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#dataverse-collections
 */
public class DataverseApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(DataverseApi.class);
    private final Path subPath;
    private static final String publish = "actions/:publish";

    protected DataverseApi(HttpClientWrapper httpClientWrapper, String alias) {
        super(httpClientWrapper);
        log.trace("ENTER");
        this.subPath = Paths.get("api/dataverses/").resolve(alias + "/");
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataverse-collection
     *
     * @param dataverse dataverse to create
     * @return description of the dataverse just created
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Dataverse> create(String dataverse) throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.postJsonString(subPath, dataverse, new HashMap<>(), new HashMap<>(), Dataverse.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataverse-collection
     *
     * @param dataverse dataverse to create
     * @return description of the dataverse just created
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Dataverse> create(Dataverse dataverse) throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.postModelObjectAsJson(subPath, dataverse, new HashMap<>(), new HashMap<>(), Dataverse.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#view-a-dataverse-collection
     *
     * @return Information about a dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Dataverse> view() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath, Dataverse.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#delete-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> delete() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.delete(subPath, DataMessage.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#show-contents-of-a-dataverse-collection
     */
    public DataverseHttpResponse<List<DataverseItem>> getContents() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath.resolve("contents"), List.class, DataverseItem.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#report-the-data-file-size-of-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> getStorageSize() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath.resolve("storagesize"), DataMessage.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-roles-defined-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> listRoles() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath.resolve("roles"), DataMessage.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-facets-configured-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> listFacets() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#set-facets-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> setFacets(List<String> facets) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-new-role-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> createRole(String role) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-new-role-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> createRole(Role role) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-role-assignments-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> listRoleAssignments() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#assign-default-role-to-user-creating-a-dataset-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> assignDefaultRoleOnDataset(String roleName) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#assign-a-new-role-on-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> assignRole(RoleAssignment roleAssignment) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#delete-role-assignment-from-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> deleteRoleAssignment(int roleAssignmentId) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-metadata-blocks-defined-on-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> listMetadataBlocks() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#define-metadata-blocks-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> defineMetadataBlocks(List<String> metadataBlocks) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#determine-if-a-dataverse-collection-inherits-its-metadata-blocks-from-its-parent
     */
    public DataverseHttpResponse<Boolean> isMetadataBlocksRoot() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath.resolve("metadatablocks/isRoot"), Boolean.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#configure-a-dataverse-collection-to-inherit-its-metadata-blocks-from-its-parent
     */
    public DataverseHttpResponse<DataMessage> setMetadataBlocksRoot(boolean isRoot) throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.putTextString(subPath.resolve("metadatablocks/isRoot"), Boolean.toString(isRoot),
            Collections.emptyMap(), Collections.emptyMap(), DataMessage.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(String dataset) throws IOException, DataverseException {
        log.trace("ENTER");
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(Dataset dataset) throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-collection
     */
    public DataverseHttpResponse<DatasetCreationResult> importDataset() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public DataverseHttpResponse<DatasetCreationResult> importDataset(String dataset, Optional<String> optPersistentId, boolean autoPublish) throws IOException, DataverseException {
        log.trace("ENTER");
        Map<String, List<String>> parameters = new HashMap<>();
        optPersistentId.ifPresent(pid -> parameters.put("release", singletonList("" + autoPublish)));
        optPersistentId.ifPresent(pid -> parameters.put("pid", singletonList(pid)));
        return httpClientWrapper.postJsonString(subPath.resolve("datasets/:import"), dataset, parameters, emptyMap(), DatasetCreationResult.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-installation-with-a-ddi-file
     */
    public DataverseHttpResponse<DatasetCreationResult> importDatasetFromDdi() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#publish-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> publish() throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.postJsonString(subPath.resolve(publish), "", new HashMap<>(), new HashMap<>(), DataMessage.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#retrieve-guestbook-responses-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> getGuestBookResponses() throws IOException, DataverseException {
        log.trace("ENTER");
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
