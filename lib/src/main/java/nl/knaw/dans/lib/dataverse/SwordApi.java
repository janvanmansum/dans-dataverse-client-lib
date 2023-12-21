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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class SwordApi extends AbstractApi {

    SwordApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper.sendApiTokenViaBasicAuth());
    }

    /**
     * Deletes a file from the current draft of the dataset.
     *
     * @param databaseId the database ID of the file to delete. To look up use @{link DatasetApi#listFiles}.
     * @return a generic http response
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/sword.html#delete-a-file-by-database-id" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Object> deleteFile(int databaseId) throws IOException, DataverseException {
        Path path = Paths.get("/dvn/api/data-deposit/v1.1/swordv2/edit-media/file/" + databaseId);
        return httpClientWrapper.delete(path);
    }
}
