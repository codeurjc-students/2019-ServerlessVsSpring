package hello;

import jdk.nashorn.internal.runtime.Context;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWordController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> hello(@RequestParam(required = false) final Object input, @RequestParam(required = false) final Context context) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Custom-Header", "application/json");
        String output = "{ \"message\": \"hello world\"}";
        return new ResponseEntity<String>(output, headers, HttpStatus.OK);
    }
}