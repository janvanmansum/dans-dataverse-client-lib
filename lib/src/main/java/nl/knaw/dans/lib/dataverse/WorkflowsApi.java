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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.workflow.ResumeMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class WorkflowsApi extends AbstractApi {

    private static final Path subPath = Paths.get("api/workflows/");

    WorkflowsApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
    }

    public DataverseHttpResponse<Object> resume(String invocationId, ResumeMessage resumeMessage) throws IOException, DataverseException {
        return httpClientWrapper.postModelObjectAsJson(subPath.resolve(invocationId), resumeMessage, Object.class);
    }

}
