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
package nl.knaw.dans.lib.dataverse.example;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.DataverseHttpResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.DataMessage;

@Slf4j
public class DataverseSetMetadataBlocksRoot extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String alias = args[0];
        boolean isRoot = Boolean.parseBoolean(args[1]);
        log.info("Setting metadata blocks isRoot to: {} for dataverse with alias: {}", isRoot, alias);
        DataverseHttpResponse<DataMessage> r = client.dataverse(alias).setMetadataBlocksRoot(isRoot);
        log.info("Status Line: {} {}", r.getHttpResponse().getCode(), r.getHttpResponse().getReasonPhrase());
        log.info("Message: {}", r.getData().getMessage());
    }
}
