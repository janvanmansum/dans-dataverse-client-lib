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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataverseComplexMessageHttpResponse2<M, D> extends DataverseComplexMessageResponse<M, D> {
    private final HttpResponse httpResponse;

    protected DataverseComplexMessageHttpResponse2(HttpResponse httpResponse, ObjectMapper customMapper, Class<?>... dataClass) throws IOException {
        super(EntityUtils.toString(httpResponse.getEntity()), customMapper, dataClass);
        if (dataClass.length > 2)
            throw new IllegalArgumentException("Currently no more than one nested parameter type supported");
        this.httpResponse = httpResponse;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
