package com.example.demo.utils;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class JSONUtil {

    public static ResponseEntity buildError (String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("status",HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(400).body(jsonObject.toString());
    }
}
