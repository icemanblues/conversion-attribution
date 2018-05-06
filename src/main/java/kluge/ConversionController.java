package kluge;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversionController {
    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public ResponseEntity<String> singleSignOn(@RequestParam String cId,
                                               @RequestParam String aId,
                                               HttpServletResponse response) {

        // Testing work with cookies
//        Cookie c = new Cookie("heroku-nav-data", navData);
//        response.addCookie(c);

        return new ResponseEntity<String>("Hello", HttpStatus.OK);

    }
}
