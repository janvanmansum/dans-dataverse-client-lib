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

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SwordApi extends AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(SwordApi.class);

    protected SwordApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper.sendApiTokenViaBasicAuth());
        log.trace("ENTER");
    }

    /**
     * Deletes a file from the current draft of the dataset.
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/sword.html#delete-a-file-by-database-id
     *
     * @param databaseId the database ID of the file to delete.
     *                   To look up use DatasetApi#listFiles.
     * @return
     */
    public HttpResponse deleteFile(int databaseId) throws IOException, DataverseException {
        log.trace("ENTER");
        Path path = Paths.get("/dvn/api/data-deposit/v1.1/swordv2/edit-media/file/" + databaseId);
        return httpClientWrapper.delete(path);
    }
}
