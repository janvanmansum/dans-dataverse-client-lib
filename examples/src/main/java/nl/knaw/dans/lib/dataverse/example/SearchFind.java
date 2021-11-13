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
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.search.DatasetResultItem;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;
import nl.knaw.dans.lib.dataverse.model.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFind extends ExampleBase {
    private static final Logger log = LoggerFactory.getLogger(SearchFind.class);

    public static void main(String[] args) throws Exception {
        String query = args[0];
        int start = args.length > 1 ? Integer.parseInt(args[1]) : 0;
        int perPage = args.length > 2 ? Integer.parseInt(args[2]) : 10;
        List<SearchItemType> types = args.length > 3 ?
            Arrays.asList(args)
                .subList(3, args.length)
                .stream().map(SearchItemType::valueOf)
                .collect(Collectors.toList())
            : Collections.singletonList(SearchItemType.dataset);
        DataverseResponse<SearchResult> r = client.search().find(query, start, perPage, types);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        SearchResult searchResult = r.getData();
        for (ResultItem item: searchResult.getItems()) {
            if (SearchItemType.dataset.equals(item.getType())) {
                DatasetResultItem dsItem = (DatasetResultItem) item;
                log.info("Name: {}", dsItem.getName());
                log.info("Type: {}", dsItem.getType());
                log.info("URL: {}", dsItem.getUrl());
                log.info("File Count: {}", dsItem.getFileCount());
            }
        }
    }
}
