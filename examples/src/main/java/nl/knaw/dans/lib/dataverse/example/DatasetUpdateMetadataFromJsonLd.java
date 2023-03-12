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

import nl.knaw.dans.lib.dataverse.CompoundFieldBuilder;
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlock;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DatasetUpdateMetadataFromJsonLd extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DatasetUpdateMetadataFromJsonLd.class);

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];

        var dvCore = "https://dataverse.org/schema/core#";
        Map<String, Object> fileTermsOfAccessValue = new HashMap<>();

        fileTermsOfAccessValue.put(dvCore + "termsOfAccess", "N/a");
        fileTermsOfAccessValue.put(dvCore + "fileRequestAccess", false);

        var jsonLd = mapper.writeValueAsString(Collections.singletonMap(dvCore + "fileTermsOfAccess", fileTermsOfAccessValue));
        System.out.println(jsonLd);

        try {

            DataverseResponse<Object> r = client.dataset(persistentId)
                .updateMetadataFromJsonLd(jsonLd, true);
            log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        }
        catch (DataverseException e) {
            System.out.println(e.getMessage());
        }
    }
}
