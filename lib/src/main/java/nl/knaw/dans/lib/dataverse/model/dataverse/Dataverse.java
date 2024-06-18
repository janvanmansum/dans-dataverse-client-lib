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
package nl.knaw.dans.lib.dataverse.model.dataverse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// Ignoring isPartOf, has to specified as option in the API call, which we don't do yet
@Data
@JsonIgnoreProperties({"isPartOf"})
public class Dataverse {
    private int id;
    private int ownerId;
    private String name;
    private String alias;
    private boolean permissionRoot;
    private String affiliation;
    private String description;
    private DataverseType dataverseType;
    private String storageDriverLabel;
    private String creationDate;
    private DataverseTheme theme;
    private List<DataverseContact> dataverseContacts;
    // isPartOf ignore!
    private boolean filePIDsEnabled;
    @JsonProperty("isReleased")
    private boolean isReleased;
}
