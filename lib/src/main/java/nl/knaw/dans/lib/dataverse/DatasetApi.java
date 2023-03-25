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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.DataMessage;
import nl.knaw.dans.lib.dataverse.model.Lock;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.RoleAssignmentReadOnly;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetLatestVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetPublicationResult;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.Embargo;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import nl.knaw.dans.lib.dataverse.model.dataset.UpdateType;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

/**
 * API end-points dealing with a single dataset.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#datasets" target="_blank">Dataverse documentation</a>
 */
@Slf4j
public class DatasetApi extends AbstractTargetedApi {

    protected DatasetApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        this(httpClientWrapper, id, isPersistentId, null);
    }

    protected DatasetApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper, id, isPersistentId, invocationId, Paths.get("api/datasets/"));
    }

    /**
     * @return a JSON object that starts at the dataset level, most fields are replicated at the dataset version level.
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-json-representation-of-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetLatestVersion> getLatestVersion() throws IOException, DataverseException {
        return getUnversionedFromTarget("", DatasetLatestVersion.class);
    }

    /**
     * Retrieves that latest version of a dataset. The difference with {@link #getLatestVersion()} is that the latter returns a different type of object. It is not clear why these variants exist.
     *
     * @return object containing the dataset version metadata
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-version-of-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> getVersion() throws IOException, DataverseException {
        // Not specifying a version results in getting all versions.
        return getVersionedFromTarget("", Version.LATEST.toString(), DatasetVersion.class);
    }

    /**
     * @param version version to retrieve
     * @return object containing the dataset version metadata
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-version-of-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> getVersion(String version) throws IOException, DataverseException {
        if (StringUtils.isBlank(version))
            throw new IllegalArgumentException("Argument 'version' may not be empty");
        return getVersionedFromTarget("", version, DatasetVersion.class);
    }

    /**
     * @return list of objects containing dataset version metadata
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-versions-of-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<DatasetVersion>> getAllVersions() throws IOException, DataverseException {
        // Not specifying a version results in getting all versions.
        return getVersionedFromTarget("", "", List.class, DatasetVersion.class);
    }

    /**
     * @param version version to get file metadata from
     * @return a list of file metas
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-files-in-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<FileMeta>> getFiles(String version) throws IOException, DataverseException {
        return getVersionedFromTarget("files", version, List.class, FileMeta.class);
    }

    /**
     * @return dataset publication result
     * @throws IOException        if an I/O exception occurs
     * @throws DataverseException if Dataverse could not handle the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#publish-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetPublicationResult> publish() throws IOException, DataverseException {
        Path path = buildPath(targetBase, persistendId, publish);
        HashMap<String, List<String>> parameters = new HashMap<>();
        parameters.put("persistentId", singletonList(id));
        parameters.put("type", singletonList("major"));
        return httpClientWrapper.postJsonString(path, "", parameters, emptyMap(), DatasetPublicationResult.class);
    }

    public DataverseHttpResponse<DataMessage> publish(UpdateType updateType, boolean assureIsIndexed) throws IOException, DataverseException {
        HashMap<String, List<String>> parameters = new HashMap<>();
        parameters.put("assureIsIndexed", singletonList(String.valueOf(assureIsIndexed)));
        parameters.put("type", singletonList(updateType.toString()));
        return httpClientWrapper.postJsonString(subPath(publish), "", params(parameters), new HashMap<>(), DataMessage.class);
    }

    public DataverseHttpResponse<DatasetPublicationResult> releaseMigrated(String publicationDateJsonLd, boolean assureIsIndexed) throws IOException, DataverseException {
        Map<String, List<String>> parameters = singletonMap("assureIsIndexed", singletonList(String.valueOf(assureIsIndexed)));
        return httpClientWrapper.postJsonLdString(subPath("actions/:releasemigrated"), publicationDateJsonLd, params(parameters), emptyMap(), DatasetPublicationResult.class);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     *
     * @param s JSON document containing the edits to perform
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(String s) throws IOException, DataverseException {
        return editMetadata(s, true, emptyMap());
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     *
     * @param s            JSON document containing the edits to perform
     * @param metadataKeys the HashMap maps the names of the metadata blocks to their 'secret' key values.
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(String s, Map<String, String> metadataKeys) throws IOException, DataverseException {
        return editMetadata(s, true, metadataKeys);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     *
     * @param s       JSON document containing the edits to perform
     * @param replace whether to replace existing values
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(String s, Boolean replace) throws IOException, DataverseException {
        return editMetadata(s, replace, emptyMap());
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     * Whenever there are metadata field from a block that is protected by a 'key', the corresponding keys must be provided.
     *
     * @param s            JSON document containing the edits to perform
     * @param replace      whether to replace existing values
     * @param metadataKeys the HashMap maps the names of the metadata blocks to their 'secret' key values.
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(String s, Boolean replace, Map<String, String> metadataKeys) throws IOException, DataverseException {
        Map<String, List<String>> queryParams = getQueryParamsFromMetadataKeys(metadataKeys);
        if (replace)
            /*
             * Sic! any value for "replace" is interpreted by Dataverse as "true", even "replace=false"
             * It is by the *absence* of the parameter that replace is set to false.
             */
            queryParams.put("replace", singletonList("true"));
        return putToTarget("editMetadata", s, queryParams, DatasetVersion.class);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     *
     * @param fields  list of fields to edit
     * @param replace whether to replace existing values
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(FieldList fields, Boolean replace) throws IOException, DataverseException {
        return editMetadata(fields, replace, emptyMap());
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow multiple values.
     *
     * @param fields       list of fields to edit
     * @param replace      whether to replace existing values
     * @param metadataKeys the HashMap maps the names of the metadata blocks to their 'secret' key values.
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(FieldList fields, Boolean replace, Map<String, String> metadataKeys) throws IOException, DataverseException {
        return editMetadata(httpClientWrapper.writeValueAsString(fields), replace, metadataKeys);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet.
     *
     * @param fields list of fields to edit
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(FieldList fields) throws IOException, DataverseException {
        return editMetadata(fields, true, emptyMap());
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet.
     *
     * @param fields       list of fields to edit
     * @param metadataKeys the HashMap maps the names of the metadata blocks to their 'secret' key values.
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> editMetadata(FieldList fields, Map<String, String> metadataKeys) throws IOException, DataverseException {
        return editMetadata(fields, true, metadataKeys);
    }

    // TODO https://guides.dataverse.org/en/latest/api/native-api.html#export-metadata-of-a-dataset-in-various-formats
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#schema-org-json-ld
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#view-dataset-files-and-folders-as-a-directory-index
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-all-metadata-blocks-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-single-metadata-block-for-a-dataset

    /**
     * @param metadata JSON document describing the metadata
     * @param replace  replace existing metadata
     * @return a generic DataverseHttpResponse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/developers/dataset-semantic-metadata-api.html#add-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Object> updateMetadataFromJsonLd(String metadata, boolean replace) throws IOException, DataverseException {
        return updateMetadataFromJsonLd(metadata, replace, emptyMap());
    }

    /**
     * @param metadata     JSON document describing the metadata
     * @param replace      replace existing metadata
     * @param metadataKeys maps the names of the metadata blocks to their 'secret' key values
     * @return a generic DataverseResponse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/developers/dataset-semantic-metadata-api.html#add-dataset-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Object> updateMetadataFromJsonLd(String metadata, boolean replace, Map<String, String> metadataKeys) throws IOException, DataverseException {
        Map<String, List<String>> queryParams = getQueryParamsFromMetadataKeys(metadataKeys);
        queryParams.put("replace", singletonList((String.valueOf(replace))));
        return httpClientWrapper.putJsonLdString(subPath("metadata"), metadata, params(queryParams), extraHeaders, RoleAssignmentReadOnly.class);
    }

    /**
     * @param s JSON document containing the new metadata
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> updateMetadata(String s) throws IOException, DataverseException {
        return updateMetadata(s, emptyMap());
    }

    /**
     * @param s            JSON document containing the new metadata
     * @param metadataKeys maps the names of the metadata blocks to their 'secret' key values
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> updateMetadata(String s, Map<String, String> metadataKeys) throws IOException, DataverseException {
        Map<String, List<String>> queryParams = getQueryParamsFromMetadataKeys(metadataKeys);
        // Cheating with endPoint here, because the only version that can be updated is :draft anyway
        return putToTarget("versions/:draft", s, queryParams, DatasetVersion.class);
    }

    /**
     * Note that not all the attributes of the DatasetVersion object are writable. Dataverse may ignore some (e.g., license) or return an error if some are filled in (e.g., files). However, for a few
     * attributes, such as fileAccessRequest and termsOfAccess, it is necessary to pass the whole version object instead of only the metadata blocks (as is done in the example of the API
     * documentation).
     *
     * @param version a version object containing the new metadata
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> updateMetadata(DatasetVersion version) throws IOException, DataverseException {
        return updateMetadata(httpClientWrapper.writeValueAsString(version), emptyMap());
    }

    /**
     * @param version      a version object containing the new metadata
     * @param metadataKeys maps the names of the metadata blocks to their 'secret' key values
     * @return DatasetVersion
     * @throws IOException
     * @throws DataverseException
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DatasetVersion> updateMetadata(DatasetVersion version, Map<String, String> metadataKeys) throws IOException, DataverseException {
        return updateMetadata(httpClientWrapper.writeValueAsString(version), metadataKeys);
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-metadata

    /**
     * @return a data message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-draft" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> deleteDraft() throws IOException, DataverseException {
        return httpClientWrapper.delete(subPath("versions/:draft"), params(emptyMap()), DataMessage.class);
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#set-citation-date-field-type-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#revert-citation-date-field-type-to-default-for-dataset

    /**
     * @return role assignment information
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-role-assignments-on-a-dataverse-api" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<RoleAssignmentReadOnly>> listRoleAssignments() throws IOException, DataverseException {
        return getUnversionedFromTarget("assignments", List.class, RoleAssignmentReadOnly.class);
    }

    /**
     * @param roleAssignment JSON document describing the assignment
     * @return role assignment information
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#assign-a-new-role-on-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<RoleAssignmentReadOnly> assignRole(String roleAssignment) throws IOException, DataverseException {
        return httpClientWrapper.postJsonString(subPath("assignments"), roleAssignment, params(emptyMap()), extraHeaders, RoleAssignmentReadOnly.class);
    }

    public DataverseHttpResponse<RoleAssignmentReadOnly> assignRole(RoleAssignment roleAssignment) throws IOException, DataverseException {
        return assignRole(httpClientWrapper.writeValueAsString(roleAssignment));
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-role-assignment-from-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-the-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-the-private-url-from-a-dataset

    /**
     * @param file     the file to add
     * @param metadata json document with the file metadata
     * @return a file list
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<FileList> addFile(Path file, String metadata) throws IOException, DataverseException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        Optional.ofNullable(file).ifPresent(f -> builder.addPart("file", new FileBody(f.toFile(), ContentType.APPLICATION_OCTET_STREAM, f.getFileName().toString())));
        Optional.ofNullable(metadata).ifPresent(m -> builder.addPart("jsonData", new StringBody(m, ContentType.APPLICATION_JSON)));
        return httpClientWrapper.post(subPath("add"), builder.build(), params(emptyMap()), extraHeaders, FileList.class);
    }

    /**
     * @param file     the file to add
     * @param fileMeta json document with the file metadata
     * @return a file list
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<FileList> addFile(Path file, FileMeta fileMeta) throws IOException, DataverseException {
        return addFile(file, httpClientWrapper.writeValueAsString(fileMeta));
    }


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

    // TODO: FIRST
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#view-the-timestamps-on-a-dataset
    /**
     * Publishes the current draft of an imported dataset as a new version with the original publication date.
     *
     * If publish is called shortly after a modification and there is a pre-publication workflow installed, there is a risk of the workflow failing to
     * start because of an OptimisticLockException. This is caused by Dataverse indexing the dataset on a separate thread. This will appear to the client
     * as Dataverse silently failing (i.e. returning success but not publishing the dataset). To make sure that indexing has already happened the `assureIsIndexed`
     * parameter is set to `true`. It will cause Dataverse to fail fast if indexing is still pending.
     *
     * @param publicationDateJsonLd original publication date
     * @param assureIsIndexed       make Dataverse return 409 Conflict if an index action is pending
     * @return
     */

    /**
     * @param embargo the embargo data
     * @return a hash map
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#set-an-embargo-on-files-in-a-dataset" target="_blank">Dataverse documentation</a>
     */
    // TODO: can the hash map be converted to a more specific object?
    public DataverseHttpResponse<HashMap> setEmbargo(Embargo embargo) throws IOException, DataverseException {
        return setEmbargo(httpClientWrapper.writeValueAsString(embargo));
    }

    public DataverseHttpResponse<HashMap> setEmbargo(String json) throws IOException, DataverseException {
        return httpClientWrapper.postJsonString(subPath("files/actions/:set-embargo"), json, params(emptyMap()), extraHeaders, HashMap.class);
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#remove-an-embargo-on-files-in-a-dataset

    /*
     * Helper methods
     */
    private <D> DataverseHttpResponse<D> getVersionedFromTarget(String endPoint, String version, Class<?>... outputClass) throws IOException, DataverseException {
        return httpClientWrapper.get(versionedSubPath(endPoint, version), params(emptyMap()), extraHeaders, outputClass);
    }

    private <D> DataverseHttpResponse<D> getUnversionedFromTarget(String endPoint, Map<String, List<String>> queryParams, Class<?>... outputClass)
        throws IOException, DataverseException {
        return httpClientWrapper.get(subPath(endPoint), params(queryParams), extraHeaders, outputClass);
    }

    private <D> DataverseHttpResponse<D> getUnversionedFromTarget(String endPoint, Class<?>... outputClass) throws IOException, DataverseException {
        return getUnversionedFromTarget(endPoint, emptyMap(), outputClass);
    }

    private <D> DataverseHttpResponse<D> putToTarget(String endPoint, String body, Map<String, List<String>> queryParams, Class<?>... outputClass)
        throws IOException, DataverseException {
        return httpClientWrapper.putJsonString(subPath(endPoint), body, params(queryParams), extraHeaders, outputClass);
    }

    /**
     * @return a list of locks
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#dataset-locks" target="_blank">...</a>
     * @see <a href="https://github.com/DANS-KNAW/dans-dataverse-client-lib/blob/master/examples/src/main/java/nl/knaw/dans/lib/dataverse/example/DatasetGetLocks.java">Code example</a>
     */
    public DataverseHttpResponse<List<Lock>> getLocks() throws IOException, DataverseException {
        return getUnversionedFromTarget("locks", List.class, Lock.class);
    }

    /**
     * Utility function that lets you wait until all locks are cleared before proceeding. Unlike most other functions in this library, this does not correspond directly with an API call. Rather the
     * {@link #getLocks()} call is done repeatedly to check if the locks have been cleared. Note that in scenarios where concurrent processes might access the same dataset it is not guaranteed that
     * the locks, once cleared, stay that way.
     *
     * @param maxNumberOfRetries     the maximum number the check for unlock is made, defaults to [[awaitLockStateMaxNumberOfRetries]]
     * @param waitTimeInMilliseconds the time between tries, defaults to [[awaitLockStateMillisecondsBetweenRetries]]
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public void awaitUnlock(int maxNumberOfRetries, int waitTimeInMilliseconds) throws IOException, DataverseException {
        awaitLockState(this::notLocked, "", "Wait for unlock expired", maxNumberOfRetries, waitTimeInMilliseconds);
    }

    /**
     * The same
     *
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public void awaitUnlock() throws IOException, DataverseException {
        awaitUnlock(httpClientWrapper.getConfig().getAwaitLockStateMaxNumberOfRetries(), httpClientWrapper.getConfig().getAwaitLockStateMillisecondsBetweenRetries());
    }

    /**
     * Utility function that lets you wait until a specified lock type is set. Unlike most other functions in this library, this does not correspond directly with an API call. Rather the
     * {@link #getLocks()} call is done repeatedly to check if the locks has been set. A use case is when an http/sr workflow wants to make sure that a dataset has been locked on its behalf, so that
     * it can be sure to have exclusive access via its invocation ID.
     *
     * @param lockType               the lock type to wait for
     * @param maxNumberOfRetries     the maximum number the check for unlock is made, defaults to #awawaitLockStateMaxNumberOfRetries
     * @param waitTimeInMilliseconds the time between tries, defaults to [[awaitLockStateMillisecondsBetweenRetries]]
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public void awaitLock(String lockType, int maxNumberOfRetries, int waitTimeInMilliseconds) throws IOException, DataverseException {
        awaitLockState(this::isLocked, lockType, String.format("Wait for lock of type %s expired", lockType), maxNumberOfRetries, waitTimeInMilliseconds);
    }

    /**
     * The same as {@link #awaitLock(String, int, int)} but with defaults for number of tries and time between tries.
     *
     * @param lockType the lock type to wait for
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public void awaitLock(String lockType) throws IOException, DataverseException {
        awaitLock(lockType, httpClientWrapper.getConfig().getAwaitLockStateMaxNumberOfRetries(), httpClientWrapper.getConfig().getAwaitLockStateMillisecondsBetweenRetries());
    }

    /**
     * Private functional interface to get the locking status, and methods implementing it.
     */
    @FunctionalInterface
    private interface Locked {
        Boolean get(List<Lock> locks, String lockType);
    }

    private Boolean isLocked(List<Lock> locks, String lockType) {
        for (Lock lock : locks) {
            if (lock.getLockType().equals(lockType))
                return true;
        }
        return false;
    }

    private Boolean notLocked(List<Lock> locks, String lockType) {
        return locks.isEmpty();
    }

    /**
     * Helper function that waits until the specified lockState function returns `true`, or throws a LockException if this never occurs within `maxNumberOrRetries` with `waitTimeInMilliseconds`
     * pauses.
     *
     * @param lockState              the function that returns whether the required state has been reached
     * @param lockType               type of locking
     * @param errorMessage           error to report in LockException if it occurs
     * @param maxNumberOfRetries     the maximum number of tries
     * @param waitTimeInMilliseconds the time to wait between tries
     */
    private void awaitLockState(Locked lockState, String lockType, String errorMessage, int maxNumberOfRetries, int waitTimeInMilliseconds)
        throws IOException, DataverseException {
        int numberOfTimesTried = 0;

        class CurrentLocks {
            private List<Lock> getCurrentLocks() throws IOException, DataverseException {
                List<Lock> locks = getLocks().getData();
                log.debug(String.format("Current locks: %s", locks.toString()));
                return locks;
            }

            private Boolean slept() {
                log.debug(String.format("Sleeping %d ms before next try..", waitTimeInMilliseconds));
                try {
                    Thread.sleep(waitTimeInMilliseconds);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }

        CurrentLocks currentLocks = new CurrentLocks();
        List<Lock> locks;
        do {
            locks = currentLocks.getCurrentLocks();
            numberOfTimesTried += 1;
        }
        while (!lockState.get(locks, lockType) && numberOfTimesTried != maxNumberOfRetries && currentLocks.slept());

        if (!lockState.get(locks, lockType))
            throw new RuntimeException(String.format("%s. Number of tries = %d, wait time between tries = %d ms.", errorMessage, maxNumberOfRetries, waitTimeInMilliseconds));
    }

}
