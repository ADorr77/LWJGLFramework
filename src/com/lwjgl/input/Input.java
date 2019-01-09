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
    private static double m_iXMousePos, m_iYMousePos;


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
        glfwSetFramebufferSizeCallback(lWindow, (window, width1, height1) -> {
            addMessage(new IntPackage(Message.FRAMEBUFFERRESIZE, width1, height1));
            m_iPixelWidth = width1;
            m_iPixelHeight = height1;
        });
        glfwSetWindowSizeCallback(lWindow, (window, width1, height1) -> {
            addMessage(new IntPackage(Message.RESIZE, width1, height1));
            m_iWidth = width1;
            m_iHeight = height1;
        });
        glfwSetCursorPosCallback(lWindow, (window, xpos, ypos) -> {
            m_iXMousePos = xpos;
            m_iYMousePos = ypos;
        });
        glfwSetMouseButtonCallback(lWindow, Input::mouseButtonCallback);
        glfwSetScrollCallback(lWindow, (window, xoffset, yoffset) ->
                addMessage(new DoublePackage(Message.MOUSESCROLL, xoffset, yoffset))
        );
        glfwSetWindowIconifyCallback(lWindow, (window, iconified) -> {
            Message message;
            if(iconified)
                message = Message.MINIMIZE;
            else
                message = Message.UNMINIMIZE;

            addMessage(new MessagePackage(message));
        });
        glfwSetWindowCloseCallback(lWindow, window -> addMessage(new MessagePackage(Message.DESTROY)));


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

    // these use the GLFW enums for key or mouse button IDs
    public static boolean keyPressed(int key) { return m_bKeyPressed[key]; }
    public static boolean mouseButtonPressed(int iButton) { return m_bMouseButtonPressed[iButton]; }

    // Functions that help record text input
    public static void recordText() { m_bRecordingText = true; }
    public static void stopRecordingText() { m_bRecordingText = false; }
    public static String getText() { return m_strTextBuffer; }
    public static void clearText() { m_strTextBuffer = ""; }

    // pixel size is used for glViewport and projection matrices
    public static int getPixelWidth() { return m_iPixelWidth; }
    public static int getPixelHeight() { return m_iPixelHeight; }

    // functions to get the window size in screen Coordinates
    public static int getScreenCoordWidth() { return m_iWidth; }
    public static int getScreenCoordHeight() { return m_iHeight; }

    // functions to get the mouse position which is always in screen Coordinates
    public static double getXMousePos() { return m_iXMousePos; }
    public static double getYMousePos() { return m_iYMousePos; }

    private static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        Message downMessage, upMessage;

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

    private static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        Message downMessage, upMessage;

        switch (mods)
        {
            case GLFW_MOUSE_BUTTON_LEFT:
                downMessage = Message.MOUSELEFTDOWN;
                upMessage = Message.MOUSELEFTUP;
                break;
            case GLFW_MOUSE_BUTTON_MIDDLE:
                downMessage = Message.MOUSEMIDDLEDOWN;
                upMessage = Message.MOUSEMIDDLEUP;
                break;
            case GLFW_MOUSE_BUTTON_RIGHT:
                downMessage = Message.MOUSERIGHTDOWN;
                upMessage = Message.MOUSERIGHTUP;
                break;
            default:
                downMessage = Message.MOUSEOTHERDOWN;
                upMessage = Message.MOUSEOTHERUP;
                break;
        }

        if(action == GLFW_PRESS)
        {
            m_bMouseButtonPressed[button] = true;
            addMessage(new DoublePackage(downMessage, m_iXMousePos, m_iYMousePos));
        }
        else if(action == GLFW_RELEASE)
        {
            m_bMouseButtonPressed[button] = false;
            addMessage(new DoublePackage(upMessage, m_iXMousePos, m_iYMousePos));
        }
    }
}
