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

class ControlledSingleValueFieldTest extends ModelDatasetMapperFixture {
    private static final Class<?> classUnderTest = ControlledSingleValueField.class;

    @Test
    public void canDeserialize() throws Exception {
        MetadataField f = mapper.readValue(getTestJsonFileFor(classUnderTest), MetadataField.class);
        assertEquals(classUnderTest, f.getClass());
        ControlledSingleValueField csvf = (ControlledSingleValueField) f;
        assertEquals("Test", csvf.getValue());
    }

    @Test
    public void roundTrip() throws Exception {
        MetadataField f = roundTrip(getTestJsonFileFor(classUnderTest), MetadataField.class);
        assertEquals(classUnderTest, f.getClass());
    }
}