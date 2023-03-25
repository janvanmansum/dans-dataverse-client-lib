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

import lombok.Data;
import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;

import java.util.List;

/**
 * Options for searching, such as further narrowing down the result, what to include in the result items and which part of the result to return, and in what order.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/search.html#parameters" target="_blank">Dataverse documentation</a>
 */
@Data
public class SearchOptions {
    public enum Order {
        asc,
        desc,
    }

    private List<SearchItemType> types;
    private List<String> filterQueries;
    private List<String> subTrees;
    private String sortField;
    private Order order;
    private int perPage = 10;
    private int start = 0;
    private boolean showRelevance;
    private boolean showFacets;
    private boolean showEntityIds;

    public SearchOptions copy() {
        SearchOptions copy = new SearchOptions();
        copy.types = this.types;
        copy.filterQueries = this.filterQueries;
        copy.subTrees = this.subTrees;
        copy.sortField = this.sortField;
        copy.order = this.order;
        copy.perPage = this.perPage;
        copy.start = this.start;
        copy.showRelevance = this.showRelevance;
        copy.showFacets = this.showFacets;
        copy.showEntityIds = this.showEntityIds;
        return copy;
    }

}
