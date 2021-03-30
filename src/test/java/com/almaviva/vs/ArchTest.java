package com.almaviva.vs;

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
            .importPackages("com.almaviva.vs");

        noClasses()
            .that()
            .resideInAnyPackage("com.almaviva.vs.service..")
            .or()
            .resideInAnyPackage("com.almaviva.vs.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.almaviva.vs.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
