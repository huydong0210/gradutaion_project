package com.huydong.resource_server;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.huydong.resource_server");

        noClasses()
            .that()
            .resideInAnyPackage("com.huydong.resource_server.service..")
            .or()
            .resideInAnyPackage("com.huydong.resource_server.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.huydong.resource_server.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
