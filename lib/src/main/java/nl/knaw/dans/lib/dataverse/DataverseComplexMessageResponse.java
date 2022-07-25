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
import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Response from Dataverse. A typical response from Dataverse is a JSON document with the following format:
 *
 * <!-- @formatter:off -->
 * ```json
 *  {
 *    "status": "OK",
 *    "data": {
 *        "myfield": "myvalue"
 *    }
 *  }
 * ```
 * <!-- @formatter:on -->
 *
 * {@link nl.knaw.dans.lib.dataverse.model}.
 *
 * @param <M> the type of message in the response message envelope
 * @param <D> the type of the data of the response message envelope
 */
public class DataverseComplexMessageResponse<M, D> {
    private static final Logger log = LoggerFactory.getLogger(DataverseComplexMessageResponse.class);
    private final ObjectMapper mapper;

    private final String bodyText;
    private final JavaType dataType;

    protected DataverseComplexMessageResponse(String bodyText, ObjectMapper mapper, Class<?>... typeParameterClasses) {
        log.trace("ENTER");
        log.trace(bodyText);
        this.bodyText = bodyText;
        this.mapper = mapper;
        if (typeParameterClasses.length < 2 || typeParameterClasses.length > 3) {
            throw new IllegalArgumentException("typeParmetersClasses must be [M, D] or [M, D (container type), D (element type)]");
        }

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType mType = typeFactory.constructType(typeParameterClasses[0]);
        if (typeParameterClasses.length == 2) {
            this.dataType = typeFactory.constructParametricType(DataverseEnvelope2.class, mType, typeFactory.constructType(typeParameterClasses[1]));
        }
        else // length == 3
        {
            JavaType dType = typeFactory.constructParametricType(typeParameterClasses[1], typeParameterClasses[2]);
            this.dataType = typeFactory.constructParametricType(DataverseEnvelope2.class, mType, dType);
        }

    }

    /**
     * @return A dataverse envelope
     * @throws IOException if body cannot be processed properly as JSON
     */
    public DataverseEnvelope2<M, D> getEnvelope() throws IOException {
        return mapper.readValue(bodyText, dataType);
    }

    /**
     * @return the payload of the envelope directly
     * @throws IOException if body cannot be processed properly as JSON
     */
    public D getData() throws IOException {
        return getEnvelope().getData();
    }

    /**
     * @return the message of the envelope directly, if present
     * @throws IOException if body cannot be processed properly as JSON
     */
    public M getMessage() throws IOException {
        return getEnvelope().getMessage();
    }

    /**
     * @return the envelope as a JSON AST
     * @throws IOException if body cannot be processed properly as JSON
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
