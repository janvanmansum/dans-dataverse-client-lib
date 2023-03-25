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
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.user.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AdminListSingleUser extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String userId = args[0];
        DataverseResponse<AuthenticatedUser> r = client.admin().listSingleUser(userId);
        log.info("For exceptions on Admin examples see 'unblockKey' in dataverse.properties.template");
        log.info(r.getEnvelopeAsJson().toPrettyString());
        log.info("requested Id: " + userId);
        log.info("Id: " + r.getData().getId());
        log.info("DisplayName: " + r.getData().getDisplayName());
        log.info("one-liners showing differences");
        log.info("model deserialized: " + mapper.writeValueAsString(r.getEnvelope()));
        log.info("original response:  " + r.getEnvelopeAsString());
    }
}
