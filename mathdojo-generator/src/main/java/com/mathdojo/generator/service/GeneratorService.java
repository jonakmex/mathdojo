package com.mathdojo.generator.service;

import com.mathdojo.generator.model.Operation;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class GeneratorService {
    private static final String FACTOR_WILDCARD = "?";
    private static final String OPERATORS = "[+\\-*/_]";
    private static final Character OPERATOR_WILDCARD = '_';
    private static final Integer MAX_ATTEMPTS = 10;

    public List<Operation> createSession(String operationPattern, Integer nOperations) {
        Set<Operation> operations = new HashSet<>();
        int callsToCreate = 0;
        while (operations.size() < nOperations && callsToCreate < nOperations) {
            Operation newOperation = createOperation(operationPattern);
            operations.add(newOperation);
            callsToCreate++;
        }
        return new ArrayList<>(operations);
    }

    private Operation createOperation (String operationPattern) {
        if (!validateOperation(operationPattern)) {
            throw new IllegalArgumentException("Invalid operation pattern");
        }

        Pattern operatorPattern = Pattern.compile(OPERATORS);
        int attempts = 0;
        while (attempts<MAX_ATTEMPTS) {
        List<Integer> factors = operatorPattern.splitAsStream(operationPattern)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .map(token -> token.contains(FACTOR_WILDCARD) ? getRandomFactor(token) : Integer.parseInt(token))
                .toList();
        List<Character> operators = IntStream.range(0, operationPattern.length())
                .mapToObj(operationPattern::charAt)
                .filter(ch -> OPERATORS.indexOf(ch) >= 0)
                .map(ch -> ch == OPERATOR_WILDCARD ? getRandomOperator(ch) : ch)
                .toList();

            Operation genOp = Operation.builder()
                    .factors(factors)
                    .operators(operators)
                    .build();
            try {
                genOp.calculate();
                return genOp;
            }
            catch (Exception e) {
                // Ignore division by zero or other calculation errors
                attempts++;
            }
        }

        throw new IllegalArgumentException("Unable to generate a valid operation after " + MAX_ATTEMPTS + " attempts");
    }

    private Character getRandomOperator(Character ch) {
        Random random = new Random();
        int randomValue = random.nextInt(4);
        switch (randomValue) {
            case 0:
                return '+';
            case 1:
                return '-';
            case 2:
                return '*';
            case 3:
                return '/';
            default:
                return ch; // Fallback to original character
        }
    }

    private Integer getRandomFactor(String token) {
        Random random = new Random();
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == '?') {
                int randomValue = random.nextInt(10);
                token = token.substring(0, i) + randomValue + token.substring(i + 1);
            }
        }
        return Integer.parseInt(token);
    }

    private boolean validateOperation (String operationPattern) {
        String validPattern = "^[0-9+\\-*/?_]+$";
        return operationPattern.matches(validPattern);
    }
}
