package com.embeddedmeng.common.base;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public Gson gson = new Gson();
}
