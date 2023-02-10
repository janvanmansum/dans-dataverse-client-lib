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
package nl.knaw.dans.lib.dataverse.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatasetResultItem extends ResultItem {

    @JsonProperty("global_id")
    private String globalId;
    private String publisher;
    private String citationHtml;
    @JsonProperty("identifier_of_dataverse")
    private String identifierOfDataverse;
    @JsonProperty("name_of_dataverse")
    private String nameOfDataverse;
    private String citation;
    private String storageIdentifier;
    private List<String> subjects;
    private Integer fileCount;
    private Integer versionId;
    private String versionState;
    private Integer majorVersion;
    private Integer minorVersion;
    private String createdAt;
    private String updatedAt;
    private List<Contact> contacts;
    private List<String> authors;
    private List<Map<Object, Object>> publications;

    public DatasetResultItem() {
        super(SearchItemType.dataset);
    }
}
