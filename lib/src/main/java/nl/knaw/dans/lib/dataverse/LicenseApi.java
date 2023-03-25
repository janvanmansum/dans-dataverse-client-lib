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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.DataMessage;
import nl.knaw.dans.lib.dataverse.model.license.CreateLicense;
import nl.knaw.dans.lib.dataverse.model.license.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * License API end-points.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#id188" target="_blank">Dataverse documentation</a>
 */
@Slf4j
public class LicenseApi extends AbstractApi {
    private final Path targetBase;

    protected LicenseApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        log.trace("ENTER");
        this.targetBase = Paths.get("api/licenses/");
    }

    public DataverseHttpResponse<List<License>> getLicenses() throws IOException, DataverseException {
        return httpClientWrapper.get(targetBase, List.class, License.class);
    }

    public DataverseHttpResponse<License> getLicenseById(long id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "" + id);
        return httpClientWrapper.get(path, License.class);
    }

    public DataverseHttpResponse<DataMessage> addLicense(CreateLicense license) throws IOException, DataverseException {
        return httpClientWrapper.postModelObjectAsJson(targetBase, license, DataMessage.class);
    }

    public DataverseHttpResponse<DataMessage> getDefaultLicense() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "default");
        return httpClientWrapper.get(path, DataMessage.class);
    }

    public DataverseHttpResponse<DataMessage> setDefaultLicense(long id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "" + id);
        return httpClientWrapper.putJsonString(path, "", Map.of(), Map.of(), DataMessage.class);
    }

    public DataverseHttpResponse<DataMessage> setActiveState(long id, boolean active) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "" + id, ":active", "" + active);
        return httpClientWrapper.putJsonString(path, "", Map.of(), Map.of(), DataMessage.class);
    }

    public DataverseHttpResponse<DataMessage> deleteLicense(long id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "" + id);
        return httpClientWrapper.delete(path, DataMessage.class);
    }
}
