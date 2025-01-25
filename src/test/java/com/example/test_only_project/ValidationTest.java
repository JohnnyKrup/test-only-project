package com.example.test_only_project;

import com.example.test_only_project.dto.StudentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Diese Klasse demonstriert, wie man mit der Jakarta Validation API Daten validiert.
 * Sie enthält Unit-Tests, die ein einfaches DTO (Data Transfer Object) validieren.
 */
public class ValidationTest {

    // Validator-Instanz zur Durchführung von Validierungen
    private Validator validator;

    /**
     * Diese Methode wird vor jedem Test ausgeführt.
     * Sie initialisiert den Validator, der zur Validierung verwendet wird.
     */
    @BeforeEach
    void setUp() {
        // Erstellt eine ValidatorFactory und initialisiert den Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Dieser Test überprüft, ob ein gültiges StudentDTO erfolgreich validiert wird.
     * Erwartetes Ergebnis: Keine Validierungsfehler.
     */
    @Test
    void validateValidStudentDTO() {
        // Arrange: Erstellen eines gültigen StudentDTO-Objekts
        StudentDTO dto = new StudentDTO();
        dto.setName("John Doe");    // Gültiger Name
        dto.setAge(20);             // Gültiges Alter
        dto.setEmail("johndoe@mail.com"); // Gültige Email

        // Act: Validierung des DTO-Objekts
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(dto);

        // Assert: Es sollten keine Validierungsfehler vorliegen
        assertThat(violations).isEmpty(); // Erwartet: Keine Fehler
    }

    /**
     * Dieser Test überprüft, ob ein ungültiges StudentDTO korrekt Validierungsfehler zurückgibt.
     * Erwartetes Ergebnis: Es gibt 3 Validierungsfehler.
     */
    @Test
    void validateInvalidStudentDTO() {
        // Arrange: Erstellen eines ungültigen StudentDTO-Objekts
        StudentDTO dto = new StudentDTO();
        dto.setName("");                // Ungültiger Name (leer)
        dto.setAge(null);               // Ungültiges Alter (null)
        dto.setEmail("invalid-email");  // Ungültige Email

        // Act: Validierung des DTO-Objekts
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(dto);

        // Assert: Es sollten genau 4 Validierungsfehler vorliegen
        assertThat(violations).hasSize(4);

        // Überprüfung der spezifischen Fehlermeldungen
        assertThat(violations).anyMatch(v ->
                v.getMessage().equals("Name cannot be blank") ||
                        v.getMessage().equals("Name must be between 2 and 50 characters")
        );
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Age is required"));
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Email must be valid"));
    }
}