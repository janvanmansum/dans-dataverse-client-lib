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

import nl.knaw.dans.lib.dataverse.model.dataset.CompoundField;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledSingleValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.SingleValueField;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompoundFieldBuilder {
    private final String typeName;
    private final boolean multiple;
    private final List<Map<String, SingleValueField>> values = new LinkedList<>();
    private Map<String, SingleValueField> currentValue = new HashMap<>();

    public CompoundFieldBuilder(String typeName, boolean multiple) {
        this.typeName = typeName;
        this.multiple = multiple;
    }

    public CompoundFieldBuilder nextValue() {
        if (!multiple)
            throw new IllegalStateException("Not a multiple value field");
        values.add(currentValue);
        currentValue = new HashMap<>();
        return this;
    }

    public CompoundFieldBuilder addSubfield(String name, String value) {
        currentValue.put(name, new PrimitiveSingleValueField(name, value));
        return this;
    }

    public CompoundFieldBuilder addControlledSubfield(String name, String value) {
        currentValue.put(name, new ControlledSingleValueField(name, value));
        return this;
    }

    public CompoundField build() {
        values.add(currentValue);
        return new CompoundField(typeName, multiple, values);
    }
}
