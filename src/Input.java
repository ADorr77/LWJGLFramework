import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

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


    public static void init(long lWindow)
    {
        m_lWindow = lWindow;
        m_bKeyPressed = new boolean[GLFW_KEY_LAST + 1];
        m_bMouseButtonPressed = new boolean[GLFW_MOUSE_BUTTON_LAST +1];

        m_messageQueues = new ArrayList<>(0);
    }

    private static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        int iKeyFlag;

        switch (mods)
        {
            case GLFW_MOD_SHIFT:
        }
    }

}
