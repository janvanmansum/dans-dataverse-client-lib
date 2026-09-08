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

class MetadataBlockDefinitionTest extends ModelDatasetMapperFixture {
    private static final Class<MetadataBlockDefinition> classUnderTest = MetadataBlockDefinition.class;

    @Test
    void can_deserialize_metadata_block_definition() throws Exception {
        var def = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(1, def.getId());
        assertEquals("citation", def.getName());
        assertEquals("Citation Metadata", def.getDisplayName());
        assertTrue(def.getDisplayOnCreate());
        assertEquals(3, def.getFields().size());

        var titleField = def.getFields().get("title");
        assertEquals("primitive", titleField.getTypeClass());
        assertFalse(titleField.isMultiple());

        var subjectField = def.getFields().get("subject");
        assertEquals("controlledVocabulary", subjectField.getTypeClass());
        assertTrue(subjectField.isMultiple());
        assertTrue(subjectField.getControlledVocabularyValues().contains("Chemistry"));
        assertTrue(subjectField.getControlledVocabularyValues().contains("Computer and Information Science"));

        var authorField = def.getFields().get("author");
        assertEquals("compound", authorField.getTypeClass());
        assertTrue(authorField.isMultiple());
        assertTrue(authorField.getChildFields().containsKey("authorName"));
    }

    @Test
    void can_round_trip() throws Exception {
        var def = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertNotNull(def);
        assertEquals("citation", def.getName());
    }
}
