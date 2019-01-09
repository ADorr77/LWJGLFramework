package InputClasses;

import InputClasses.Message;

public class IntPackage extends MessagePackage
{
    private int m_iXData, m_iYData;

    IntPackage(Message message, int iXData, int iYData)
    {
        super(message);
        m_iXData = iXData;
        m_iYData = iYData;
    }

    @Override
    public int getIntXData() {
        return m_iXData;
    }

    @Override
    public int getIntYData() {
        return m_iYData;
    }
}
