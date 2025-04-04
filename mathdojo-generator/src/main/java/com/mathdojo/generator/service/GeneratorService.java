package com.mathdojo.generator.service;

import com.mathdojo.generator.model.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class GeneratorService {
    private static final String FACTOR_WILDCARD = "?";
    private static final String OPERATORS = "[+\\-*/_]";
    private static final Character OPERATOR_WILDCARD = '_';

    public List<Operation> createSession(String operationPattern, Integer nOperations) {
        List<Operation> operations = new ArrayList<>();
        for (int i = 0; i < nOperations; i++) {
            operations.add(createOperation(operationPattern));
        }
        return operations;
    }

    private Operation createOperation (String operationPattern) {
        Pattern operatorPattern = Pattern.compile(OPERATORS);
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

        return Operation.builder()
                .factors(factors)
                .operators(operators)
                .build();
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
}
