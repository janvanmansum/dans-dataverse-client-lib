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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.file.Checksum;
import nl.knaw.dans.lib.dataverse.model.search.Contact;
import nl.knaw.dans.lib.dataverse.model.search.DatasetResultItem;
import nl.knaw.dans.lib.dataverse.model.search.DataverseResultItem;
import nl.knaw.dans.lib.dataverse.model.search.FileResultItem;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResultItemDeserializer extends StdDeserializer {
    private ObjectMapper mapper;

    public ResultItemDeserializer(ObjectMapper mapper) {
        this(null, mapper);
    }

    protected ResultItemDeserializer(Class<?> vc, ObjectMapper mapper) {

        super(vc);
        this.mapper = mapper;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String type = node.get("type").asText();

        if ("dataverse".equalsIgnoreCase(type)) {
            DataverseResultItem resultItem = new DataverseResultItem();
            setCommonFields(node, resultItem);
            resultItem.setIdentifier(getStringValue(node, "identifier"));
            return resultItem;
        }
        else if ("dataset".equalsIgnoreCase(type)) {
            DatasetResultItem resultItem = new DatasetResultItem();
            setCommonFields(node, resultItem);
            resultItem.setGlobalId(getStringValue(node, "global_id"));
            resultItem.setPublisher(getStringValue(node, "publisher"));
            resultItem.setCitationHtml(getStringValue(node, "citationHtml"));
            resultItem.setIdentifierOfDataverse(getStringValue(node, "identifier_of_dataverse"));
            resultItem.setNameOfDataverse(getStringValue(node, "name_of_dataverse"));
            resultItem.setCitation(getStringValue(node, "citation"));
            resultItem.setStorageIdentifier(getStringValue(node, "storageIdentifier"));
            resultItem.setSubjects(getListValue(node, "subjects", String.class));
            resultItem.setFileCount(getIntegerValue(node, "fileCount"));
            resultItem.setVersionId(getIntegerValue(node, "versionId"));
            resultItem.setVersionState(getStringValue(node, "versionState"));
            resultItem.setMajorVersion(getIntegerValue(node, "majorVersion"));
            resultItem.setMinorVersion(getIntegerValue(node, "minorVersion"));
            resultItem.setCreatedAt(getStringValue(node, "created_at"));
            resultItem.setUpdatedAt(getStringValue(node, "updated_at"));
            resultItem.setContacts(getListValue(node, "contacts", Contact.class));
            resultItem.setAuthors(getListValue(node, "authors", String.class));
            resultItem.setPublications(getListOfObjects(node, "publications"));
            return resultItem;
        }
        else if ("file".equalsIgnoreCase(type)) {
            FileResultItem resultItem = new FileResultItem();
            setCommonFields(node, resultItem);
            resultItem.setFileType(getStringValue(node, "file_type"));
            resultItem.setFileContentType(getStringValue(node, "file_content_type"));
            resultItem.setSizeInBytes(mapper.treeToValue(node.get("size_in_bytes"), Long.class));
            resultItem.setChecksum(mapper.treeToValue(node.get("checksum"), Checksum.class));
            resultItem.setDatasetName(getStringValue(node, "dataset_name"));
            resultItem.setDatasetId(getStringValue(node, "dataset_id"));
            resultItem.setDatasetPersistentId(getStringValue(node, "dataset_persistent_id"));
            resultItem.setDatasetCitation(getStringValue(node, "dataset_citation"));
            return resultItem;
        }
        else {
            throw new IllegalArgumentException("Cannot this type of ResultItem: " + type);
        }
    }

    private void setCommonFields(JsonNode node, ResultItem resultItem) throws JsonProcessingException {
        resultItem.setName(getStringValue(node, "name"));
        resultItem.setUrl(getUriValue(node, "url"));
        resultItem.setDescription(getStringValue(node, "description"));
        resultItem.setPublishedAt(getStringValue(node, "published_at"));
    }

    private String getStringValue(JsonNode node, String name) throws JsonProcessingException {
        return mapper.treeToValue(node.get(name), String.class);
    }

    private URI getUriValue(JsonNode node, String name) throws JsonProcessingException {
        return mapper.treeToValue(node.get(name), URI.class);
    }

    private Integer getIntegerValue(JsonNode node, String name) throws JsonProcessingException {
        return mapper.treeToValue(node.get(name), Integer.class);
    }

    private <E> List<E> getListValue(JsonNode node, String name, Class<E> containedClass) throws JsonProcessingException {
        JavaType t = mapper.getTypeFactory().constructParametricType(List.class, containedClass);
        // writing the node to String and and then parsing it back again seems to be the only way for parametric types.
        if (node.get(name) == null)
            return null;
        else
            return mapper.readValue(node.get(name).toString(), t);
    }

    // Generic getter for "objects", currently only used for the mysterious field "publications"
    private List<Map<Object, Object>> getListOfObjects(JsonNode node, String name) throws JsonProcessingException {
        JavaType t = mapper.getTypeFactory().constructParametricType(List.class, Map.class);
        // writing the node to String and then parsing it back again seems to be the only way for parametric types.
        if (node.get(name) == null)
            return null;
        else
            return mapper.readValue(node.get(name).toString(), t);
    }

}
