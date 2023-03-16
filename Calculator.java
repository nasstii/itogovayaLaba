import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

class Fraction {
    int numerator;
    int denominator;

    Fraction(int numerator) {
        this.numerator = 1;
        this.denominator = 1;
    }

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;

    }

    public Fraction sokr(Fraction fraction) {
        int x = fraction.numerator;
        int y = fraction.denominator;
        while (y != 0) {
            int remainder = x % y;
            x = y;
            y = remainder;
        }
        int ReducedDenominator = fraction.denominator / x;
        int ReducedNumerator = fraction.numerator / x;

        Fraction ReducedFraction = new Fraction(ReducedNumerator, ReducedDenominator);
        return ReducedFraction;
    }

    public Fraction add(Fraction f1, Fraction f2) {
        int NumeratorFinal = f1.numerator * f2.denominator + f2.numerator * f1.denominator;
        int DenominatorFinal = f1.denominator * f2.denominator;

        Fraction FinalFraction = new Fraction(NumeratorFinal, DenominatorFinal);
        return FinalFraction.sokr(FinalFraction);
    }

    public Fraction add(Fraction fraction) {
        Fraction Finalvalue = add(fraction, this);
        return Finalvalue;
    }

    public Fraction subtract(Fraction f1, Fraction f2) {
        int NumeratorFinal = f1.numerator * f2.denominator - f2.numerator * f1.denominator;
        int DenominatorFinal = f1.denominator * f2.denominator;

        Fraction FinalFraction = new Fraction(NumeratorFinal, DenominatorFinal);
        return FinalFraction.sokr(FinalFraction);
    }

    public Fraction subtract(Fraction fraction) {
        Fraction Finalvalue = subtract(this, fraction);
        return Finalvalue;
    }

    public Fraction multiply(Fraction f1, Fraction f2) {

        int DenominatorFinal = f1.denominator * f2.denominator;
        int NumeratorFinal = f1.numerator * f2.numerator;

        Fraction FinalFraction = new Fraction(NumeratorFinal, DenominatorFinal);
        return FinalFraction.sokr(FinalFraction);
    }

    public Fraction multiply(Fraction fraction) {
        Fraction Finalvalue = multiply(this, fraction);
        return Finalvalue;
    }

    public Fraction divide(Fraction f1, Fraction f2) {

        int NumeratorFinal = f1.numerator * f2.denominator;
        int DenominatorFinal = f1.denominator * f2.numerator;

        Fraction FinalFraction = new Fraction(NumeratorFinal, DenominatorFinal);
        return FinalFraction.sokr(FinalFraction);
    }

    public Fraction divide(Fraction fraction) {
        Fraction Finalvalue = divide(this, fraction);
        return Finalvalue;
    }

    public String toString() {
        if (denominator == 1) {
            return " " + numerator;
        } else if (denominator * numerator < 0) {
            return " " + numerator + "/" + (denominator);
        } else {
            return "  " + numerator + "/" + denominator;
        }
    }

    static public Fraction fromString(String stringFraction) throws MyException {
        String[] strFrac = stringFraction.split("\\/");
        int numFrac[] = new int[strFrac.length];
        for (int i = 0; i < strFrac.length; i++) {
            numFrac[i] = Integer.parseInt(strFrac[i]);
        }
        int numerator = numFrac[0];
        if (numFrac[1] == 0) {
            throw new MyException("Знаментель равен 0");
        }
        int denominator = numFrac[1];
        Fraction fraction = new Fraction(numerator, denominator);

        return fraction;
    }

    private static class MyException extends Exception {
        public MyException(String message) {
            super(message);
        }
    }

    private static boolean isDelimiter(String token) {

        String delimiters = "() +-*/";

        if (token.length() != 1)
            return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i))
                return true;
        }
        return false;
    }

    private static boolean isOperator(String token) {
        String operators = "+-*/";
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i))
                return true;
        }
        return false;
    }

    private static int precedence(String token) {// приоритет операций

        char c = token.charAt(0);
        switch (c) {
            case '(':
                return 1;
            case '-':
                return 2;
            case '+':
                return 2;
            case '*':
                return 3;
            case ':':
                return 3;

        }
        return 4;
    }

    public static List<String> parse(String expression) {

        String delimiters = "() +:-*";

        List<String> postfixExpression = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(expression, delimiters, true);
        String previousToken = "";
        String currentToken = "";

        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();

            if (currentToken.equals(" "))
                continue;
            if (currentToken.equals("/"))
                continue;

            else if (isDelimiter(currentToken)) {
                if (currentToken.equals("("))
                    stack.push(currentToken);
                else if (currentToken.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfixExpression.add(stack.pop());

                    }
                    stack.pop();

                } else {
                    if (currentToken.equals("-") && (previousToken.equals("")
                            || (isDelimiter(previousToken) && !previousToken.equals(")")))) {
                        currentToken = "~";
                    } else {
                        while (!stack.isEmpty() && (precedence(currentToken) <= precedence(stack.peek()))) {
                            postfixExpression.add(stack.pop());
                        }

                    }
                    stack.push(currentToken);
                }

            }

            else {
                postfixExpression.add(currentToken);
            }
            previousToken = currentToken;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()))
                postfixExpression.add(stack.pop());

        }
        return postfixExpression;
    }

    public static Fraction calc(List<String> postfixExpression) {

        Deque<Fraction> stack = new ArrayDeque<Fraction>();
        for (String x : postfixExpression) {

            if (x.equals("+")) {
                Fraction f1 = stack.pop();
                Fraction f2 = stack.pop();
                stack.push(f1.add(f2));
            } else if (x.equals("-")) {
                Fraction f1 = stack.pop();
                Fraction f2 = stack.pop();
                stack.push(f2.subtract(f1));
            } else if (x.equals("*")) {
                Fraction f1 = stack.pop();
                Fraction f2 = stack.pop();
                stack.push(f1.multiply(f2));
            } else if (x.equals(":")) {
                Fraction f1 = stack.pop();
                Fraction f2 = stack.pop();
                stack.push(f2.divide(f1));
            } else
                try {
                    stack.push(fromString(x));
                } catch (MyException ex) {
                    System.out.println(ex.getMessage());

                }
        }
        return stack.pop();

    }

    public static void main(String[] args) {
        System.out.print("Введите пример: ");
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();

        try {

            System.out.println("Результат: " + calc(parse(expression)));
        } catch (Exception ex) {
            System.out.println("Ошибка");

        }

        in.close();
    }
}