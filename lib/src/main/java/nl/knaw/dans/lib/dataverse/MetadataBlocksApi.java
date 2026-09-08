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

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlockDefinition;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metadata blocks API end-points.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#metadata-blocks" target="_blank">Dataverse documentation</a>
 */
@Slf4j
@ToString
public class MetadataBlocksApi extends AbstractApi {
    private final Path targetBase;

    MetadataBlocksApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/metadatablocks/");
    }

    /**
     * Retrieves a list of all metadata blocks.
     *
     * @return a list of metadata blocks
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-all-metadata-blocks" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<MetadataBlockDefinition>> listMetadataBlocks() throws IOException, DataverseException {
        return listMetadataBlocks(false, false);
    }

    /**
     * Retrieves a list of all metadata blocks with an option to filter only those displayed on create.
     *
     * @param onlyDisplayedOnCreate whether to return only metadata blocks displayed on create
     * @return a list of metadata blocks
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-all-metadata-blocks" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<MetadataBlockDefinition>> listMetadataBlocks(boolean onlyDisplayedOnCreate) throws IOException, DataverseException {
        return listMetadataBlocks(onlyDisplayedOnCreate, false);
    }

    /**
     * Retrieves a list of all metadata blocks with options to filter only those displayed on create and to return dataset field types.
     *
     * @param onlyDisplayedOnCreate   whether to return only metadata blocks displayed on create
     * @param returnDatasetFieldTypes whether to include dataset field types
     * @return a list of metadata blocks
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-all-metadata-blocks" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<MetadataBlockDefinition>> listMetadataBlocks(boolean onlyDisplayedOnCreate, boolean returnDatasetFieldTypes)
        throws IOException, DataverseException {
        Map<String, List<String>> parameters = new HashMap<>();
        if (onlyDisplayedOnCreate) {
            parameters.put("onlyDisplayedOnCreate", Collections.singletonList("true"));
        }
        if (returnDatasetFieldTypes) {
            parameters.put("returnDatasetFieldTypes", Collections.singletonList("true"));
        }
        return httpClientWrapper.get(targetBase, parameters, List.class, MetadataBlockDefinition.class);
    }

    /**
     * Retrieves metadata block information by its identifier (name or ID).
     *
     * @param identifier the metadata block identifier (name or ID)
     * @return the metadata block
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-single-metadata-block" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<MetadataBlockDefinition> getMetadataBlock(String identifier) throws IOException, DataverseException {
        return httpClientWrapper.get(buildPath(targetBase, identifier), MetadataBlockDefinition.class);
    }

    /**
     * Retrieves metadata block information by its integer ID.
     *
     * @param id the metadata block ID
     * @return the metadata block
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-single-metadata-block" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<MetadataBlockDefinition> getMetadataBlock(int id) throws IOException, DataverseException {
        return getMetadataBlock(Integer.toString(id));
    }

    /**
     * Retrieves metadata block information by its long ID.
     *
     * @param id the metadata block ID
     * @return the metadata block
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#get-info-about-single-metadata-block" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<MetadataBlockDefinition> getMetadataBlock(long id) throws IOException, DataverseException {
        return getMetadataBlock(Long.toString(id));
    }
}
