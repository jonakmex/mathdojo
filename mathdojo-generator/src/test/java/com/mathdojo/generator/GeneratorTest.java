package com.mathdojo.generator;

import com.mathdojo.generator.model.Operation;
import com.mathdojo.generator.service.GeneratorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    private static GeneratorService generatorService;

    @BeforeAll
    static void beforeAll() {
        generatorService = new GeneratorService();
    }

    @Test
    void should_get_basic_operation () {
        // Given
        String basicOperation = "1+2";
        Integer expectedResult = 3;
        Integer nOperations = 1;
        // When
        List<Operation> operation = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operation.size());
        Operation expectedOperation = Operation.builder()
                .factors(Arrays.asList(1, 2))
                .operators(Arrays.asList('+'))
                .build();
        assertEquals(expectedOperation, operation.getFirst());

        assertEquals(expectedResult, operation.getFirst().calculate());
    }

    @Test
    void should_sum_three_operators () {
        // Given
        String basicOperation = "1+2+3";
        Integer expectedResult = 6;
        Integer nOperations = 1;
        // When
        List<Operation> operation = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operation.size());
        assertEquals(expectedResult, operation.getFirst().calculate());
    }

    @Test
    void should_sum_six_operators_with_priority () {
        // Given
        String basicOperation = "1+2*3";
        Integer expectedResult = 7;
        Integer nOperations = 1;
        // When
        List<Operation> operation = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operation.size());
        assertEquals(expectedResult, operation.getFirst().calculate());
    }

    @Test
    void should_use_wild_card () {
        // Given
        String basicOperation = "1+?";
        Integer nOperations = 1;
        // When
        List<Operation> operation = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operation.size());
        assertEquals(1, operation.getFirst().getFactors().getFirst());
        Integer calculatedFactor = operation.getFirst().getFactors().get(1);
        Integer expectedResult = 1 + calculatedFactor;
        assertEquals(expectedResult, operation.getFirst().calculate());
    }

    @Test
    void should_use_wild_card_six_factors () {
        // Given
        String basicOperation = "?+?+?+?+?+?";
        Integer nOperations = 1;
        // When
        List<Operation> operations = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operations.size());
        Integer calculatedFactors = operations.getFirst().getFactors().size();
        assertEquals(6, calculatedFactors);
        System.out.println(operations.getFirst());
        System.out.println(operations.getFirst().calculate());
    }

    @Test
    void should_generate_with_random_operator () {
        // Given
        String basicOperation = "1_2";
        Integer nOperations = 1;
        // When
        List<Operation> operations = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operations.size());
        Operation generatedOperation = operations.getFirst();
        assertEquals(1, generatedOperation.getFactors().getFirst());
        assertEquals(2, generatedOperation.getFactors().get(1));
        List<Character> validOperators = Arrays.asList('+', '-', '*', '/');
        for (Character operator : generatedOperation.getOperators()) {
            assertTrue(validOperators.contains(operator), "Invalid operator found: " + operator);
        }
    }

    @Test
    void should_generate_with_random_operators_and_random_factors () {
        // Given
        String basicOperation = "??_??";
        Integer nOperations = 1;
        // When
        List<Operation> operations = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operations.size());
        Operation generatedOperation = operations.getFirst();

        for (Integer factor : generatedOperation.getFactors()) {
            assertTrue(factor >= 10 && factor <= 99, "Factor is not a two-digit integer: " + factor);
        }

        List<Character> validOperators = Arrays.asList('+', '-', '*', '/');
        for (Character operator : generatedOperation.getOperators()) {
            assertTrue(validOperators.contains(operator), "Invalid operator found: " + operator);
        }
    }

    @Test
    void should_generate_with_random_operators_and_random_factors_100 () {
        // Given
        String basicOperation = "???_???";
        Integer nOperations = 1;
        // When
        List<Operation> operations = generatorService.createSession(basicOperation,nOperations);
        // Then
        assertEquals(nOperations, operations.size());
        Operation generatedOperation = operations.getFirst();

        for (Integer factor : generatedOperation.getFactors()) {
            assertTrue(factor >= 100 && factor <= 999, "Factor is not a two-digit integer: " + factor);
        }

        List<Character> validOperators = Arrays.asList('+', '-', '*', '/');
        for (Character operator : generatedOperation.getOperators()) {
            assertTrue(validOperators.contains(operator), "Invalid operator found: " + operator);
        }
    }

    @Test
    void should_print_operation () {
        // Given
        String basicOperation = "1+2-3*4";
        Integer nOperations = 1;
        // When
        Operation operation = generatorService.createSession(basicOperation,nOperations).getFirst();
        // Then
        String expectedString = "1+2-3*4=-9";
        assertEquals(expectedString, operation.toString() + "=" + operation.calculate());
    }
}
