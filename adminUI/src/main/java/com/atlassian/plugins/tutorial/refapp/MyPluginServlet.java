package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.annotations.security.AdminOnly;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.sal.api.websudo.WebSudoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static java.util.Objects.requireNonNull;

public class MyPluginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MyPluginServlet.class);

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final WebSudoManager webSudoManager;

    @Inject
    public MyPluginServlet(final UserManager userManager,
                           final LoginUriProvider loginUriProvider,
                           final WebSudoManager webSudoManager) {
        this.userManager = requireNonNull(userManager, "userManager");
        this.loginUriProvider = requireNonNull(loginUriProvider, "loginUriProvider");
        this.webSudoManager = requireNonNull(webSudoManager, "webSudoManager");
    }

    @AdminOnly
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserProfile user = userManager.getRemoteUser(request);
        log.info("User: " + user);

        Boolean isUserAdmin = false;
        if (user != null) {
            isUserAdmin = userManager.isSystemAdmin(user.getUserKey());
            if (isUserAdmin) {
                try {
                    webSudoManager.willExecuteWebSudoRequest(request);
                    response.setContentType("text/html");
                    response.getWriter().write("<html><body>Hi again! Looking good.</body></html>");
                    return;
                } catch (Exception e) {
                    webSudoManager.enforceWebSudoProtection(request, response);
                    return;
                }
            }else {
            }
        }
        redirectToLogin(request, response);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }

}