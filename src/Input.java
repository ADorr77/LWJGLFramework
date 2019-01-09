import java.util.ArrayList;

/**
 * This is a class that will handle input from glfw.
 *
 * @author (Celnardur)
 * @version (1/9/19)
 */
public class Input
{
    private static long m_lWindow;

    private static ArrayList<ArrayList<Message>> m_messageQueues;

    private static boolean [] m_bKeyPressed;
    private static boolean [] m_bMouseButtonPressed;

    private static String m_strTextBuffer;
    private static boolean m_bRecordingText;
    private static int m_iPixelWidth, m_iPixelHeight;
    private static int m_iWidth, m_iHeight;
    private static int m_iXMousePos, m_iYMousePos;
}
