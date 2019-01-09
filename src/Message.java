public class Message
{
    public int message;
    public int iXData;
    public int iYData;

    public enum Messages {
        EMPTY,
        KEYDOWN,
        KEYUP,
        SHIFTKEYDOWN,
        SHIFTKEYUP,
        CTRLCOMMANDUP,
        CTRLCOMMANDDOWN,
        ALTCOMMANDUP,
        ALTCOMMANDDOWN,
        CHARINPUT,
        MOUSELEFTDOWN,
        MOUSELEFTUP,
        MOUSEMIDDLEDOWN,
        MOUSEMIDDLEUP,
        MOUSERIGHTDOWN,
        MOUSERIGHTUP,
        MOUSEOTHERDOWN,
        MOUSEOTHERUP,
        MOUSESCROLL,
        RESIZE,
        FRAMEBUFFERRESIZE,
        MINIMIZE,
        UNMINIMIZE,
        DESTROY
    }

    Message(int m, int x, int y)
    {
        message = m;
        iXData = x;
        iYData = y;
    }
}
