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

import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;
import nl.knaw.dans.lib.dataverse.model.search.SearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchApi extends AbstractApi {
    private final Path subPath = Paths.get("api", "search");

    protected SearchApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
    }

    /**
     * See [Dataverse API Guide].
     *
     * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/search.html
     *
     * @param query   search query
     * @param start   offset of search result
     * @param perPage how many results per page
     * @param types   types of results to return
     * @return a search result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseResponse<SearchResult> find(String query, int start, int perPage, List<SearchItemType> types) throws IOException, DataverseException {
        // Map("q" -> query, "start" -> start.toString, "per_page" -> perPage.toString) ++ types.map(v => ("type" -> v)).toMap
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("q", Collections.singletonList(query));
        parameters.put("start", Collections.singletonList(Integer.toString(start)));
        parameters.put("per_page", Collections.singletonList(Integer.toString(perPage)));
        if (!types.isEmpty()) parameters.put("type", types.stream().map(t -> t.toString()).collect(Collectors.toList()));
        return httpClientWrapper.get(subPath, parameters, SearchResult.class);
    }

}
