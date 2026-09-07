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
package nl.knaw.dans.lib.dataverse.model.dataset;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetadataBlockTest extends ModelDatasetMapperFixture {
    private static final Class<MetadataBlock> classUnderTest = MetadataBlock.class;

    @Test
    public void canDeserialize() throws Exception {
        MetadataBlock mb = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, mb.getClass());
        assertEquals(1, mb.getId());
        assertEquals("citation", mb.getName());
        assertEquals("Citation Metadata", mb.getDisplayName());
        assertTrue(mb.getDisplayOnCreate());
        List<MetadataField> fields = mb.getFields();
        assertEquals(2, fields.size());
        assertEquals("title", fields.get(0).getTypeName());
        assertEquals("author", fields.get(1).getTypeName());
    }

    @Test
    public void roundTrip() throws Exception {
        MetadataBlock mb = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, mb.getClass());
    }

}