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
package nl.knaw.dans.lib.dataverse.example;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.DatasetApi;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class DatasetGetVersion extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        String version = null;
        if (args.length > 1) {
            version = args[1];
        }

        DatasetApi dataset = client.dataset(persistentId);
        DataverseResponse<DatasetVersion> r = version == null ? dataset.getVersion() : dataset.getVersion(version);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        log.info("Create Time: {}", r.getData().getCreateTime());
        log.info("Version State: {}", r.getData().getVersionState());
        log.info("Version UNF: {}", r.getData().getUnf());
    }
}
