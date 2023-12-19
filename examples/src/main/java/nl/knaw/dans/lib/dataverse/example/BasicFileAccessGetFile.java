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
package nl.knaw.dans.lib.dataverse.example;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.GetFileRange;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class BasicFileAccessGetFile extends ExampleBase {

    public static void main(String[] args) throws Exception {
        int id = Integer.parseInt(args[0]);
        Path dest = Paths.get(args[1]);
        GetFileRange range = null;
        if (args.length > 3) {
            range = new GetFileRange(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        // TODO: test GetFileOptions
        client.basicFileAccess(id).getFile(range, r -> {
            if (List.of(200, 206).contains(r.getCode())) {
                FileUtils.copyInputStreamToFile(r.getEntity().getContent(), dest.toFile());
            }
            else {
                throw new RuntimeException("Failed to get file: " + r.getCode() + " " + r.getReasonPhrase());
            }
            return r.getCode();
        });
    }
}

