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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseItem;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Object that lets your code talk to a Dataverse server.
 */
public class DataverseClient {

    private final HttpClientWrapper httpClientWrapper;
    private final ObjectMapper mapper;
    private SearchApi searchApi;

    /**
     * Creates a DataverseClient.
     *
     * @param config configuration for this DataverseClient
     */
    public DataverseClient(DataverseClientConfig config) {
        this(config, HttpClients.createDefault(), null);
    }

    /**
     * Creates a DataverseClient with a custom HttpClient.
     *
     * @param config     configuration for this DataverseClient
     * @param httpClient the `org.apache.http.client.HttpClient` to use when interacting with Dataverse
     */
    public DataverseClient(DataverseClientConfig config, HttpClient httpClient, ObjectMapper objectMapper) {
        if (objectMapper == null)
            mapper = new ObjectMapper();
        else
            mapper = objectMapper;
        SimpleModule module = new SimpleModule();
        // TODO: How to get rid of type warnings?
        // TODO: Create proper Jackson module for this?
        // TODO: Make use of this deserializer optional through system property?
        module.addDeserializer(MetadataField.class, new MetadataFieldDeserializer());
        module.addDeserializer(DataverseItem.class, new DataverseItemDeserializer());
        module.addDeserializer(ResultItem.class, new ResultItemDeserializer(mapper));
        mapper.registerModule(module);
        this.httpClientWrapper = new HttpClientWrapper(config, httpClient, mapper);
    }

    public WorkflowsApi workflows() {
        return new WorkflowsApi(httpClientWrapper);
    }

    public DatasetApi dataset(String pid) {
        return new DatasetApi(httpClientWrapper, pid, true);
    }

    public DataverseApi dataverse(String alias) {
        return new DataverseApi(httpClientWrapper, alias);
    }

    public SearchApi search() {
        if (searchApi == null)
            searchApi = new SearchApi(httpClientWrapper);
        return searchApi;
    }
}
