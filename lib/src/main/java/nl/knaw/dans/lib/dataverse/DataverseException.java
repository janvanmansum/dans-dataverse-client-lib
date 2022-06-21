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

import org.apache.http.HttpResponse;

public class DataverseException extends Exception {
    private final int status;
    private final HttpResponse httpResponse;

    public DataverseException(int status, String msg, HttpResponse httpResponse) {
        super(msg);
        this.status = status;
        this.httpResponse = httpResponse;
    }

    public int getStatus() {
        return status;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    @Override
    public String toString() {
        return "DataverseException{" +
            "status=" + status +
            ", httpResponse=" + httpResponse +
            '}';
    }
}
