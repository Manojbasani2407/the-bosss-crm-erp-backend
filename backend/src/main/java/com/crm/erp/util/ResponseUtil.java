package com.crm.erp.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> createResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}
