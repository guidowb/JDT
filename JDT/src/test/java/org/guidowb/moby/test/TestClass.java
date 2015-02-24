package org.guidowb.moby.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
public class TestClass extends TestBase implements TestInterface1, TestInterface2 {

}
