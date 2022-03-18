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

import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope;
import nl.knaw.dans.lib.dataverse.model.ModelFixture;
import nl.knaw.dans.lib.dataverse.model.user.AuthenticatedUser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListSingleUserTest extends ModelFixture {

    private static class ListSingleUser extends DataverseEnvelope<AuthenticatedUser> {
    }

    private static final Class<ListSingleUser> wrappedClassUnderTest = ListSingleUser.class;
    private final File jsonFile = getTestJsonFileFor(wrappedClassUnderTest);

    @Test
    public void canDeserialize() throws Exception {
        assertEquals(
                "Dataverse Admin",
                mapper.readValue(jsonFile, wrappedClassUnderTest).getData().getDisplayName()
        );
    }

    @Test
    public void roundTrip() throws Exception {
        assertEquals(
                "Dataverse.org",
                roundTrip(jsonFile, wrappedClassUnderTest).getData().getAffiliation()
        );
    }
}
