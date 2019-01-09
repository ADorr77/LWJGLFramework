package InputClasses;

public class DoublePackage extends MessagePackage
{
    private double m_dXData, m_dYData;

    DoublePackage(Message message, double dXData, double dYData)
    {
        super(message);
        m_dXData = dXData;
        m_dYData = dYData;
    }

    @Override
    public double getDoubleXData() {
        return m_dXData;
    }

    @Override
    public double getDoubleYData() {
        return m_dYData;
    }
}
