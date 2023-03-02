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

import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public class FileApi extends AbstractTargetedApi {

    private static final Logger logger = LoggerFactory.getLogger(DatasetApi.class);

    protected FileApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper, id, isPersistentId, null, Paths.get("api/v1/files/"));
    }

    protected FileApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper, id, isPersistentId, invocationId, Paths.get("api/v1/files/"));
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#restrict-files
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#uningest-a-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#reingest-a-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#redetect-file-type
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#replacing-files
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#getting-file-metadata
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#adding-file-metadata

    /**
     * @param optDataFile     the data file
     * @param optFileMetadata json containing file metadata
     * @return a file list
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#replacing-files" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<FileList> replaceFileItem(Optional<File> optDataFile, Optional<String> optFileMetadata) throws IOException, DataverseException {
        logger.trace("{} {}", optDataFile, optFileMetadata);
        if (!optDataFile.isPresent() && !optFileMetadata.isPresent())
            throw new IllegalArgumentException("At least one of file data and file metadata must be provided.");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        optDataFile.ifPresent(f -> builder.addPart("file", new FileBody(f, ContentType.APPLICATION_OCTET_STREAM, f.getName())));
        optFileMetadata.ifPresent(m -> builder.addPart("jsonData", new InputStreamBody(new ByteArrayInputStream(m.getBytes()), ContentType.APPLICATION_JSON, "jsonData")));
        return httpClientWrapper.post(subPath("replace"), builder.build(), (emptyMap()), new HashMap<>(), FileList.class);
    }

    /**
     * @param json the file metadata
     * @return hash map
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#updating-file-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<String> updateMetadata(String json) throws IOException, DataverseException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("jsonData", json.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON, "jsonData");
        return httpClientWrapper.post(subPath("metadata"), builder.build(), params(emptyMap()), new HashMap<>(), HashMap.class);
    }

    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#editing-variable-level-metadata
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-provenance-json-for-an-uploaded-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#get-provenance-description-for-an-uploaded-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#create-update-provenance-json-and-provide-related-entity-name-for-an-uploaded-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#create-update-provenance-description-for-an-uploaded-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#delete-provenance-json-for-an-uploaded-file
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#datafile-integrity

}
