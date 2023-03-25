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
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.SearchOptions;
import nl.knaw.dans.lib.dataverse.model.search.DatasetResultItem;
import nl.knaw.dans.lib.dataverse.model.search.DataverseResultItem;
import nl.knaw.dans.lib.dataverse.model.search.FileResultItem;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SearchIterator extends ExampleBase {
    public static void main(String[] args) throws Exception {
        // Read command line
        String query = args[0];
        int start = args.length > 1 ? Integer.parseInt(args[1]) : 0;
        int perPage = args.length > 2 ? Integer.parseInt(args[2]) : 10;
        List<SearchItemType> types = args.length > 3 ?
            Arrays.asList(args)
                .subList(3, args.length)
                .stream().map(SearchItemType::valueOf)
                .collect(Collectors.toList())
            : Arrays.asList(SearchItemType.dataverse, SearchItemType.dataset, SearchItemType.file);

        // Set search options
        SearchOptions options = new SearchOptions();
        options.setTypes(types);
        options.setStart(start);
        options.setPerPage(perPage);

        // Do search
        Iterator<ResultItem> iterator = client.search().iterator(query, options);

        // Render result
        while (iterator.hasNext()) {
            ResultItem item = iterator.next();
            log.info("NEXT ITEM");
            log.info("Name: {}", item.getName());
            log.info("Type: {}", item.getType());
            log.info("URL: {}", item.getUrl());
            log.info("Description: {}", item.getDescription());

            switch (item.getType()) {
                case dataverse:
                    DataverseResultItem dataverseResultItem = (DataverseResultItem) item;
                    log.info("Identifier: {}", dataverseResultItem.getIdentifier());
                    break;
                case dataset:
                    DatasetResultItem datasetResultItem = (DatasetResultItem) item;
                    log.info("Global ID: {}", datasetResultItem.getGlobalId());
                    break;
                case file:
                    FileResultItem fileResultItem = (FileResultItem) item;
                    log.info("Checksum Type: {}", fileResultItem.getChecksum().getType());
                    log.info("Checksum Value: {}", fileResultItem.getChecksum().getValue());
            }
            log.info("---");
        }
    }
}
