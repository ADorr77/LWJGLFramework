package com.lwjgl.input;

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

    private static ArrayList<ArrayList<MessagePackage>> m_messageQueues;

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

        m_strTextBuffer = "";
        m_bRecordingText = false;

        int [] width = new int[1];
        int [] height = new int[1];
        glfwGetFramebufferSize(lWindow, width, height);
        m_iPixelWidth = width[0];
        m_iPixelHeight = height[0];
        glfwGetWindowSize(lWindow, width, height);
        m_iWidth = width[0];
        m_iHeight = height[0];

        m_iXMousePos = 0;
        m_iYMousePos = 0;

        // only put the long ones with :: because -> helps with debuging
        glfwSetKeyCallback(lWindow, Input::keyCallback);
        glfwSetCharCallback(lWindow, ((window, codepoint) -> {
            addMessage(new IDPackage(Message.CHARINPUT, codepoint));
            if (m_bRecordingText)
                m_strTextBuffer += (char)codepoint;
        }));

        m_messageQueues = new ArrayList<>();
        m_messageQueues.add(new ArrayList<>());
    }

    public static void update()
    {
        glfwPollEvents();
        MessagePackage message = getMessage(0);
        while (message != null)
        {
            switch (message.getMesssage())
            {
                case KEYDOWN:
                    switch (message.getID())
                    {
                        case GLFW_KEY_BACKSPACE:
                            m_strTextBuffer = m_strTextBuffer.substring(0, m_strTextBuffer.length() - 1);
                            break;
                        case GLFW_KEY_ENTER:
                            m_strTextBuffer += "\n";
                            break;
                        case GLFW_KEY_TAB:
                            m_strTextBuffer += "\t";
                            break;
                    }
            }
            message = getMessage(0);
        }
    }

    public static int createMessageQueue()
    {
        m_messageQueues.add(new ArrayList<>(0));
        return m_messageQueues.size() -1;
    }

    public static MessagePackage getMessage(int iQueue)
    {
        ArrayList<MessagePackage> queue = m_messageQueues.get(iQueue);
        if (queue.isEmpty())
            return null;

        return queue.remove(0);
    }

    private static void addMessage(MessagePackage message)
    {
        for ( ArrayList<MessagePackage> queue : m_messageQueues)
        {
            queue.add(message);
        }
    }

    public static void recordText() { m_bRecordingText = true; }
    public static void stopRecordingText() { m_bRecordingText = false; }
    public static String getText() { return m_strTextBuffer; }
    public static void clearText() { m_strTextBuffer = ""; }

    private static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        Message downMessage, upMessage;
        MessagePackage message;

        switch (mods)
        {
            case GLFW_MOD_SHIFT:
                downMessage = Message.SHIFTKEYDOWN;
                upMessage = Message.SHIFTKEYUP;
                break;
            case GLFW_MOD_CONTROL:
                downMessage = Message.CTRLCOMMANDDOWN;
                upMessage = Message.CTRLCOMMANDUP;
                break;
            case GLFW_MOD_ALT:
                downMessage = Message.ALTCOMMANDDOWN;
                upMessage = Message.ALTCOMMANDUP;
                break;
            default:
                downMessage = Message.KEYDOWN;
                upMessage = Message.KEYUP;
                break;
        }

        if(action == GLFW_PRESS)
        {
            m_bKeyPressed[key] = true;
            addMessage(new IDPackage(downMessage, key));
        }
        else if(action == GLFW_RELEASE)
        {
            m_bKeyPressed[key] = false;
            addMessage(new IDPackage(upMessage, key));
        }
    }

}
