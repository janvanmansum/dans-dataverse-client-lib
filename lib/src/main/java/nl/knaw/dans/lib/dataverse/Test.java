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
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        JavaType stringType = typeFactory.constructType(String.class);
        JavaType listOfString = typeFactory.constructParametricType(List.class, String.class);
        JavaType t = typeFactory.constructParametricType(TestModel.class, stringType, listOfString);

        TestModel<String, List<String>> m = mapper.readValue(FileUtils.readFileToString(new File("/Users/janm/git/service/dataverse-dans/dans-dataverse-client-lib/testmod.json"), StandardCharsets.UTF_8), t);

        System.out.println(m.getA());
        System.out.println(m.getB().get(0));
    }
}
