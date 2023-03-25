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
import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.util.Collections.emptyMap;

@Slf4j
public class FileApi extends AbstractTargetedApi {

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
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#getting-file-metadata
    // TODO: https://guides.dataverse.org/en/latest/api/native-api.html#adding-file-metadata

    public DataverseHttpResponse<FileList> replaceFile(Path dataFile, FileMeta fileMeta) throws IOException, DataverseException {
        return replaceFile(dataFile, httpClientWrapper.writeValueAsString(fileMeta));
    }

    /**
     * @param dataFile the data file
     * @param fileMeta json containing file metadata
     * @return a file list
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#replacing-files" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<FileList> replaceFile(Path dataFile, String fileMeta) throws IOException, DataverseException {
        if (dataFile == null && fileMeta == null)
            throw new IllegalArgumentException("At least one of file data and file metadata must be provided.");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (dataFile != null) {
            builder.addPart("file", new FileBody(dataFile.toFile(), ContentType.APPLICATION_OCTET_STREAM, dataFile.getFileName().toString()));
        }
        if (fileMeta != null) {
            builder.addPart("jsonData", new StringBody(fileMeta, ContentType.APPLICATION_JSON));
        }
        return httpClientWrapper.post(subPath("replace"), builder.build(), (emptyMap()), new HashMap<>(), FileList.class);
    }

    /**
     * @param fileMeta the file metadata
     * @return hash map
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#updating-file-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<String> updateMetadata(FileMeta fileMeta) throws IOException, DataverseException {
        return updateMetadata(httpClientWrapper.writeValueAsString(fileMeta));
    }

    /**
     * @param fileMeta the file metadata
     * @return hash map
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#updating-file-metadata" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<String> updateMetadata(String fileMeta) throws IOException, DataverseException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("jsonData", new StringBody(fileMeta, ContentType.APPLICATION_JSON));
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
