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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.RoleAssignmentReadOnly;

@Slf4j
public class DatasetAssignRole extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        String userIdentifier = args[1];
        String roleName = args[2];
        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setAssignee("@" + userIdentifier);
        roleAssignment.setRole(roleName);

        var r = client.dataset(persistentId).assignRole(roleAssignment);
        log.info(r.getEnvelopeAsJson().toPrettyString());
        RoleAssignmentReadOnly ra = r.getData();
        log.info("Role Assignment ID: {}", ra.getId());
        log.info("Role ID: {}", ra.getRoleId());
        log.info("Assignee: {}", ra.getAssignee());
        log.info("Definition Point: {}", ra.getDefinitionPointId());
        log.info("Role Assignment Private URL Token: {}", ra.getPrivateUrlToken());
        log.info("---");
    }
}
