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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nl.knaw.dans.lib.dataverse.model.dataverse.DataverseItem;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.search.ResultItem;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class MapperFixture {
    private final File testFileDirectory;

    protected MapperFixture(String testFileDirectory) {
        this.testFileDirectory = new File(new File("src/test/resources/"), testFileDirectory);
    }

    protected ObjectMapper mapper;

    @BeforeEach
    public void beforeEach() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MetadataField.class, new MetadataFieldDeserializer());
        module.addDeserializer(DataverseItem.class, new DataverseItemDeserializer());
        module.addDeserializer(ResultItem.class, new ResultItemDeserializer(mapper));
        mapper.registerModule(module);
    }

    /**
     * Reads reads a string as a JSON object (deserializes it), then writes it out as a string and reads it back in again. This verifies that what is written out during serialization is still
     * readable. For example if the serialization is customized to write out dates in a certain format, this verifies that that format is understandable for the deserializer.
     *
     * @param <T>      the type of object to produce when reading the string
     * @param testFile the test file containing the original input
     * @param c        Class object of type T
     * @return the final result after reading the seconding time
     * @throws Exception should not happen, but since this is a test, we don't want to handle exceptions
     */
    protected <T> T roundTrip(File testFile, Class<T> c) throws Exception {
        return mapper.readValue(
            mapper.writeValueAsString(
                mapper.readValue(testFile, c)),
            c);
    }

    protected <T> T roundTrip(File testFile, JavaType t) throws Exception {
        return mapper.readValue(
            mapper.writeValueAsString(
                mapper.readValue(testFile, t)),
            t);
    }

    protected File getTestJsonFileFor(Class<?> classUnderTest) {
        return new File(testFileDirectory, classUnderTest.getSimpleName() + ".json");
    }

    protected File getTestJsonFileFor(Class<?> classUnderTest, String suffix) {
        return new File(testFileDirectory, classUnderTest.getSimpleName() + "-" + suffix + ".json");
    }
}
