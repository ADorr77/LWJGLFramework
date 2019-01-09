package InputClasses;

import InputClasses.Message;

public class MessagePackage
{
    protected Message m_message;

    MessagePackage(Message message)
    {
        m_message = message;
    }

    public final Message getMesssage()
    {
        return m_message;
    }

    public int getID()
    {
        return -1;
    }

    public int getIntXData() { return -1; }
    public int getIntYData() { return -1; }

    public double getDoubleXData() { return 0; }
    public double getDoubleYData() { return 0; }
}
