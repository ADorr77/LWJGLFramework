package InputClasses;

public class IDPackage extends MessagePackage
{
    private int m_ID;

    IDPackage(Message message, int id)
    {
        super(message);
        m_ID = id;
    }

    @Override
    public int getID() {
        return m_ID;
    }
}
