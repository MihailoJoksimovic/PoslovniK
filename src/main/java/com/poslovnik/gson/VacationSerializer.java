/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

/**
 *
 * @author mixa
 */
public class VacationSerializer implements JsonSerializer<Vacation>
{
    @Override
    public JsonElement serialize(Vacation t, Type type, JsonSerializationContext jsc) {
        JsonObject obj = new JsonObject();
        
        obj.add("id", new JsonPrimitive(t.getId()));
        obj.add("person_id", new JsonPrimitive(t.getPersonId().getId()));
        obj.add("status", new JsonPrimitive(t.getStatus()));
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        obj.add("date_from", new JsonPrimitive(sdf.format(t.getDateFrom())));
        obj.add("date_to", new JsonPrimitive(sdf.format(t.getDateTo())));
        
        return obj;
    }
    
}
