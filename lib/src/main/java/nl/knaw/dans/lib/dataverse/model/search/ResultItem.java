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

import java.net.URI;
import java.util.List;
import java.util.Map;

// Mix between snake_case and camelCase, so we need to specify per field what name conversion strategy to use for (de)serialization
public class ResultItem {
    private String type;
    private String name;
    private URI url;
    private String globalId;
    private String description;
    private String publishedAt; // TODO: use timestamp class
    private String publisher;
    private String citationHtml;
    private String identifierOfDataverse;
    private String nameOfDataverse;
    private String citation;
    private String storageIdentifier;
    private List<String> subjects;
    private int fileCount;
    private int versionId;
    private String versionState;
    private int majorVersion;
    private int minorVersion;
    private String createdAt; // TODO: use timestamp class
    private String updatedAt; // TODO: use timestamp class
    private List<Contact> contacts;
    private List<String> authors;
    private List<Map<String, String>> publications; // TODO: this field seems to appear when there exists a draft version.

    public ResultItem() {
    }

    public ResultItem(String type, String name, URI url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    @JsonProperty("global_id")
    public String getGlobalId() {
        return globalId;
    }

    @JsonProperty("global_id")
    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("published_at")
    public String getPublishedAt() {
        return publishedAt;
    }

    @JsonProperty("published_at")
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCitationHtml() {
        return citationHtml;
    }

    public void setCitationHtml(String citationHtml) {
        this.citationHtml = citationHtml;
    }

    @JsonProperty("identifier_of_dataverse")
    public String getIdentifierOfDataverse() {
        return identifierOfDataverse;
    }

    @JsonProperty("identifier_of_dataverse")
    public void setIdentifierOfDataverse(String identifierOfDataverse) {
        this.identifierOfDataverse = identifierOfDataverse;
    }

    @JsonProperty("name_of_dataverse")
    public String getNameOfDataverse() {
        return nameOfDataverse;
    }

    @JsonProperty("name_of_dataverse")
    public void setNameOfDataverse(String nameOfDataverse) {
        this.nameOfDataverse = nameOfDataverse;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getStorageIdentifier() {
        return storageIdentifier;
    }

    public void setStorageIdentifier(String storageIdentifier) {
        this.storageIdentifier = storageIdentifier;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getVersionState() {
        return versionState;
    }

    public void setVersionState(String versionState) {
        this.versionState = versionState;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<Map<String, String>> getPublications() {
        return publications;
    }

    public void setPublications(List<Map<String, String>> publications) {
        this.publications = publications;
    }
}
