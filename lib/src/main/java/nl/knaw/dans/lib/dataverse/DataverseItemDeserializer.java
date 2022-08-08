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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseDatasetItem;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseItemType;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseSubverseItem;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class DataverseItemDeserializer extends StdDeserializer {

    public DataverseItemDeserializer() {
        this(null);
    }

    public DataverseItemDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String dataverseItemType = node.get("type").asText();
        if (DataverseItemType.dataverse.toString().equals(dataverseItemType)) {
            return new DataverseSubverseItem(node.get("title").asText(), node.get("id").asInt());
        }
        else if (DataverseItemType.dataset.toString().equals(dataverseItemType)) {
            DataverseDatasetItem item = new DataverseDatasetItem();
            Optional.ofNullable(node.get("id")).ifPresent(n -> item.setId(n.asInt()));
            Optional.ofNullable(node.get("persistentUrl")).ifPresent(n -> item.setPersistentUrl(URI.create(n.asText())));
            Optional.ofNullable(node.get("protocol")).ifPresent(n -> item.setProtocol(n.asText()));
            Optional.ofNullable(node.get("authority")).ifPresent(n -> item.setAuthority(n.asText()));
            Optional.ofNullable(node.get("publisher")).ifPresent(n -> item.setPublisher(n.asText()));
            Optional.ofNullable(node.get("publicationDate")).ifPresent(n -> item.setPublicationDate(n.asText()));
            Optional.ofNullable(node.get("storageIdentifier")).ifPresent(n -> item.setStorageIdentifier(n.asText()));
            Optional.ofNullable(node.get("metadataLanguage")).ifPresent(n -> item.setMetadataLanguage(n.asText()));
            return item;
        }
        else {
            throw new IllegalArgumentException("Not a valid dataverse item type: " + dataverseItemType);
        }
    }
}
