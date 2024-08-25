package com.prography.yakgwa.domain.auth.controller;

import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AuthControllerTest extends ControllerUnitTestEnvironment {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

}