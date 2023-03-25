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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import nl.knaw.dans.lib.dataverse.model.search.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@Slf4j
class ResultItemIterator implements Iterator<ResultItem> {
    private final SearchApi searchApi;
    private final String query;
    private final SearchOptions searchOptions;
    private Iterator<ResultItem> currentBatch = Collections.emptyIterator();

    public ResultItemIterator(SearchApi searchApi, String query, SearchOptions searchOptions) {
        this.searchApi = searchApi;
        this.query = query;
        this.searchOptions = searchOptions.copy();
    }

    @Override
    public boolean hasNext() {
        if (!currentBatch.hasNext()) {
            currentBatch = getNextBatch();
            int nextStart = searchOptions.getStart() + searchOptions.getPerPage();
            searchOptions.setStart(nextStart);
        }

        return currentBatch.hasNext();
    }

    @Override
    public ResultItem next() {
        return currentBatch.next();
    }

    private Iterator<ResultItem> getNextBatch() {
        try {
            DataverseResponse<SearchResult> r = searchApi.find(query, searchOptions);
            return new ArrayList<>(r.getData().getItems()).iterator();
        }
        catch (IOException | DataverseException e) {
            throw new IllegalStateException("Cannot retrieve next batch of datasets", e);
        }
    }
}
