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

import java.io.IOException;

/**
 * Response from Dataverse that is <em>not</em> wrapped in an envelope. Normally, Dataverse responses are wrapped in an
 * envelope that contains metadata about the response, such as the status code and the message. In some cases, however,
 * the response is not wrapped in an envelope. This class is used to handle such responses.
 *
 * @param <B> the type of the data of the response body
 */

@Slf4j
public class DataverseResponseWithoutEnvelope<B> {
    private final ObjectMapper mapper;

    private final String bodyText;
    private final JavaType dataType;

    protected DataverseResponseWithoutEnvelope(String bodyText, ObjectMapper mapper, Class<?>... dataClass) {
        this.bodyText = bodyText;
        this.mapper = mapper;
        TypeFactory typeFactory = mapper.getTypeFactory();
        switch (dataClass.length) {
            case 0:
                throw new IllegalArgumentException("No parameter type given");
            case 1:
                this.dataType = typeFactory.constructType(dataClass[0]);
                break;
            case 2:
                this.dataType = typeFactory.constructParametricType(dataClass[0], dataClass[1]);
                break;
            case 3:
                JavaType nested = typeFactory.constructParametricType(dataClass[1], dataClass[2]);
                this.dataType = typeFactory.constructParametricType(dataClass[0], nested);
                break;
            default:
                throw new IllegalArgumentException("Currently no more than two nested parameter types supported");
        }
    }


    /**
     * @return the body as bean of type D
     * @throws com.fasterxml.jackson.core.JsonParseException if body cannot be processed properly as JSON
     */
    public B getBodyAsObject() throws IOException {
        return mapper.readValue(bodyText, dataType);
    }

    /**
     * @return the body as a JsonNode
     * @throws com.fasterxml.jackson.core.JsonParseException if body cannot be processed properly as JSON
     */
    public JsonNode getBodyAsJson() throws IOException {
        return mapper.readTree(bodyText);
    }

    /**
     * @return the body as a String
     */
    public String getBodyAsString() {
        return bodyText;
    }
}
