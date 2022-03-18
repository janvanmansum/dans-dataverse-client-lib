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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class ExampleBase {

    protected static DataverseClient client;
    protected static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration("examples/dataverse.properties");
            DataverseClientConfig config = new DataverseClientConfig(new URI(props.getString("baseUrl")), props.getString("apiToken"), props.getString("unblockKey", null));
            client = new DataverseClient(config);
        }
        catch (ConfigurationException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
