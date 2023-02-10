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

public class DatasetAwaitUnlock extends ExampleBase {
    private static final Logger log = LoggerFactory.getLogger(DatasetAwaitUnlock.class);

    /**
     * The easiest way to test this manually is to create an InReview lock. Other locks will be released too quickly.
     *
     * 1. Create a dataset.
     * 2. Submit the dataset for review.
     * 3. Log in as a curator or admin.
     * 4. Start the program.
     * 5. Publish the dataset.
     */
    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        int awaitLockStateMaxNumberOfRetries = args.length > 2 ? Integer.parseInt(args[2]) : 10;
        int awaitLockStateMillisecondsBetweenRetries = args.length > 3 ? Integer.parseInt(args[4]) : 2000;
        client.dataset(persistentId).awaitUnlock(awaitLockStateMaxNumberOfRetries, awaitLockStateMillisecondsBetweenRetries);
        log.info("All locks on dataset {} were cleared", persistentId);
    }
}
