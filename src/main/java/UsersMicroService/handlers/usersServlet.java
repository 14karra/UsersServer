package UsersMicroService.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import UsersMicroService.Main;
import UsersMicroService.Models.Answer;
import UsersMicroService.Models.Clients;
import UsersMicroService.classes.Client;
import UsersMicroService.utils.Common;
; // TODO: create log file for GET / POST requests and delete this import + Getters and Setters in Main class

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @See https://www.tutorialspoint.com/servlets/servlets-form-data.htm for examples
 */
public class usersServlet extends HttpServlet {

    private static Logger usersLog = LoggerFactory.getLogger(usersServlet.class.getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req != null) {
            try {
                String reqStr = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
                if (StringUtils.isBlank(reqStr)) {
                    resp.setContentType("application/json");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println(Common.getPrettyGson().toJson(null));
                    return;
                }
                Client c = Common.getPrettyGson().fromJson(reqStr, Client.class);
                Clients.getPeopleTable().put(c.getClientId(), c);
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                resp.getWriter().println(Common.getPrettyGson().toJson(Clients.getPeopleTable().get(c.getClientId())));
            } catch (Exception e) {
                Main.getLog().error("PROCESSING ERROR.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("");
            }
        } else {
            Main.getLog().error("Empty request.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("EMPTY_REQUEST");
        }
    }

 // nd
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request != null) {
            String id;
            id = request.getParameter("clientId");
            if (id == null) {
                Main.getLog().error("Error on clientId, value not provided.");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(Common.getPrettyGson().toJson(
                        new Answer("BAD_REQUEST", "No clientId provided")));
                return;
            }
            try {
                if (Clients.getPeopleTable().containsKey(id)) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    response.getWriter().println(Common.getPrettyGson().toJson(Clients.getPeopleTable().get(id)));
                    Main.getLog().error("Object found. Provided id: " + id);
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().println(Common.getPrettyGson().toJson(
                            new Answer("NO_CONTENT","Client Not found")));
                    Main.getLog().error("Object not found. Provided id: " + id);
                }
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(Common.getPrettyGson().toJson(
                        new Answer("INTERNAL_SERVER_ERROR","An internal Error occurred on server UsersMicroService")));
                Main.getLog().error("Error. " + e.getMessage());
            }
        } else {
            Main.getLog().error("Empty request.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(Common.getPrettyGson().toJson(
                    new Answer("EMPTY_REQUEST", "The request provided was empty")));
        }
    }

    public static Logger getUsersLog() {
        return usersLog;
    }

    public static void setUsersLog(Logger usersLog) {
        usersServlet.usersLog = usersLog;
    }

    // TODO: 07.05.2020 Also consider overriding methods doHead, doPut, doDelete, doTrace, doOptions
    // TODO: 07.05.2020 read чистый код
}
