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
package nl.knaw.dans.lib.dataverse.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.knaw.dans.lib.dataverse.model.dataset.RetentionPeriod;

import java.util.ArrayList;
import java.util.List;

// Ignoring dataTables and varGroups, those have to do with ingesting tabular data
// Ignoring isPartOf, has to specified as option in the API call, which we don't do
//
// The optional "md5" property is redundant and only there for backward compatibility.
@Data
@JsonIgnoreProperties({ "md5", "dataTables", "varGroups", "isPartOf"}) 
public class DataFile {
    // Note that the following fields should align with those 
    // in dataverse src/main/java/edu/harvard/iq/dataverse/util/json/JsonPrinter.java
    private int id;
    private String persistentId;
    private String pidURL;
    private String filename;
    private String contentType;
    private String friendlyType;
    private long filesize;
    private String description;
    private List<String> categories = new ArrayList<>();
    private Embargo embargo;
    private RetentionPeriod retention;
    private String storageIdentifier;
    private String originalFileFormat;
    private String originalFormatLabel;
    private Long originalFileSize;
    private String originalFileName;
    @JsonProperty("UNF")
    private String unf;
    private int rootDataFileId;
    private int previousDataFileId;
    // md5 is ignored!
    private Checksum checksum;
    private Boolean tabularData;
    private List<String> tabularTags = new ArrayList<>();
    private String creationDate;
    private String  publicationDate;
    private Boolean fileAccessRequest;
    private Boolean restricted; // Could be ignored, but simple to add
    private Long fileMetadataId; // Could be ignored, but simple to add
    // dataTables is ignored!
    // varGroups is ignored!
    // isPartOf is ignored!
}
