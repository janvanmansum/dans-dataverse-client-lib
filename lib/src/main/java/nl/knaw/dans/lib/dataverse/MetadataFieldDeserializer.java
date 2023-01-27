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
import nl.knaw.dans.lib.dataverse.model.dataset.CompoundMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.CompoundSingleValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledSingleValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.SingleValueField;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MetadataFieldDeserializer extends StdDeserializer {

    public MetadataFieldDeserializer() {
        this(null);
    }

    protected MetadataFieldDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return deserializedMetadataField(p.getCodec().readTree(p), false);
    }

    private MetadataField deserializedMetadataField(JsonNode n, boolean subField) {
        String typeName = n.get("typeName").asText();
        String typeClass = n.get("typeClass").asText();
        boolean multiple = n.get("multiple").asBoolean();
        JsonNode valueNode = n.get("value");

        if ("primitive".equals(typeClass)) {
            if (multiple) {
                if (subField) throw new IllegalArgumentException("Compound fields cannot contain multi-value subfields");
                Iterable<JsonNode> jsonNodeIterable = valueNode::elements;
                return new PrimitiveMultiValueField(
                    typeName,
                    StreamSupport.stream(jsonNodeIterable.spliterator(), false)
                        .map(JsonNode::asText).collect(Collectors.toList()));
            }
            else {
                return new PrimitiveSingleValueField(
                    typeClass,
                    typeName,
                    valueNode.asText());
            }
        }
        else if ("controlledVocabulary".equals(typeClass)) {
            if (multiple) {
                if (subField) throw new IllegalArgumentException("Compound fields cannot contain multi-value subfields");
                Iterable<JsonNode> jsonNodeIterable = valueNode::elements;
                return new ControlledMultiValueField(
                    typeName,
                    StreamSupport.stream(jsonNodeIterable.spliterator(), false)
                        .map(JsonNode::asText).collect(Collectors.toList()));
            }
            else {
                return new ControlledSingleValueField(
                    typeName,
                    valueNode.asText());
            }
        }
        else if ("compound".equals(typeClass)) {
            if (subField) throw new IllegalArgumentException("Compound fields cannot contain compound fields as subfields");
            if (multiple) {
                Iterable<JsonNode> jsonNodeIterable = valueNode::elements;
                return new CompoundMultiValueField(
                    typeName,
                    StreamSupport.stream(jsonNodeIterable.spliterator(), false)
                        .map(this::deserializeCompoundFieldValue).collect(Collectors.toList()));
            } else {
                return new CompoundSingleValueField(
                    typeName,
                    deserializeCompoundFieldValue(valueNode));
            }
        }

        return null;
    }

    private Map<String, SingleValueField> deserializeCompoundFieldValue(JsonNode jsonNode) {
        Map<String, SingleValueField> subFields = new HashMap<>();
        for (Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
            String subFieldName = it.next();
            subFields.put(subFieldName, (SingleValueField) deserializedMetadataField(jsonNode.get(subFieldName), true));
        }
        return subFields;
    }

}
