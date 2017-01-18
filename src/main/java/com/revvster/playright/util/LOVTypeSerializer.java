/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.revvster.playright.model.LOVType;
import java.lang.reflect.Type;

/**
 *
 * @author rahulkk
 */
public class LOVTypeSerializer implements JsonSerializer<LOVType>{

    @Override
    public JsonElement serialize(final LOVType t, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", t.toString());
        jsonObject.addProperty("editable", t.isEditable());
        jsonObject.addProperty("context", t.getContext().getContextLevel());        
        return jsonObject;
    }
    
}
