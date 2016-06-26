/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.poslovnik.exception.NoSuchPersonException;
import com.poslovnik.gson.GsonWrapper;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import com.poslovnik.service.PayoutService;
import com.poslovnik.service.PersonService;
import com.poslovnik.service.VacationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author mixa
 */
public class VacationServlet extends HttpServlet {

    JSONObject json = new JSONObject();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String action = request.getParameter("action");
        ActionType tip = ActionType.getForAction(action);

        switch (tip) {
            case LIST:
                listAction(request, response);
                break;
            default:
                response.sendError(400, "Bad request - unknown action requested");

                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ActionType tip = ActionType.getForAction(action);

        Vacation v = new Vacation();

        if (tip != tip.DELETE) {
            v = getObjectFromRequest(request);
        }

        switch (tip) {
            case ADD:
                addAction(request, v);
                break;
            case EDIT:
                editAction(request, v);
                break;
            case DELETE:
                deleteAction(request);
                break;
            default:
                throw new ServletException("Unknown action requested!");
        }

        response.getWriter().print(json.toString());
    }

    private void listAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person p;

        try {
            Integer personId = Integer.parseInt(request.getParameter("id"));

            p = PersonService.getInstance().findById(personId);

            if (p == null) {
                throw new NoSuchPersonException();
            }
        } catch (NumberFormatException | NoSuchPersonException ex) {
            response.sendError(400, "Bad request - missing/invalid person ID");

            return;
        }

        Collection<Vacation> vacations  = p.getVacationCollection();

        Gson gson = GsonWrapper.getGson();

        String vacationsJsonArray = gson.toJson(vacations);

        response.getOutputStream().print(vacationsJsonArray);
    }

    private void addAction(HttpServletRequest request, Vacation v) throws IOException {
        Integer personId = Integer.parseInt(request.getParameter("person_id"));
        
        VacationService.getInstance().add(v);

        json.put("success", true);
    }

    private Vacation getObjectFromRequest(HttpServletRequest request) throws IOException {
        Vacation v = new Vacation();

        Gson gson = GsonWrapper.getGson();

        VacationBean bean = new VacationBean();

        BufferedReader br = new BufferedReader(request.getReader());

        bean = gson.fromJson(br.readLine(), VacationBean.class);

        Person person = PersonService.getInstance().findById(bean.getPerson_id());

        if (bean.getId() != null) {
            v = person.getVacationById(bean.getId());
        }

        v.setDateFrom(bean.getDate_from());
        v.setDateTo(bean.getDate_to());
        v.setStatus(bean.getStatus());

        v.setPersonId(person);

        return v;
    }

    private void editAction(HttpServletRequest request, Vacation v) {
        VacationService.getInstance().edit(v);

        json.put("success", true);
    }

    private void deleteAction(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));

        Vacation v = VacationService.getInstance().findById(id);

        VacationService.getInstance().delete(v);

        json.put("success", true);
    }

    private class VacationBean {
;

        @Expose
        private Integer id;

        @Expose
        private Date date_from;
        
        @Expose
        private Date date_to;

        @Expose
        private String status;

        @Expose
        private Integer person_id;

        public Integer getId() {
            return id;
        }

        public Date getDate_from() {
            return date_from;
        }

        public Date getDate_to() {
            return date_to;
        }

        public String getStatus() {
            return status;
        }

        public Integer getPerson_id() {
            return person_id;
        }

        

    }
}
