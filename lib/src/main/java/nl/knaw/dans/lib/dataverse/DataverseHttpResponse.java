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
import lombok.SneakyThrows;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataverseHttpResponse<D> extends DataverseResponse<D> {
    private final HttpResponse httpResponse;
    private final org.apache.hc.core5.http.HttpResponse httpResponse5;

    protected DataverseHttpResponse(HttpResponse httpResponse, ObjectMapper customMapper, Class<?>... dataClass) throws IOException {
        super(EntityUtils.toString(httpResponse.getEntity()), customMapper, dataClass);
        if (dataClass.length > 2)
            throw new IllegalArgumentException("Currently no more than one nested parameter type supported");
        this.httpResponse = httpResponse;
        this.httpResponse5 = null;
    }

    @SneakyThrows
    DataverseHttpResponse(DispatchResult dispatchResult, ObjectMapper customMapper, Class<?>... dataClass) throws IOException {
        super(dispatchResult.getBody(), customMapper, dataClass);
        this.httpResponse5 = dispatchResult.getResponse();
        this.httpResponse = null;
    }


    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public org.apache.hc.core5.http.HttpResponse getHttpResponse5() {
        return httpResponse5;
    }
}
