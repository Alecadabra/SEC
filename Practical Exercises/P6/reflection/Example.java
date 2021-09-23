public class Example
{
    public Example(String blah)
    {
        System.out.println(
            "[Example] I'm being constructed with string \"" + blah + "\""
        );
    }

    public String stringReturn(int x)
    {
        System.out.println(
            "[Example] methodOne called with int \"" + x + "\""
        );
        return "[Example] Return from methodOne";
    }

    public void voidReturn(int x)
    {
        System.out.println(
            "[Example] methodTwo called with int \"" + x + "\""
        );
    }

    public void notShownMethod()
    {
    }
}
