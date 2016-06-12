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
public enum TipAkcije {
    LIST("list"), ADD("add"), EDIT("edit"), DELETE("delete");

    private final String action;

    private TipAkcije(String action) {
        this.action = action;
    }

    public static TipAkcije getForAction(String action) {
        for (TipAkcije tipAkcije : values()) {
            if (tipAkcije.action.equals(action)) {
                return tipAkcije;
            }
        }
        return LIST;
    }
}
