package UsersMicroService;

import UsersMicroService.classes.fileProcessor;
import UsersMicroService.handlers.usersServlet;
import UsersMicroService.utils.Common;
import UsersMicroService.utils.PropertyManager;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class.getSimpleName());

    private static Server server;

    private static FileWriter file;

    public static void main(String[] args) throws Exception {
        PropertyManager.load();
        Common.configure();
        fileProcessor.loadFromJsonFile();
        runServer();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileProcessor.save2JsonFile();
                } catch (IOException e) {
                    log.error("Error: ", e);
                }
                stopServer();
            }
        }, "Stop Jetty Hook"));
    }

    private static void runServer()  {
        int port = PropertyManager.getPropertyAsInteger("server.port", 7000);
        String contextStr = PropertyManager.getPropertyAsString("server.context", "/");

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextStr);
        server.setHandler(context);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(usersServlet.class, "/userData");

        try
        {
            server.start();
            log.info("Server has started at port: " + port);
            server.join();
        }catch(Throwable t){
            log.error("Error while starting server.", t);
        }
    }

    private static void stopServer() {
        try {
            if(server.isRunning()){
                server.stop();
            }
        } catch (Exception e) {
            log.error("Error while stopping server", e);
        }
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        Main.log = log;
    }
}

