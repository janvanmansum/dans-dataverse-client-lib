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
import nl.knaw.dans.lib.dataverse.CompoundFieldBuilder;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;

import java.util.HashMap;

@Slf4j
public class DatasetDeleteMetadata extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        String description = args[1];

        var keyMap = new HashMap<String, String>();
        if (args.length > 2) {
            var mdKeyValue = args[2];
            keyMap.put("citation", mdKeyValue);
            log.info("Supplied citation metadata key: {}", mdKeyValue);
        }

        FieldList fieldList = new FieldList();
        fieldList.add(new CompoundFieldBuilder("dsDescription", true)
            .addSubfield("dsDescriptionValue", description)
            .build());
        DataverseResponse<DatasetVersion> r = client.dataset(persistentId).deleteMetadata(fieldList, keyMap);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        log.info("Version number: {}", r.getData().getVersionNumber());
    }
}
