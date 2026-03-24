package com.example.marketplace.architecture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Testes de arquitetura usando ArchUnit.
 * Valida que a arquitetura em camadas está sendo respeitada.
 */
class LayeredArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.example.marketplace");
    }

    /**
     * Valida que as camadas seguem as regras de dependência:
     * - Controller pode acessar Service e Model
     * - Service pode acessar Repository e Model
     * - Repository pode acessar apenas Model
     * - Model não pode acessar nenhuma outra camada
     */
    @Test
    void validarArquiteturaEmCamadas() {
        layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                
                // Definir camadas
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                .layer("Model").definedBy("..model..")
                
                // Regras de dependência
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Controller").mayOnlyAccessLayers("Service", "Model")
                .whereLayer("Service").mayOnlyAccessLayers("Repository", "Model")
                .whereLayer("Repository").mayOnlyAccessLayers("Model")
                
                .check(importedClasses);
    }

    /**
     * Valida que classes terminadas em "Controller" estão no package correto
     */
    @Test
    void controllersDevemEstarNoPackageController() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..controller..")
                .as("Controllers devem estar no package controller");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que classes terminadas em "Service" estão no package correto
     */
    @Test
    void servicesDevemEstarNoPackageService() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..service..")
                .as("Services devem estar no package service");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que classes terminadas em "Repository" estão no package correto
     */
    @Test
    void repositoriesDevemEstarNoPackageRepository() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..repository..")
                .as("Repositories devem estar no package repository");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Controllers têm a anotação @Controller ou @RestController
     */
    @Test
    void controllersDevemSerAnotadosCorretamente() {
        ArchRule rule = classes()
                .that().resideInAPackage("..controller..")
                .and().haveSimpleNameEndingWith("Controller")
                .should().beAnnotatedWith(org.springframework.stereotype.Controller.class)
                .orShould().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                .as("Controllers devem ser anotados com @Controller ou @RestController");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Services têm a anotação @Service
     */
    @Test
    void servicesDevemSerAnotadosComService() {
        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .and().haveSimpleNameEndingWith("Service")
                .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
                .as("Services devem ser anotados com @Service");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Repositories não podem acessar Services
     */
    @Test
    void repositoriesNaoPodemAcessarServices() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("..service..")
                .as("Repositories não devem depender de Services");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Repositories não podem acessar Controllers
     */
    @Test
    void repositoriesNaoPodemAcessarControllers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("..controller..")
                .as("Repositories não devem depender de Controllers");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Services não podem acessar Controllers
     */
    @Test
    void servicesNaoPodemAcessarControllers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat()
                .resideInAPackage("..controller..")
                .as("Services não devem depender de Controllers");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que Model não pode depender de outras camadas
     */
    @Test
    void modelNaoDeveDependerDeOutrasCamadas() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..controller..", "..service..", "..repository..")
                .as("Model não deve depender de outras camadas");
        
        rule.check(importedClasses);
    }

    /**
     * Valida que interfaces Repository estão no package repository
     */
    @Test
    void interfacesRepositoryDevemEstarNoPackageCorreto() {
        ArchRule rule = classes()
                .that().areInterfaces()
                .and().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..repository..")
                .as("Interfaces Repository devem estar no package repository");
        
        rule.check(importedClasses);
    }
}
