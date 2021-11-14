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

import java.util.List;

/**
 * Options for searching, such as further narrowing down the result, what to include in the result items and which part of the result to return, and in what order.
 * See [Dataverse API Guide] for details.
 *
 * [Dataverse API Guide]:  * https://guides.dataverse.org/en/latest/api/search.html#parameters
 */
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

    public List<SearchItemType> getTypes() {
        return types;
    }

    public void setTypes(List<SearchItemType> types) {
        this.types = types;
    }

    public List<String> getFilterQueries() {
        return filterQueries;
    }

    public void setFilterQueries(List<String> filterQueries) {
        this.filterQueries = filterQueries;
    }

    public List<String> getSubTrees() {
        return subTrees;
    }

    public void setSubTrees(List<String> subTrees) {
        this.subTrees = subTrees;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isShowRelevance() {
        return showRelevance;
    }

    public void setShowRelevance(boolean showRelevance) {
        this.showRelevance = showRelevance;
    }

    public boolean isShowFacets() {
        return showFacets;
    }

    public void setShowFacets(boolean showFacets) {
        this.showFacets = showFacets;
    }

    public boolean isShowEntityIds() {
        return showEntityIds;
    }

    public void setShowEntityIds(boolean showEntityIds) {
        this.showEntityIds = showEntityIds;
    }
}
