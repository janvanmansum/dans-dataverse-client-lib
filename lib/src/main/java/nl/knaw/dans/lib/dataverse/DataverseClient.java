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
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseItem;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;

/**
 * Object that lets your code talk to a Dataverse server.
 */
@Slf4j
public class DataverseClient {
    private final HttpClientWrapper httpClientWrapper;
    private SearchApi searchApi;

    /**
     * Creates a DataverseClient.
     *
     * @param config configuration for this DataverseClient
     */
    public DataverseClient(DataverseClientConfig config) {
        this(config, null, null);
    }

    /**
     * Creates a DataverseClient with a custom HttpClient.
     *
     * @param config       configuration for this DataverseClient
     * @param httpClient5   the HttpClient to use, or null to use a default client
     * @param objectMapper the Jackson object mapper to use, or null to use a default mapper
     */
    public DataverseClient(DataverseClientConfig config, HttpClient httpClient5, ObjectMapper objectMapper) {
        ObjectMapper mapper = objectMapper == null ? new ObjectMapper() : objectMapper;
        SimpleModule module = new SimpleModule();
        // TODO: How to get rid of type warnings?
        // TODO: Create proper Jackson module for this?
        // TODO: Make use of this deserializer optional through system property?
        module.addDeserializer(MetadataField.class, new MetadataFieldDeserializer());
        module.addDeserializer(DataverseItem.class, new DataverseItemDeserializer());
        module.addDeserializer(ResultItem.class, new ResultItemDeserializer(mapper));
        mapper.registerModule(module);
        this.httpClientWrapper = new HttpClientWrapper(config, httpClient5 == null ?  HttpClients.createDefault() : httpClient5
            , mapper);
    }

    public void checkConnection() throws IOException, DataverseException {
        log.debug("Checking if root dataverse can be reached...");
        dataverse("root").view().getData();
        log.debug("OK: root dataverse is reachable.");
    }

    public WorkflowsApi workflows() {
        return new WorkflowsApi(httpClientWrapper);
    }

    public DatasetApi dataset(String pid) {
        return dataset(pid, null);
    }

    public DatasetApi dataset(String pid, String invocationId) {
        return new DatasetApi(httpClientWrapper, pid, true, invocationId);
    }

    public DatasetApi dataset(int pid) {
        return dataset(pid, null);
    }

    public DatasetApi dataset(int pid, String invocationId) {
        return new DatasetApi(httpClientWrapper, Integer.toString(pid), false, invocationId);
    }

    public DataverseApi dataverse(String alias) {
        return new DataverseApi(httpClientWrapper, alias);
    }

    public AdminApi admin() {
        return new AdminApi(httpClientWrapper);
    }

    public SwordApi sword() {
        return new SwordApi(httpClientWrapper);
    }

    public FileApi file(int id) {
        return new FileApi(httpClientWrapper, String.valueOf(id), false);
    }

    public FileApi file(int id, String invocationId) {
        return new FileApi(httpClientWrapper, String.valueOf(id), false, invocationId);
    }

    public DataAccessRequestsApi accessRequests(String pid) {
        return new DataAccessRequestsApi(httpClientWrapper, pid, true);
    }

    public DataAccessRequestsApi accessRequests(int id) {
        return new DataAccessRequestsApi(httpClientWrapper, String.valueOf(id), false);
    }

    public DataAccessRequestsApi accessRequests(String pid, String invocationId) {
        return new DataAccessRequestsApi(httpClientWrapper, pid, true, invocationId);
    }

    public DataAccessRequestsApi accessRequests(int id, String invocationId) {
        return new DataAccessRequestsApi(httpClientWrapper, String.valueOf(id), false, invocationId);
    }

    public BasicFileAccessApi basicFileAccess(String pid) {
        return new BasicFileAccessApi(httpClientWrapper, pid, true);
    }

    public BasicFileAccessApi basicFileAccess(String pid, String invocationId) {
        return new BasicFileAccessApi(httpClientWrapper, pid, true, invocationId);
    }

    public BasicFileAccessApi basicFileAccess(int id) {
        return new BasicFileAccessApi(httpClientWrapper, Integer.toString(id), false);
    }

    public BasicFileAccessApi basicFileAccess(int id, String invocationId) {
        return new BasicFileAccessApi(httpClientWrapper, Integer.toString(id), false, invocationId);
    }

    public SearchApi search() {
        if (searchApi == null)
            searchApi = new SearchApi(httpClientWrapper);
        return searchApi;
    }

    public LicenseApi license() {
        return new LicenseApi(httpClientWrapper);
    }
}
