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

@JsonIgnoreProperties({ "md5" }) // The optional "md5" property is redundant and only there for backward compatibility. 
public class DataFile {
  private int   id;
  private String persistentId;
  private String pidURL;
  private String filename;
  private String contentType;
  private long filesize;
  private String description;
  private Embargo embargo;
  private String storageIdentifier;
  private String originalFileFormat;
  private String originalFormatLabel;
  private Long originalFileSize;
  private String originalFileName;
  private String unf;
  private int rootDataFileId;
  private Checksum checksum;
  private String creationDate; // TODO why not a DateTime?
  private int previousDataFileId;

  public int getPreviousDataFileId() {
    return previousDataFileId;
  }

  public void setPreviousDataFileId(int previousDataFileId) {
    this.previousDataFileId = previousDataFileId;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public Checksum getChecksum() {
    return checksum;
  }

  public void setChecksum(Checksum checksum) {
    this.checksum = checksum;
  }

  public Embargo getEmbargo() {
    return embargo;
  }

  public void setEmbargo(Embargo embargo) {
    this.embargo = embargo;
  }

  public int getRootDataFileId() {
    return rootDataFileId;
  }

  public void setRootDataFileId(int rootDataFileId) {
    this.rootDataFileId = rootDataFileId;
  }

  public String getStorageIdentifier() {
    return storageIdentifier;
  }

  public void setStorageIdentifier(String storageIdentifier) {
    this.storageIdentifier = storageIdentifier;
  }

  public long getFilesize() {
    return filesize;
  }

  public void setFilesize(long filesize) {
    this.filesize = filesize;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getPidURL() {
    return pidURL;
  }

  public void setPidURL(String pidURL) {
    this.pidURL = pidURL;
  }

  public String getPersistentId() {
    return persistentId;
  }

  public void setPersistentId(String persistentId) {
    this.persistentId = persistentId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty("UNF")
  public String getUnf() {
    return unf;
  }

  @JsonProperty("UNF")
  public void setUnf(String unf) {
    this.unf = unf;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public void setOriginalFileName(String originalFileName) {
    this.originalFileName = originalFileName;
  }

  public Long getOriginalFileSize() {
    return originalFileSize;
  }

  public void setOriginalFileSize(Long originalFileSize) {
    this.originalFileSize = originalFileSize;
  }

  public String getOriginalFormatLabel() {
    return originalFormatLabel;
  }

  public void setOriginalFormatLabel(String originalFormatLabel) {
    this.originalFormatLabel = originalFormatLabel;
  }

  public String getOriginalFileFormat() {
    return originalFileFormat;
  }

  public void setOriginalFileFormat(String originalFileFormat) {
    this.originalFileFormat = originalFileFormat;
  }
}
