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
 * See [Dataverse API Guide].
 *
 * [Dataverse API Guide]: https://guides.dataverse.org/en/latest/api/dataaccess.html#basic-file-access
 */
public class BasicFileAccessApi extends AbstractTargetedApi {
    protected BasicFileAccessApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper, id, isPersistentId, Paths.get("api/access/datafile"));
    }

    public HttpResponse getFile() throws DataverseException, IOException {
        return getFile(null, null);
    }

    public HttpResponse getFile(GetFileRange range) throws DataverseException, IOException {
        return getFile(null, range);
    }

    public HttpResponse getFile(GetFileOptions options) throws DataverseException, IOException {
        return getFile(options, null);
    }

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

        return httpClientWrapper.get(subPath(""), params, headers);
    }
}
