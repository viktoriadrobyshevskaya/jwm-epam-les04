package by.epam.jwd.les4;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    public static List<Lexeme> lexAnalyzer(String expression) {
        List<Lexeme> list = new ArrayList<>();
        int position = 0;
        while (position < expression.length()) {
            char c = expression.charAt(position);
            switch (c) {
                case '(':
                    list.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    position++;
                    continue;
                case ')':
                    list.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    position++;
                    continue;
                case '+':
                    list.add(new Lexeme(LexemeType.PLUS, c));
                    position++;
                    continue;
                case '-':
                    list.add(new Lexeme(LexemeType.MINUS, c));
                    position++;
                    continue;
                case '*':
                    list.add(new Lexeme(LexemeType.MUL, c));
                    position++;
                    continue;
                case '/':
                    list.add(new Lexeme(LexemeType.DIV, c));
                    position++;
                    continue;
                default:
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            position++;
                            if (position >= expression.length()) {
                                break;
                            }
                            c = expression.charAt(position);
                        } while (c <= '9' && c >= '0');
                        list.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected lexeme: " + c);
                        }
                        position++;
                    }
            }
        }
        list.add(new Lexeme(LexemeType.EOF, ""));
        return list;
    }

    public static double expr(LexemeBuffer lexemes) {
        LexicalAnalyzer.Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexicalAnalyzer.LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusMinus(lexemes);
        }
    }

    public static double plusMinus(LexemeBuffer lexemes) {
        double value = multDiv(lexemes);
        while (true) {
            LexicalAnalyzer.Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case PLUS -> value += multDiv(lexemes);
                case MINUS -> value -= multDiv(lexemes);
                default -> {
                    lexemes.back();
                    return value;
                }
            }
        }

    }

    public static double multDiv(LexemeBuffer lexemes) {
        double value = factor(lexemes);
        while (true) {
            LexicalAnalyzer.Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case MUL -> value *= factor(lexemes);
                case DIV -> value /= factor(lexemes);
                default -> {
                    lexemes.back();
                    return value;
                }
            }
        }
    }

    public static double factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case LEFT_BRACKET:
                double value = expr(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexicalAnalyzer.LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPosition());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPosition());
        }
    }

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET,
        PLUS, MINUS, MUL, DIV,
        NUMBER,
        EOF
    }

    public static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "Lexeme{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class LexemeBuffer {
        private int position;

        List<Lexeme> lexemeList;

        public LexemeBuffer(List<Lexeme> lexemeList) {
            this.lexemeList = lexemeList;
        }

        public Lexeme next() {
            return lexemeList.get(position++);
        }

        public void back() {
            position--;
        }

        public int getPosition() {
            return position;
        }
    }
}
