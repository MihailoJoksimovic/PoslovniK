/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

/**
 *
 * @author mixa
 */
public enum ActionType {
    LIST("list"), ADD("add"), EDIT("edit"), DELETE("delete");

    private final String action;

    private ActionType(String action) {
        this.action = action;
    }

    public static ActionType getForAction(String action) {
        for (ActionType tipAkcije : values()) {
            if (tipAkcije.action.equals(action)) {
                return tipAkcije;
            }
        }
        return LIST;
    }
}
