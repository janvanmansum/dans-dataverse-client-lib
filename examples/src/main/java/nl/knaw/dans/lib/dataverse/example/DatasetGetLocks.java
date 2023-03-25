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
import nl.knaw.dans.lib.dataverse.model.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class DatasetGetLocks extends ExampleBase {

    public static void main(String[] args) throws Exception {
        /*
         * Start the example and then do something with the dataset that causes a lock, such as ingesting a
         * tabular file.
         */

        String persistentId = args[0];

        for (int i = 0; i < 300; i += 1) {
            DataverseResponse<List<Lock>> response = client.dataset(persistentId).getLocks();
            List<Lock> locks = response.getData();
            log.debug(String.format("Locks: %s", locks));
            if (!locks.isEmpty())
                log.info(String.format("Dataset is currently locked by: %s", locks));
            Thread.sleep(500);
        }
    }
}
