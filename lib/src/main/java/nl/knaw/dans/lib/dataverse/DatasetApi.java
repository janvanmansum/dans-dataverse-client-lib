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

import nl.knaw.dans.lib.dataverse.model.Lock;
import nl.knaw.dans.lib.dataverse.model.RoleAssignmentReadOnly;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetLatestVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetPublicationResult;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlock;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class DatasetApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(DatasetApi.class);
    private static final String persistendId = ":persistentId/";
    private static final String publish = "actions/:publish";

    private final Path targetBase;
    private final String id;
    private final boolean isPersistentId;
    private final Map<String, String> extraHeaders = new HashMap<>();

    protected DatasetApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        this(httpClientWrapper, id, isPersistentId, null);
    }

    protected DatasetApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/datasets/");
        this.id = id;
        this.isPersistentId = isPersistentId;
        if (invocationId != null)
            extraHeaders.put("X-Dataverse-invocationID", invocationId);
    }

    /**
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#get-json-representation-of-a-dataset
     *
     * @return a JSON object that starts at the dataset level, most fields are replicated at the dataset version level.
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetLatestVersion> viewLatestVersion() throws IOException, DataverseException {
        return getUnversionedFromTarget("", DatasetLatestVersion.class);
    }

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
     * @param version [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-files-in-a-dataset
     */
    public DataverseResponse<List<FileMeta>> getFiles(String version) throws IOException, DataverseException {
        log.trace("ENTER");
        return getVersionedFromTarget("files", version, List.class, FileMeta.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#publish-a-dataset
     */
    public DataverseResponse<DatasetPublicationResult> publish() throws IOException, DataverseException {
        Path path = buildPath(targetBase, persistendId, publish);
        HashMap<String, List<String>> parameters = new HashMap<>();
        parameters.put("persistentId", singletonList(id));
        parameters.put("type", singletonList("major"));
        return httpClientWrapper.postJsonString(path, "", parameters, emptyMap(), DatasetPublicationResult.class);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow
     * multiple values.
     * Replaces existing data.
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata
     *
     * @param s JSON document containing the edits to perform
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> editMetadata(String s) throws IOException, DataverseException {
        return editMetadata(s, true);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow
     * multiple values.
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata
     *
     * @param s       JSON document containing the edits to perform
     * @param replace whether to replace existing values
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> editMetadata(String s, Boolean replace) throws IOException, DataverseException {
        log.trace("ENTER");
        HashMap<String, List<String>> queryParams = new HashMap<>();
        if (replace)
            /*
             * Sic! any value for "replace" is interpreted by Dataverse as "true", even "replace=false"
             * It is by the *absence* of the parameter that replace is set to false.
             */
            queryParams.put("replace", singletonList("true"));
        return putToTarget("editMetadata", s, queryParams, DatasetVersion.class);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet. If `replace` is set to `false`, all specified fields must be either currently empty or allow
     * multiple values.
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata
     *
     * @param fields  list of fields to edit
     * @param replace whether to replace existing values
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> editMetadata(FieldList fields, Boolean replace) throws IOException, DataverseException {
        return editMetadata(httpClientWrapper.writeValueAsString(fields), replace);
    }

    /**
     * Edits the current draft's metadata, adding the fields that do not exist yet.
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#edit-dataset-metadata
     *
     * @param fields list of fields to edit
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> editMetadata(FieldList fields) throws IOException, DataverseException {
        return editMetadata(httpClientWrapper.writeValueAsString(fields), true);
    }

    // TODO https://guides.dataverse.org/en/latest/api/native-api.html#export-metadata-of-a-dataset-in-various-formats
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#schema-org-json-ld
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#view-dataset-files-and-folders-as-a-directory-index
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-all-metadata-blocks-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#list-single-metadata-block-for-a-dataset

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/developers/dataset-semantic-metadata-api.html#add-dataset-metadata
     *
     * @param metadata JSON document describing the metadata
     * @return
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<Object> updateMetadataFromJsonLd(String metadata, boolean replace) throws IOException, DataverseException {
        Map<String, List<String>> queryParams = singletonMap("replace", singletonList((String.valueOf(replace))));
        return httpClientWrapper.putJsonLdString(subPath("metadata"), metadata, params(queryParams), extraHeaders, RoleAssignmentReadOnly.class);
    }

    /**
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset
     *
     * @param s JSON document containing the new metadata
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> updateMetadata(String s) throws IOException, DataverseException {
        // Cheating with endPoint here, because the only version that can be updated is :draft anyway
        return putToTarget("versions/:draft", s, emptyMap(), DatasetVersion.class);
    }

    /**
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#update-metadata-for-a-dataset
     *
     * @param metadataBlocks map of metadata block name to metadata block
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<DatasetVersion> updateMetadata(Map<String, MetadataBlock> metadataBlocks) throws IOException, DataverseException {
        return updateMetadata(httpClientWrapper.writeValueAsString(singletonMap("metadataBlocks", metadataBlocks)));
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-metadata

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-dataset-draft
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#set-citation-date-field-type-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#revert-citation-date-field-type-to-default-for-dataset

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#list-role-assignments-on-a-dataverse-api
     *
     * @return
     */
    public DataverseHttpResponse<List<RoleAssignmentReadOnly>> listRoleAssignments() throws IOException, DataverseException {
        log.trace("ENTER");
        return getUnversionedFromTarget("assignments", List.class, RoleAssignmentReadOnly.class);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#assign-a-new-role-on-a-dataset
     *
     * @param roleAssignment JSON document describing the assignment
     * @return
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<RoleAssignmentReadOnly> assignRole(String roleAssignment) throws IOException, DataverseException {
        return httpClientWrapper.postJsonString(subPath("assignments"), roleAssignment, params(emptyMap()), extraHeaders, RoleAssignmentReadOnly.class);
    }


    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-role-assignment-from-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#create-a-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-the-private-url-for-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-the-private-url-from-a-dataset

    /**
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset
     *
     * @param file the file to add
     * @param metadata json document with the file metadata
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<FileList> addFile(Path file, String metadata) throws IOException, DataverseException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        Optional.ofNullable(file).ifPresent(f -> builder.addPart("file", new FileBody(f.toFile(), ContentType.APPLICATION_OCTET_STREAM, f.getFileName().toString())));
        Optional.ofNullable(metadata).ifPresent(m -> builder.addPart("jsonData", new StringBody(m, ContentType.APPLICATION_JSON)));
        return httpClientWrapper.post(subPath("add"), builder.build(), params(emptyMap()), extraHeaders, FileList.class);
    }

    /**
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset
     *
     * @param file the file to add
     * @param fileMeta json document with the file metadata
     * @return DatasetVersion
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<FileList> addFile(Path file, FileMeta fileMeta) throws IOException, DataverseException {
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
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#set-an-embargo-on-files-in-a-dataset
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#remove-an-embargo-on-files-in-a-dataset

    /*
     * Helper methods
     */
    private <D> DataverseHttpResponse<D> getVersionedFromTarget(String endPoint, String version, Class<?>... outputClass) throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(versionedSubPath(endPoint, version), params(emptyMap()), extraHeaders, outputClass);
    }

    private <D> DataverseHttpResponse<D> getUnversionedFromTarget(String endPoint, Map<String, List<String>> queryParams, Class<?>... outputClass)
        throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.get(subPath(endPoint), params(queryParams), extraHeaders, outputClass);
    }

    private <D> DataverseHttpResponse<D> getUnversionedFromTarget(String endPoint, Class<?>... outputClass) throws IOException, DataverseException {
        return getUnversionedFromTarget(endPoint, emptyMap(), outputClass);
    }

    private <D> DataverseResponse<D> putToTarget(String endPoint, String body, Map<String, List<String>> queryParams, Class<?>... outputClass)
        throws IOException, DataverseException {
        log.trace("ENTER");
        return httpClientWrapper.putJsonString(subPath(endPoint), body, params(queryParams), extraHeaders, outputClass);
    }

    private Map<String, List<String>> params(Map<String, List<String>> queryParams) {
        if (!isPersistentId)
            return queryParams;
        HashMap<String, List<String>> parameters = new HashMap<>();
        parameters.put("persistentId", singletonList(id));
        parameters.putAll(queryParams);
        return parameters;
    }

    private Path subPath(String endPoint) {
        if (isPersistentId)
            return buildPath(targetBase, persistendId, endPoint);
        else
            return buildPath(targetBase, id, endPoint);
    }

    private Path versionedSubPath(String endPoint, String version) {
        if (isPersistentId)
            return buildPath(targetBase, persistendId, "versions", version, endPoint);
        else
            return buildPath(targetBase, id, "versions", version, endPoint);
    }

    /**
     * See [Dataverse API Guide] and [code example]
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/native-api.html#dataset-locks
     * [code example]: https://github.com/DANS-KNAW/dans-dataverse-client-lib/blob/master/examples/src/main/java/nl/knaw/dans/lib/dataverse/example/DatasetGetLocks.java
     */
    public DataverseResponse<List<Lock>> getLocks() throws IOException, DataverseException {
        log.trace("getting locks from Dataverse");
        return getUnversionedFromTarget("locks", List.class, Lock.class);
    }

    /**
     * Utility function that lets you wait until all locks are cleared before proceeding. Unlike most other functions in this library, this does not correspond directly with an
     * API call. Rather the {@link #getLocks()} call is done repeatedly to check if the locks have been cleared. Note that in scenarios where concurrent processes might access the
     * same dataset it is not guaranteed that the locks, once cleared, stay that way.
     *
     * @param maxNumberOfRetries     the maximum number the check for unlock is made, defaults to [[awaitLockStateMaxNumberOfRetries]]
     * @param waitTimeInMilliseconds the time between tries, defaults to [[awaitLockStateMillisecondsBetweenRetries]]
     */
    public void awaitUnlock(int maxNumberOfRetries, int waitTimeInMilliseconds) throws IOException, DataverseException {
        log.trace(String.format("awaitUnlock: maxNumberOfRetries %d, waitTimeInMilliseconds %d", maxNumberOfRetries, waitTimeInMilliseconds));
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
     * Utility function that lets you wait until a specified lock type is set. Unlike most other functions in this library, this does not correspond directly with an API call.
     * Rather the {@link #getLocks()} call is done repeatedly to check if the locks has been set. A use case is when an http/sr workflow wants to make sure that a dataset has been
     * locked on its behalf, so that it can be sure to have exclusive access via its invocation ID.
     *
     * @param lockType               the lock type to wait for
     * @param maxNumberOfRetries     the maximum number the check for unlock is made, defaults to #awawaitLockStateMaxNumberOfRetries
     * @param waitTimeInMilliseconds the time between tries, defaults to [[awaitLockStateMillisecondsBetweenRetries]]
     */
    public void awaitLock(String lockType, int maxNumberOfRetries, int waitTimeInMilliseconds) throws IOException, DataverseException {
        log.trace(String.format("awaitLock: lockType %s, maxNumberOfRetries %d, waitTimeInMilliseconds %d", lockType, maxNumberOfRetries, waitTimeInMilliseconds));
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
