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

import nl.knaw.dans.lib.dataverse.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicensesGetSingle extends ExampleBase {
    private static final Logger log = LoggerFactory.getLogger(DataversePublish.class);

    public static void main(String[] args) throws Exception {
        log.info("--- BEGIN JSON OBJECT ---");
        var id = args.length > 1 ? Long.parseLong(args[1]) : 1;
        var response = client.license().getLicenseById(id);
        log.info("--- END JSON OBJECT ---");
        log.info("License: {}", response.getData());
    }
}
