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

import nl.knaw.dans.lib.dataverse.DataverseHttpResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.DataverseDatasetItem;
import nl.knaw.dans.lib.dataverse.model.DataverseItem;
import nl.knaw.dans.lib.dataverse.model.DataverseItemType;
import nl.knaw.dans.lib.dataverse.model.DataverseSubverseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataverseGetContents extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DataverseGetContents.class);

    public static void main(String[] args) throws Exception {
        DataverseHttpResponse<List<DataverseItem>> r = client.dataverse("root").getContents();
        r.getData().forEach(item -> {
            log.info("type: {}", item.getType());
            log.info("id: {}", item.getId());
            if (DataverseItemType.dataverse.equals(item.getType())) {
                DataverseSubverseItem subverseItem = (DataverseSubverseItem) item;
                log.info("title: {}", subverseItem.getTitle());
            }
            else {
                DataverseDatasetItem datasetItem = (DataverseDatasetItem) item;
                log.info("protocol: {}", datasetItem.getProtocol());
                log.info("authority: {}", datasetItem.getAuthority());
                log.info("identifier: {}", datasetItem.getIdentifier());
                log.info("persistent URL: {}", datasetItem.getPersistentUrl());
                log.info("publisher: {}", datasetItem.getPublisher());
                log.info("publication date: {}", datasetItem.getPublicationDate());
                log.info("storage identifier: {}", datasetItem.getStorageIdentifier());
                log.info("metadata language: {}", datasetItem.getMetadataLanguage());
            }
        });

    }
}
