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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope;

import java.io.IOException;

/**
 * Response from Dataverse.
 *
 * @param <D> the type of the data of the response message envelope, one of the classes in {@link nl.knaw.dans.lib.dataverse.model}
 */

@Slf4j
public class DataverseResponse<D> {
    private final ObjectMapper mapper;

    private final String bodyText;
    private final JavaType dataType;

    protected DataverseResponse(String bodyText, ObjectMapper mapper, Class<?>... dataClass) {
        this.bodyText = bodyText;
        this.mapper = mapper;
        TypeFactory typeFactory = mapper.getTypeFactory();

        switch (dataClass.length) {
            case 0:
                throw new IllegalArgumentException("No parameter type given");
            case 1:
                this.dataType = typeFactory.constructParametricType(DataverseEnvelope.class, dataClass[0]);
                break;
            case 2:
                JavaType inner = typeFactory.constructParametricType(dataClass[0], dataClass[1]);
                this.dataType = typeFactory.constructParametricType(DataverseEnvelope.class, inner);
                break;
            default:
                throw new IllegalArgumentException("Currently no more than one nested parameter type supported");
        }
    }

    // Must be static, otherwise compiler complains about passing result into 'this()' call
    private static JavaType constuctJavaTypeForContainerClass(Class<?> containerClass, Class<?> elementClass, ObjectMapper mapper) {
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType inner = typeFactory.constructParametricType(containerClass, elementClass);
        return typeFactory.constructParametricType(DataverseEnvelope.class, inner);
    }

    /**
     * @return A dataverse envelope
     * @throws com.fasterxml.jackson.core.JsonParseException if body cannot be processed properly as JSON
     */
    public DataverseEnvelope<D> getEnvelope() throws IOException {
        return mapper.readValue(bodyText, dataType);
    }

    /**
     * @return the payload of the envelope directly
     * @throws com.fasterxml.jackson.core.JsonParseException if body cannot be processed properly as JSON
     */
    public D getData() throws IOException {
        return getEnvelope().getData();
    }

    /**
     * @return the envelope as a JSON AST
     * @throws com.fasterxml.jackson.core.JsonParseException if body cannot be processed properly as JSON
     */
    public JsonNode getEnvelopeAsJson() throws IOException {
        return mapper.readTree(bodyText);
    }

    /**
     * @return the body as a String
     */
    public String getEnvelopeAsString() {
        return bodyText;
    }
}
