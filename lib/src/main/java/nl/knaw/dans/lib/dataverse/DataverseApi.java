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
import nl.knaw.dans.lib.dataverse.model.RoleAssignmentReadOnly;
import nl.knaw.dans.lib.dataverse.model.dataset.Dataset;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetCreationResult;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlockSummary;
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

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

/**
 * API end-points that operate on a dataverse collection.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#dataverse-collections" target="_blank">Dataverse documentation</a>
 */
public class DataverseApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(DataverseApi.class);
    private final Path subPath;
    private static final String publish = "actions/:publish";

    protected DataverseApi(HttpClientWrapper httpClientWrapper, String alias) {
        super(httpClientWrapper);
        this.subPath = Paths.get("api/dataverses/").resolve(alias + "/");
    }

    /**
     * @param dataverse dataverse to create
     * @return description of the dataverse just created
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Dataverse> create(String dataverse) throws IOException, DataverseException {
        return httpClientWrapper.postJsonString2(subPath, dataverse, new HashMap<>(), new HashMap<>(), Dataverse.class);
    }

    /**
     * @param dataverse dataverse to create
     * @return description of the dataverse just created
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Dataverse> create(Dataverse dataverse) throws IOException, DataverseException {
        return httpClientWrapper.postModelObjectAsJson(subPath, dataverse, new HashMap<>(), new HashMap<>(), Dataverse.class);
    }

    /**
     * @return Information about a dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#view-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Dataverse> view() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath, Dataverse.class);
    }

    /**
     * @return a generic response
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#delete-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> delete() throws IOException, DataverseException {
        return httpClientWrapper.delete(subPath, DataMessage.class);
    }

    /**
     * @return a list of information items about subverses
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#show-contents-of-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<DataverseItem>> getContents() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("contents"), List.class, DataverseItem.class);
    }

    /**
     * @return a data message containing information about the storage size
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#report-the-data-file-size-of-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> getStorageSize() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("storagesize"), DataMessage.class);
    }

    /**
     * @return a list of roles
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-roles-defined-in-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<Role>> listRoles() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("roles"), List.class, Role.class);
    }

    /**
     * @return a list of facets
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-facets-configured-for-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> listFacets() throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /*
     * https://guides.dataverse.org/en/latest/api/native-api.html#set-facets-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> setFacets(List<String> facets) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /* https://guides.dataverse.org/en/latest/api/native-api.html#create-a-new-role-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> createRole(String role) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /*
     *  https://guides.dataverse.org/en/latest/api/native-api.html#create-a-new-role-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> createRole(Role role) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /* https://guides.dataverse.org/en/latest/api/native-api.html#list-role-assignments-in-a-dataverse-collection
     */
    public DataverseHttpResponse<List<RoleAssignmentReadOnly>> listRoleAssignments() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("assignments"), List.class, RoleAssignmentReadOnly.class);
    }

    /*
     * https://guides.dataverse.org/en/latest/api/native-api.html#assign-default-role-to-user-creating-a-dataset-in-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> assignDefaultRoleOnDataset(String roleName) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /*
     * https://guides.dataverse.org/en/latest/api/native-api.html#assign-a-new-role-on-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> assignRole(RoleAssignment roleAssignment) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /* https://guides.dataverse.org/en/latest/api/native-api.html#delete-role-assignment-from-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> deleteRoleAssignment(int roleAssignmentId) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * @return a list of metadata block summaries
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-metadata-blocks-defined-on-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<MetadataBlockSummary>> listMetadataBlocks() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("metadatablocks"), List.class, MetadataBlockSummary.class);
    }

    /* https://guides.dataverse.org/en/latest/api/native-api.html#define-metadata-blocks-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> defineMetadataBlocks(List<String> metadataBlocks) throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * @return <code>true</code> if this dataverse is a metadata blocks root, <code>false</code> otherwise
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#determine-if-a-dataverse-collection-inherits-its-metadata-blocks-from-its-parent" target="_blank">Dataverse
     * documentation</a>
     */
    public DataverseHttpResponse<Boolean> isMetadataBlocksRoot() throws IOException, DataverseException {
        return httpClientWrapper.get(subPath.resolve("metadatablocks/isRoot"), Boolean.class);
    }

    /**
     * @param isRoot whether to make this dataverse collection a metadata blocks root
     * @return a data message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#configure-a-dataverse-collection-to-inherit-its-metadata-blocks-from-its-parent" target="_blank">Dataverse
     * documentation</a>
     */
    public DataverseHttpResponse<DataMessage> setMetadataBlocksRoot(boolean isRoot) throws IOException, DataverseException {
        return httpClientWrapper.putTextString(subPath.resolve("metadatablocks/isRoot"), Boolean.toString(isRoot), Collections.emptyMap(), Collections.emptyMap(),
            DataMessage.class);
    }

    /**
     * @param dataset JSON string defining the dataset to create
     * @return a creation result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(String dataset) throws IOException, DataverseException {
        return createDataset(dataset, Collections.emptyMap());
    }

    /**
     * @param dataset      JSON string defining the dataset to create
     * @param metadataKeys the HashMap maps the names of the metadata blocks to their 'secret' key values
     * @return a creation result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(String dataset, Map<String, String> metadataKeys) throws IOException, DataverseException {
        Map<String, List<String>> queryParams = getQueryParamsFromMetadataKeys(metadataKeys);
        return httpClientWrapper.postJsonString2(subPath.resolve("datasets"), dataset, queryParams, Collections.emptyMap(), DatasetCreationResult.class);
    }

    /**
     * @param dataset the dataset to create
     * @return a creation result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(Dataset dataset) throws IOException, DataverseException {
        return createDataset(httpClientWrapper.writeValueAsString(dataset), Collections.emptyMap());
    }

    /**
     * @param dataset      the dataset to create
     * @param metadataKeys maps the names of the metadata blocks to their 'secret' key values
     * @return a creation result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#create-a-dataset-in-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> createDataset(Dataset dataset, Map<String, String> metadataKeys) throws IOException, DataverseException {
        return createDataset(httpClientWrapper.writeValueAsString(dataset), metadataKeys);
    }

    /**
     * @param dataset         the dataset to import
     * @param persistentId existing persistent identifier (PID)
     * @param autoPublish     immediately publish the dataset
     * @return an import result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> importDataset(Dataset dataset, String persistentId, boolean autoPublish) throws IOException, DataverseException {
        return importDataset(httpClientWrapper.writeValueAsString(dataset), persistentId, autoPublish, emptyMap());
    }

    /**
     * @param dataset         the dataset to import
     * @param persistentId existing persistent identifier (PID)
     * @param autoPublish     immediately publish the dataset
     * @param metadataKeys    maps the names of the metadata blocks to their 'secret' key values
     * @return an import result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> importDataset(Dataset dataset, String persistentId, boolean autoPublish, Map<String, String> metadataKeys)
        throws IOException, DataverseException {
        return importDataset(httpClientWrapper.writeValueAsString(dataset), persistentId, autoPublish, metadataKeys);
    }

    /**
     * @param dataset         JSON string defining the dataset to import
     * @param persistentId existing persistent identifier (PID)
     * @param autoPublish     immediately publish the dataset
     * @return an import result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> importDataset(String dataset, String persistentId, boolean autoPublish)
        throws IOException, DataverseException {
        return importDataset(dataset, persistentId, autoPublish, emptyMap());
    }

    /**
     * @param dataset         JSON string defining the dataset to import
     * @param persistentId existing persistent identifier (PID)
     * @param autoPublish     immediately publish the dataset
     * @param metadataKeys    maps the names of the metadata blocks to their 'secret' key values
     * @return an import result message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-collection">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetCreationResult> importDataset(String dataset, String persistentId, boolean autoPublish, Map<String, String> metadataKeys)
        throws IOException, DataverseException {
        Map<String, List<String>> queryParams = getQueryParamsFromMetadataKeys(metadataKeys);
        Map<String, List<String>> parameters = new HashMap<>(queryParams);
        if (persistentId != null) {
            parameters.put("release", singletonList("" + autoPublish));
            parameters.put("pid", singletonList(persistentId));
        }
        return httpClientWrapper.postJsonString2(subPath.resolve("datasets/:import"), dataset, parameters, emptyMap(), DatasetCreationResult.class);
    }

    /* https://guides.dataverse.org/en/latest/api/native-api.html#import-a-dataset-into-a-dataverse-installation-with-a-ddi-file
     */
    public DataverseHttpResponse<DatasetCreationResult> importDatasetFromDdi() throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    /**
     * @return a data message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#publish-a-dataverse-collection" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> publish() throws IOException, DataverseException {
        return httpClientWrapper.postJsonString2(subPath.resolve(publish), "", new HashMap<>(), new HashMap<>(), DataMessage.class);
    }

    /*https://guides.dataverse.org/en/latest/api/native-api.html#retrieve-guestbook-responses-for-a-dataverse-collection
     */
    public DataverseHttpResponse<DataMessage> getGuestBookResponses() throws IOException, DataverseException {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
