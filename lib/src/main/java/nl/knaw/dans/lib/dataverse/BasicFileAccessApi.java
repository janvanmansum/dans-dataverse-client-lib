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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Basic file access API end-points.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access" target="_blank">Dataverse documentation</a>
 */
public class BasicFileAccessApi extends AbstractTargetedApi {
    protected BasicFileAccessApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper, id, isPersistentId, null, Paths.get("api/access/datafile"));
    }

    protected BasicFileAccessApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper, id, isPersistentId, invocationId, Paths.get("api/access/datafile"));
    }


    /**
     * @return a HttpResponse object
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access" target="_blank">Dataverse documentation</a>
     */
    public HttpResponse getFile() throws DataverseException, IOException {
        return getFile(null, null);
    }

    /**
     * @param range the range of the file to return, see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#headers" target="_blank">Dataverse documentation</a>
     * @return a HttpResponse object
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access" target="_blank">Dataverse documentation</a>
     */
    public HttpResponse getFile(GetFileRange range) throws DataverseException, IOException {
        return getFile(null, range);
    }

    /**
     * @param options the request options, see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#parameters" target="_blank">Dataverse documentation</a>
     * @return a HttpResponse object
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access" target="_blank">Dataverse documentation</a>
     */
    public HttpResponse getFile(GetFileOptions options) throws DataverseException, IOException {
        return getFile(options, null);
    }

    /**
     * @param options the request options, see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#parameters" target="_blank">Dataverse documentation</a>
     * @param range   the range of the file to return, see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#headers" target="_blank">Dataverse documentation</a>
     * @return a HttpResponse object
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access" target="_blank">Dataverse documentation</a>
     */
    public HttpResponse getFile(GetFileOptions options, GetFileRange range) throws DataverseException, IOException {
        HashMap<String, List<String>> params = new HashMap<>();
        if (options != null) {
            Optional.ofNullable(options.getFormat()).ifPresent(f -> params.put("format", Collections.singletonList(f)));
            if (options.isImageThumb())
                params.put("imageThumb", Collections.singletonList(Integer.toString(options.getImageThumbPixels())));
            if (options.isNoVarHeader())
                params.put("noVarHeader", Collections.singletonList("true"));
        }
        HashMap<String, String> headers = new HashMap<>();
        if (range != null) {
            headers.put("Range", "bytes=" + range.getStart() + "-" + range.getEnd());
        }
        headers.putAll(extraHeaders);
        return httpClientWrapper.get(subPath(""), params, headers);
    }
}
