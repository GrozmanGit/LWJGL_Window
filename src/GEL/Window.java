package GEL;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;
//import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

        private final int width, height;
        private final String title;
        private long glfwWindow;
        private static Window window = null;

        private float[] vertices = new float[]
            {
                    +0.0f, +0.8f,    // Top coordinate
                    -0.8f, -0.8f,    // Bottom-left coordinate
                    +0.8f, -0.8f     // Bottom-right coordinate
            };

        private Window()
        {
            // width: 1920, height: 1080
            // width: 640, height: 480
            this.width = 1200;
            this.height = 700;
            this.title = "window";
        }

        public static Window get()
        {
            if (Window.window == null)
            {
                Window.window = new Window();
            }

            return Window.window;
        }

        public void run()
        {
            System.out.println("Running_LWJGL version: " + Version.getVersion());

            try {
                init();
                loop();
            } catch(Exception excp) {
                excp.printStackTrace();
            }

            // Free memory
            glfwFreeCallbacks(glfwWindow); // free callback from window
            glfwDestroyWindow(glfwWindow); // destroys window

            // Terminate GLFW, free err callback
            glfwTerminate();
            glfwSetErrorCallback(null).free(); // free memory
        }

        public void init()
        {
            // Setup error callback
            GLFWErrorCallback.createPrint(System.err).set(); // creates error prints

            // Initialize GLFW
            if (!glfwInit())
            {
                // throw new IllegalStateException("Unable to initialize GLFW");
                System.out.println("Unable to initialize GLFW");
                glfwTerminate();
            } else {
                System.out.println("GLFW initialization success");
            }

            // Configure GLFW
            glfwDefaultWindowHints(); //
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);

            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

            // Create window
            glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
            if (glfwWindow == NULL)
            {
                // throw new IllegalStateException("Failed to create GLFW window");
                /*System.out.println("Failed to create GLFW window");
                glfwTerminate();*/
                throw new RuntimeException("Failed to create GLFW window");
            }

            // Set up key callback
            glfwSetKeyCallback(glfwWindow, (glfwWindow, key, scancode, action, mods) ->
            {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                {
                    glfwSetWindowShouldClose(glfwWindow, true);
                }
            });

            // Get thread stack, push new frame
            try ( MemoryStack stack = stackPush() ) {
                IntBuffer pWidth = stack.mallocInt(1); // int*
                IntBuffer pHeight = stack.mallocInt(1); // int*

                // Get window size passed to glfwCreateWindow
                glfwGetWindowSize(glfwWindow, pWidth, pHeight);

                // Get monitor resolution
               GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
               System.out.println(vidmode);

                // Center window
                glfwSetWindowPos(
                        glfwWindow,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
                System.out.println(vidmode.width());
                System.out.println(vidmode.height());
            }

            glfwMakeContextCurrent(glfwWindow);  // Make OpenGL Context Current
            // Enable v-sync
            glfwSwapInterval(1); // frame-rate swapping according to monitor
            glfwShowWindow(glfwWindow); // Make window visible
        }

    public void loop()
        {
            // This line is critical for LWJGL's interoperation with GLFW's
            // OpenGL context, or any context that is managed externally.
            // LWJGL detects the context that is current in the current thread,
            // creates the GLCapabilities instance and makes the OpenGL
            // bindings available for use
            GL.createCapabilities();

            // Set clear color
            glClearColor(0.2f,0.3f,0.3f, 1.0f);

            // Do this while WindowShouldClose = GL_FALSE
            while (!glfwWindowShouldClose(glfwWindow))
            {

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear framebuffer

                // Draw


                glfwSwapBuffers(glfwWindow); // Swap color buffers
                glfwPollEvents(); // Poll window events
            }
        }

    //
}
