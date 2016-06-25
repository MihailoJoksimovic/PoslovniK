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
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

/**
 *
 * @author mixa
 */
public class PayoutSerializer implements JsonSerializer<Payout>
{
    @Override
    public JsonElement serialize(Payout t, Type type, JsonSerializationContext jsc) {
        JsonObject obj = new JsonObject();
        
        obj.add("id", new JsonPrimitive(t.getId()));
        obj.add("person_id", new JsonPrimitive(t.getPersonId().getId()));
        obj.add("amount", new JsonPrimitive(t.getAmount()));
        obj.add("description", new JsonPrimitive(t.getDescription()));
        obj.add("type", new JsonPrimitive(t.getType()));
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        obj.add("date", new JsonPrimitive(sdf.format(t.getDate())));
        
        return obj;
    }
    
}
