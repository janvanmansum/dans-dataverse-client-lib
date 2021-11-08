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

import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DatasetGetVersion extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DatasetGetVersion.class);

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        String version = args[1];
        DataverseResponse<DatasetVersion> r = client.dataset(persistentId).getVersion(version);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        log.info("Create Time: {}", r.getData().getCreateTime());
        log.info("Version State: {}", r.getData().getVersionState());
    }
}
