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

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import nl.knaw.dans.lib.dataverse.model.search.SearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ToString
public class SearchApi extends AbstractApi {
    private final Path subPath = Paths.get("api", "search");

    SearchApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
    }

    /**
     * A basic search action with default options.
     *
     * @param query search query
     * @return a search result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/search.html" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<SearchResult> find(String query) throws IOException, DataverseException {
        return find(query, new SearchOptions());
    }

    /**
     * A search action with specific {@link SearchOptions}.
     *
     * @param query   search query
     * @param options further options for futher filtering and rendering the results
     * @return a search result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/search.html" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<SearchResult> find(String query, SearchOptions options)
        throws IOException, DataverseException {
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("q", Collections.singletonList(query));
        Optional.ofNullable(options.getTypes()).ifPresent(types -> parameters.put("type", types.stream().map(Enum::toString).collect(Collectors.toList())));
        Optional.ofNullable(options.getFilterQueries()).ifPresent(fqs -> parameters.put("fq", fqs));
        Optional.ofNullable(options.getSubTrees()).ifPresent(subTrees -> parameters.put("subtree", subTrees));
        Optional.ofNullable(options.getSortField()).ifPresent(sf -> parameters.put("sort", Collections.singletonList(sf)));
        Optional.ofNullable(options.getOrder()).ifPresent(order -> parameters.put("order", Collections.singletonList(order.toString())));
        parameters.put("start", Collections.singletonList(Integer.toString(options.getStart())));
        parameters.put("per_page", Collections.singletonList(Integer.toString(options.getPerPage())));
        if (options.isShowRelevance())
            parameters.put("show_relevance", Collections.singletonList(Integer.toString(options.getPerPage())));
        if (options.isShowFacets())
            parameters.put("show_facets", Collections.singletonList(Integer.toString(options.getPerPage())));
        if (options.isShowEntityIds())
            parameters.put("show_entity_ids", Collections.singletonList(Integer.toString(options.getPerPage())));
        return httpClientWrapper.get(subPath, parameters, SearchResult.class);
    }

    /**
     * Returns an iterator to all the results for the specified query and options. The caller is responsible for calling the {@link ResultItem}s to the appropriate subclass.
     *
     * @param query         the query to execute
     * @param searchOptions the options for the query
     * @return an iterator over the results
     */
    public Iterator<ResultItem> iterator(String query, SearchOptions searchOptions) {
        return new ResultItemIterator(this, query, searchOptions);
    }

    /**
     * Returns an iterator to all the results for the specified query and default options. The caller is responsible for calling the {@link ResultItem}s to the appropriate subclass.
     *
     * @param query the query to execute
     * @return an iterator over the results
     */
    public Iterator<ResultItem> iterator(String query) {
        return new ResultItemIterator(this, query, new SearchOptions());
    }

}
