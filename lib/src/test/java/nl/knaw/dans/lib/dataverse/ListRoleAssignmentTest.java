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
import nl.knaw.dans.lib.dataverse.model.RoleAssignmentReadOnly;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListRoleAssignmentTest extends ModelFixture {

    private static final Logger log = LoggerFactory.getLogger(ListRoleAssignmentTest.class);

    private static class ListRoleAssignment extends DataverseEnvelope<List<RoleAssignmentReadOnly>> {
    }

    private static final Class<ListRoleAssignment> wrappedClassUnderTest = ListRoleAssignment.class;
    private final File jsonFile = getTestJsonFileFor(wrappedClassUnderTest);

    @Test
    public void canDeserialize() throws Exception {
        assertEquals(
            ":authenticated-users",
            mapper.readValue(jsonFile, wrappedClassUnderTest).getData().get(2).getAssignee()
        );
    }

    @Test
    public void roundTrip() throws Exception {
        assertEquals(
            "admin",
            roundTrip(jsonFile, wrappedClassUnderTest).getData().get(1).get_roleAlias()
        );
    }
}
