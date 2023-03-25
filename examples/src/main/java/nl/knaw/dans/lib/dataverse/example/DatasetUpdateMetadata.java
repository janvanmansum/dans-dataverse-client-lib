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
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetLatestVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlock;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class DatasetUpdateMetadata extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        var keyMap = new HashMap<String, String>();
        if (args.length > 1) {
            var mdKeyValue = args[1];
            keyMap.put("citation", mdKeyValue);
            System.out.println("Supplied citation metadata key: " + mdKeyValue);
        }

        MetadataBlock citation = new MetadataBlock();
        citation.setDisplayName("Citation Metadata");
        citation.setName("citation");
        citation.setFields(Arrays.asList(
            new PrimitiveSingleValueField("title", "My New Title"),
            new CompoundFieldBuilder("author", true)
                .addSubfield("authorName", "A. Thor")
                .addSubfield("authorAffiliation", "Walhalla")
                .addSubfield("authorEmail", "thor@asgard.no")
                .build(),
            new CompoundFieldBuilder("dsDescription", true)
                .addSubfield("dsDescriptionValue", "My new description value")
                .addSubfield("dsDescriptionDate", "2022-01-01")
                .build(),
            new CompoundFieldBuilder("datasetContact", true)
                .addSubfield("datasetContactName", "NEW CONTACT NAME")
                .addSubfield("datasetContactEmail", "NEW_CONTACT@example.com")
                .build(),
            new ControlledMultiValueField("subject", Collections.singletonList("Chemistry"))
        ));

        // Note that if the dataset is not already in draft state, the draft created here will not be indexed.
        // You may initiate a draft for a new version by making a trivial change to the metadata using the editMetadata API
        try {
            DataverseResponse<DatasetLatestVersion> r = client.dataset(persistentId).getLatestVersion();
            DatasetVersion latest = r.getData().getLatestVersion();
            latest.setTermsOfAccess("Some new terms. Pray I don't alter them any further.");
            latest.setFiles(Collections.emptyList());
            latest.setMetadataBlocks(Collections.singletonMap("citation", citation));
            var r2 = client.dataset(persistentId).updateMetadata(latest, keyMap);
            log.info("Response message: {}", r2.getEnvelopeAsJson().toPrettyString());
            log.info("Version number: {}", r2.getData().getVersionNumber());
        }
        catch (DataverseException e) {
            System.out.println(e.getMessage());
        }
    }
}
