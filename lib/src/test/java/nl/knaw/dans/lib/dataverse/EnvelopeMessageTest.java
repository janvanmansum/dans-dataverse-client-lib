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

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope;
import nl.knaw.dans.lib.dataverse.model.ModelFixture;
import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnvelopeMessageTest extends ModelFixture {

    private static class EnvelopeMessage extends DataverseEnvelope<FileList> {
    }

    private static final Class<EnvelopeMessage> wrappedClassUnderTest = EnvelopeMessage.class;
    private final File jsonFile = getTestJsonFileFor(wrappedClassUnderTest);

    @Test
    public void canDeserialize() throws Exception {
        // TODO a DataverseEnvelopeDeserializer?
        //  it should allow message as either a String or a DataMessage as in AddFile.json?
        //  it needs to support the generic data field
        assertThrows(MismatchedInputException.class, () -> mapper.readValue(jsonFile, wrappedClassUnderTest),
            "Cannot construct instance of `nl.knaw.dans.lib.dataverse.model.DataMessage` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('This message is just hypothetical')\n"
                + " at [Source: (File); line: 3, column: 14] (through reference chain: nl.knaw.dans.lib.dataverse.AddFile2Test$AddFile2[\"message\"])");
    }
}
