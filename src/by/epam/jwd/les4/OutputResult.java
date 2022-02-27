package by.epam.jwd.les4;

import java.util.List;

public class OutputResult {

    public static void printResult(String str) {
        List<LexicalAnalyzer.Lexeme> lexemes = LexicalAnalyzer.lexAnalyzer(str);
        LexicalAnalyzer.LexemeBuffer lexemeBuffer = new LexicalAnalyzer.LexemeBuffer(lexemes);

        System.out.println("Result = " + LexicalAnalyzer.expr(lexemeBuffer));
    }
}
