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

import java.net.URI;

public class DataverseClientConfig {
    private final URI baseUrl;
    private final String apiToken;
    private final int awaitLockStateMaxNumberOfRetries;
    private final int awaitLockStateMillisecondsBetweenRetries;
    private final String unblockKey;

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl                                  the base URL of the Dataverse server to communicate with
     * @param apiToken                                 the API token used for authorization
     * @param awaitLockStateMaxNumberOfRetries         the maximum number of tries for {@link DatasetApi#awaitLock(String)} API (default 30)
     * @param awaitLockStateMillisecondsBetweenRetries the number or milliseconds to wait between tries for {@link DatasetApi#awaitLock(String)} API (default 500)
     * @param unblockKey                               a key required for admin tasks when not running on localhost
     */
    public DataverseClientConfig(URI baseUrl, String apiToken, int awaitLockStateMaxNumberOfRetries, int awaitLockStateMillisecondsBetweenRetries, String unblockKey) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        this.awaitLockStateMaxNumberOfRetries = awaitLockStateMaxNumberOfRetries;
        this.awaitLockStateMillisecondsBetweenRetries = awaitLockStateMillisecondsBetweenRetries;
        this.unblockKey = unblockKey;
    }

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl    the base URL of the Dataverse server to communicate with
     * @param apiToken   the API token used for authorization
     * @param unblockKey a key required for admin tasks when not running on localhost
     */
    public DataverseClientConfig(URI baseUrl, String apiToken, String unblockKey) {
        this(baseUrl, apiToken, 30, 500, unblockKey);
    }

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl  the base URL of the Dataverse server to communicate with
     * @param apiToken the API token used for authorization
     */
    public DataverseClientConfig(URI baseUrl, String apiToken) {
        this(baseUrl, apiToken, 30, 500, null);
    }

    /**
     * Configuration settings for the {@link DataverseClient}. No API token is specified, so the client will only able to access endpoints that require no account.
     *
     * @param baseUrl the base URL of the Dataverse server to communicate with
     */
    public DataverseClientConfig(URI baseUrl) {
        this(baseUrl, null);
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    int getAwaitLockStateMaxNumberOfRetries() {
        return awaitLockStateMaxNumberOfRetries;
    }

    int getAwaitLockStateMillisecondsBetweenRetries() {
        return awaitLockStateMillisecondsBetweenRetries;
    }

    public String getUnblockKey() {
        return unblockKey;
    }
}
