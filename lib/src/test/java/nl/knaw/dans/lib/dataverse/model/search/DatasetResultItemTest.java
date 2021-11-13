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

public class DatasetResultItemTest extends ModelSearchFixture{
    private static final Class<?> classUnderTest = DatasetResultItem.class;

    @Test
    public void canDeserialize() throws Exception {
        ResultItem c = mapper.readValue(getTestJsonFileFor(classUnderTest), ResultItem.class);
        assertEquals(classUnderTest, c.getClass());
        assertEquals("Manual test", c.getName());
        DatasetResultItem d = (DatasetResultItem)c;
        assertEquals("doi:10.5072/DAR/NYBJJT", d.getGlobalId());
        assertEquals("Positor01, D", d.getContacts().get(0).getName());
    }

    @Test
    public void roundTrip() throws Exception {
        ResultItem c = roundTrip(getTestJsonFileFor(classUnderTest), ResultItem.class);
        assertEquals(classUnderTest, c.getClass());
    }
}
