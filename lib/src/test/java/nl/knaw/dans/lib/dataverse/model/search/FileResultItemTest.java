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
package nl.knaw.dans.lib.dataverse.model.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileResultItemTest extends ModelSearchFixture{
    private static final Class<?> classUnderTest = FileResultItem.class;

    @Test
    public void canDeserialize() throws Exception {
        ResultItem resultItem = mapper.readValue(getTestJsonFileFor(classUnderTest), ResultItem.class);
        assertEquals(classUnderTest, resultItem.getClass());
        assertEquals("deposit-api.jpg", resultItem.getName());
        FileResultItem fileResultItem = (FileResultItem) resultItem;
        assertEquals("31cc50f4bbef69aed5658b8aef69cc54b67138be", fileResultItem.getChecksum().getValue());
    }

    @Test
    public void roundTrip() throws Exception {
        ResultItem c = roundTrip(getTestJsonFileFor(classUnderTest), ResultItem.class);
        assertEquals(classUnderTest, c.getClass());
    }
}
