package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.annotations.security.AdminOnly;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.sal.api.websudo.WebSudoManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MyPluginServlet extends HttpServlet {

    private static final String PLUGIN_STORAGE_KEY = "com.atlassian.plugins.tutorial.refapp.adminui";
    private static final Logger log = LoggerFactory.getLogger(MyPluginServlet.class);

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final WebSudoManager webSudoManager;
    private final TemplateRenderer templateRenderer;
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public MyPluginServlet(final UserManager userManager,
                           final LoginUriProvider loginUriProvider,
                           final WebSudoManager webSudoManager,
                           final TemplateRenderer templateRenderer,
                           final PluginSettingsFactory pluginSettingsFactory) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.webSudoManager = webSudoManager;
        this.templateRenderer = templateRenderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @AdminOnly
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        UserProfile user = userManager.getRemoteUser(request);
//        log.info("User: " + user);
//
//        Boolean isUserAdmin = false;
//        if (user != null) {
//            isUserAdmin = userManager.isSystemAdmin(user.getUserKey());
//            if (isUserAdmin) {
//                try {
//                    webSudoManager.willExecuteWebSudoRequest(request);
//                    response.setContentType("text/html");
//                    response.getWriter().write("<html><body>Hi again! Looking good.</body></html>");
//                    return;
//                } catch (Exception e) {
//                    webSudoManager.enforceWebSudoProtection(request, response);
//                    return;
//                }
//            }else {
//            }
//        }
//        redirectToLogin(request, response);

        //    Create a GUI with templates and AUI
        String userName = userManager.getRemoteUsername(request);
        log.info("userName: " + userName);
        if (userName == null || !userManager.isSystemAdmin(userName)){
            redirectToLogin(request, response);
            return;
        }
        Map<String, Object> context = new HashMap<String, Object>();

        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        if (pluginSettings.get(PLUGIN_STORAGE_KEY + ".name") == null){
            String noName = "Enter a name here.";
            pluginSettings.put(PLUGIN_STORAGE_KEY +".name", noName);
        }

        if (pluginSettings.get(PLUGIN_STORAGE_KEY + ".age") == null){
            String noAge = "Enter an age here.";
            pluginSettings.put(PLUGIN_STORAGE_KEY + ".age", noAge);
        }
        context.put("name", pluginSettings.get(PLUGIN_STORAGE_KEY + ".name"));
        context.put("age", pluginSettings.get(PLUGIN_STORAGE_KEY + ".age"));
        response.setContentType("text/html;charset=utf-8");
        templateRenderer.render("admin.vm", response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".name", req.getParameter("name"));
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".age", req.getParameter("age"));
        response.sendRedirect("test");
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