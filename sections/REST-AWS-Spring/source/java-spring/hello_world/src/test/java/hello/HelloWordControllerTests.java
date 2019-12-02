package hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloWordControllerTests {

    @Autowired
    private WebApplicationContext wac;
    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MvcResult result = mvc
                .perform(
                        get("/hello")
                                .accept(
                                        MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
    }
}
