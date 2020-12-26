package miniplc0java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
//import miniplc0java.instruction.Instruction;
import miniplc0java.tokenizer.StringIter;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;
import miniplc0java.tokenizer.Tokenizer;

//import net.sourceforge.argparse4j.*;
//import net.sourceforge.argparse4j.impl.Arguments;
//import net.sourceforge.argparse4j.inf.ArgumentAction;
//import net.sourceforge.argparse4j.inf.ArgumentParser;
//import net.sourceforge.argparse4j.inf.ArgumentParserException;
//import net.sourceforge.argparse4j.inf.Namespace;

public class App {
    public static void main(String[] args) throws CompileError {
//        var argparse = buildArgparse();

//        Namespace result;
//        try {
//            result = argparse.parseArgs(args);
//        } catch (ArgumentParserException e1) {
//            argparse.handleError(e1);
//            return;
//        }

        String result="a";
//        var inputFileName=args[0];
//        var outputFileName=args[1];
        var inputFileName="in.txt";
        var outputFileName="out.txt";

        InputStream input;
        if (inputFileName.equals("-")) {
            input = System.in;
        } else {
            try {
                input = new FileInputStream(inputFileName);
            } catch (FileNotFoundException e) {
                System.err.println("Cannot find input file.");
                e.printStackTrace();
                System.exit(2);
                return;
            }
        }

        PrintStream output;
        if (outputFileName.equals("-")) {
            output = System.out;
        } else {
            try {
                output = new PrintStream(new FileOutputStream(outputFileName));
            } catch (FileNotFoundException e) {
                System.err.println("Cannot open output file.");
                e.printStackTrace();
                System.exit(2);
                return;
            }
        }

        Scanner scanner;
        scanner = new Scanner(input);
        var iter = new StringIter(scanner);

//        while(scanner.hasNextInt()){
//            System.out.println(scanner.nextLine());
//        }

        var tokenizer = tokenize(iter);

        if (result.equals("t")) {
            var tokens = new ArrayList<Token>();
            try {
                while (true) {
                    var token = tokenizer.nextToken();
                    if (token.getTokenType().equals(TokenType.EOF)) {
                        break;
                    }
                    tokens.add(token);
                }
            } catch (Exception e) {
                // 遇到错误不输出，直接退出
                System.err.println(e);
                System.exit(0);
                return;
            }
            for (Token token : tokens) {
//                System.out.println(token.toString());
                output.println(token.toString());
            }
        }
        else if (result.equals("a")) {
            // analyze
            var analyzer = new Analyser(tokenizer);
            analyzer.analyse();
            analyzer.vm.eout(output);
        }
        else {
            System.err.println("Please specify either '--analyse' or '--tokenize'.");
            System.exit(3);
        }
    }

    private static Tokenizer tokenize(StringIter iter) {
        var tokenizer = new Tokenizer(iter);
        return tokenizer;
    }
}
