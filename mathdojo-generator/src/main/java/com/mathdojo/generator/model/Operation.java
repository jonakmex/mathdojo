package com.mathdojo.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class Operation {
    private List<Integer> factors;
    private List<Character> operators;

    public Integer calculate() {
        List<Integer> tempFactors = new ArrayList<>(factors);
        List<Character> tempOperators = new ArrayList<>(operators);

        // First pass: handle * and /
        for (int i = 0; i < tempOperators.size(); i++) {
            if (tempOperators.get(i) == '*' || tempOperators.get(i) == '/') {
                Float result = tempOperators.get(i) == '*'
                        ? (tempFactors.get(i) * tempFactors.get(i + 1))
                        : ((float)tempFactors.get(i) / (float)tempFactors.get(i + 1));
                if (result.intValue() != result) {
                    throw new IllegalArgumentException("Result is not an integer: " + result);
                }
                tempFactors.set(i, result.intValue());
                tempFactors.remove(i + 1);
                tempOperators.remove(i);
                i--; // Adjust index after removal
            }
        }

        // Second pass: handle + and -
        int result = tempFactors.get(0);
        for (int i = 0; i < tempOperators.size(); i++) {
            if (tempOperators.get(i) == '+') {
                result += tempFactors.get(i + 1);
            } else if (tempOperators.get(i) == '-') {
                result -= tempFactors.get(i + 1);
            }
        }

        return result;
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < factors.size(); i++) {
            sb.append(factors.get(i));
            if (i < operators.size()) {
                sb.append(operators.get(i));
            }
        }
        return sb.toString();
    }
}
