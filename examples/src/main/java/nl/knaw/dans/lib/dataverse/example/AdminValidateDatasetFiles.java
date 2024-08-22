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
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.DataverseResponseWithoutEnvelope;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.DatasetFileValidationResultList;

import java.util.Map;

@Slf4j
public class AdminValidateDatasetFiles extends ExampleBase {

    public static void main(String[] args) throws Exception {
        var id = args[0];
        DataverseResponseWithoutEnvelope<DatasetFileValidationResultList> r;
        // If id is parseable as a number, it is assumed to be a dataset id.
        try {
            r = client.admin().validateDatasetFiles(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            r = client.admin().validateDatasetFiles(id);
        }
        log.info(r.getAsJson().toPrettyString());
        for (var result : r.getBodyAsObject().getDataFiles()) {
            log.info("File: {}", result.getDatafileId());
            log.info("  Storage Identifier: {}", result.getStorageIdentifier());
            log.info("  Status: {}", result.getStatus());
        }
    }
}
