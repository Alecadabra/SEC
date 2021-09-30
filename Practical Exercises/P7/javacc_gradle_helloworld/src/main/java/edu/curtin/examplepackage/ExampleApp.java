package edu.curtin.examplepackage;

import java.io.*;

public class ExampleApp
{
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Enter some text to be validated. Press Ctrl-D to finish.");
        FileInputStream inputFile = new FileInputStream("input.txt");
        InputStreamReader fileReader = new InputStreamReader(inputFile);
        MyParser parser = new MyParser(fileReader);
        try
        {
            parser.busFile();
            fileReader.close();
            System.out.println("Input valid");
        }
        catch(ParseException | IOException e)
        {
            System.out.println("Parsing error!");
            System.out.println(e.getMessage());
        }
    }
}

