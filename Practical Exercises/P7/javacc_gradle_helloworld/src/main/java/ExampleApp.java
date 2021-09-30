package edu.curtin.examplepackage;

public class ExampleApp 
{
    public static void main(String[] args) 
    {
        System.out.println("Enter some text to be validated. Press Ctrl-D to finish.");
        MyParser parser = new MyParser(System.in);
        try
        {
            parser.helloWorld();
            System.out.println("Input valid");
        }
        catch(ParseException e)
        {
            System.out.println("Parsing error!");
            System.out.println(e.getMessage());
        }
    }
}

