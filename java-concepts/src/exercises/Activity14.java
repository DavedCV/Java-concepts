package exercises;

public class Activity14 {

    public static void main(String[] args) {
        CalculatorWithFixedOperators summer = new CalculatorWithFixedOperators(1, 2, "+");
        System.out.println(summer.operate());

        CalculatorWithFixedOperators subtract = new CalculatorWithFixedOperators(1, 2, "-");
        System.out.println(subtract.operate());

        CalculatorWithFixedOperators multiplier = new CalculatorWithFixedOperators(4, 100, "*");
        System.out.println(multiplier.operate());

        CalculatorWithFixedOperators divider = new CalculatorWithFixedOperators(4, 100, "/");
        System.out.println(divider.operate());
    }

    private static abstract class Operator {

        private final String operator;

        public Operator(String operator) {
            this.operator = operator;
        }

        private boolean Matches(String operator) {
            return this.operator.equals(operator);
        }

        abstract public double operate(double operand1, double operand2);
    }

    private static class CalculatorWithFixedOperators {
        final double operand1;
        final double operand2;
        final Operator operator;

        public CalculatorWithFixedOperators(double operand1, double operand2, String operator) {
            this.operand1 = operand1;
            this.operand2 = operand2;

            switch (operator) {
                case "+" -> this.operator = new Operator(operator) {
                    @Override
                    public double operate(double operator1, double operator2) {
                        return operator1 + operator2;
                    }
                };
                case "-" -> this.operator = new Operator(operator) {
                    @Override
                    public double operate(double operator1, double operator2) {
                        return operator1 - operator2;
                    }
                };
                case "*" -> this.operator = new Operator(operator) {
                    @Override
                    public double operate(double operator1, double operator2) {
                        return operator1 * operator2;
                    }
                };
                case "/" -> this.operator = new Operator(operator) {
                    @Override
                    public double operate(double operator1, double operator2) {
                        return operator1 / operator2;
                    }
                };
                default -> this.operator = null;
            }
        }

        public double operate() {
            return operator.operate(operand1, operand2);
        }
    }

}
