/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.poslovnik.exception.ValidationException;
import com.poslovnik.gson.GsonWrapper;
import com.poslovnik.model.data.Vacation;
import com.poslovnik.service.VacationService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.google.gson.Gson;
import java.text.ParseException;
import java.util.Collection;


/**
 *
 * @author mixa
 */
public class VacationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // For now - only one method is supported trough GET and that is LIST ;)
        listAction(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        ActionType actionType = ActionType.getForAction(action);
        
        switch (actionType) {
            case ADD:
                addAction(req, resp);
                break;
            case EDIT:
                editAction(req, resp);
                break;
            case DELETE:
                deleteAction(req, resp);
                break;
        }
    }
    
    private void listAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject responseJson = new JSONObject();
        
        Collection<Vacation> vacations = VacationService.getInstance().getVacationListFromRequest(req);
        
        String output = GsonWrapper.getGson().toJson(vacations);
        
        resp.getOutputStream().print(output);
    }

    private void addAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject responseJson = new JSONObject();
        
        try {
            Vacation vacation = VacationService.getInstance().createEntityFromRequest(req);
        
            Gson gson = GsonWrapper.getGson();

            String output = gson.toJson(vacation, Vacation.class);

            resp.getOutputStream().print(output);
        } catch (IOException ex) {
            resp.sendError(500, "Unknown error occurred (IOException)");
        } catch (ParseException ex) {
            resp.sendError(400, "Invalid request received (malformed JSON?)");
        }
    }

    private void editAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject responseJson = new JSONObject();
        
        try {
            Vacation vacation = VacationService.getInstance().editEntityFromRequest(req);
        
            Gson gson = GsonWrapper.getGson();

            String output = gson.toJson(vacation, Vacation.class);

            resp.getOutputStream().print(output);
        } catch (IOException ex) {
            resp.sendError(500, "Unknown error occurred (IOException)");
        }
    }

    private void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {        
        try {
            VacationService.getInstance().removeEntityFromRequest(req);

            resp.getOutputStream().print(new JSONObject("{success:true}").toString());
        } catch (IOException ex) {
            resp.sendError(500, "Unknown error occurred (IOException)");
        }
    }
    
}
