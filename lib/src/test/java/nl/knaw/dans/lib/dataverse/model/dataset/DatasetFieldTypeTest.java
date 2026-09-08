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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatasetFieldTypeTest extends ModelDatasetMapperFixture {
    private static final Class<DatasetFieldType> classUnderTest = DatasetFieldType.class;

    @Test
    void can_deserialize_compound_field_type() throws Exception {
        var fieldType = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals("author", fieldType.getName());
        assertEquals("compound", fieldType.getTypeClass());
        assertTrue(fieldType.isMultiple());
        assertEquals(2, fieldType.getChildFields().size());
        assertEquals("primitive", fieldType.getChildFields().get("authorName").getTypeClass());
        assertFalse(fieldType.getChildFields().get("authorName").isMultiple());
    }

    @Test
    void can_round_trip() throws Exception {
        var fieldType = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertNotNull(fieldType);
        assertEquals("author", fieldType.getName());
    }
}
