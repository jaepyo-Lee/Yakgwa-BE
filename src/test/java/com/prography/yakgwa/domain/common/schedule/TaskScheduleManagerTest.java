package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TaskScheduleManagerTest extends IntegrationTestSupport {
    @AfterEach
    void init(){
        deleter.deleteAll();
    }


}