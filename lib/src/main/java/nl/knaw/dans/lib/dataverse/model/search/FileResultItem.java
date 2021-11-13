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

import nl.knaw.dans.lib.dataverse.model.file.Checksum;

public class FileResultItem extends ResultItem {
    private String fileType;
    private String fileContentType;
    private Long sizeInBytes;
    private Checksum checksum;
    private String datasetName;
    private String datasetId;
    private String datasetPersistentId;
    private String datasetCitation;

    public FileResultItem() {
        super(SearchItemType.file);
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetPersistentId() {
        return datasetPersistentId;
    }

    public void setDatasetPersistentId(String datasetPersistentId) {
        this.datasetPersistentId = datasetPersistentId;
    }

    public String getDatasetCitation() {
        return datasetCitation;
    }

    public void setDatasetCitation(String datasetCitation) {
        this.datasetCitation = datasetCitation;
    }
}

