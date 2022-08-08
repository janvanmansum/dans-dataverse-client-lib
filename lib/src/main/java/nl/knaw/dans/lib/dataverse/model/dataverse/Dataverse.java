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

import java.util.List;

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

    public DataverseTheme getTheme() {
        return theme;
    }

    public void setTheme(DataverseTheme theme) {
        this.theme = theme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isPermissionRoot() {
        return permissionRoot;
    }

    public void setPermissionRoot(boolean permissionRoot) {
        this.permissionRoot = permissionRoot;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataverseType getDataverseType() {
        return dataverseType;
    }

    public void setDataverseType(DataverseType dataverseType) {
        this.dataverseType = dataverseType;
    }

    public String getStorageDriverLabel() {
        return storageDriverLabel;
    }

    public void setStorageDriverLabel(String storageDriverLabel) {
        this.storageDriverLabel = storageDriverLabel;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<DataverseContact> getDataverseContacts() {
        return dataverseContacts;
    }

    public void setDataverseContacts(List<DataverseContact> dataverseContacts) {
        this.dataverseContacts = dataverseContacts;
    }
}
