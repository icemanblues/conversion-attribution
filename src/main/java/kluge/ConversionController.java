package kluge;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversionController {

    public static final String COOKIE_PREFIX = "myAdNetwork";
    public static final String DELIM = "-";
    public static final String PIXEL_PATH = "src/main/resources/transparent-pixel.png";

    // Load image pixels into memory. So responding is instant
    byte[] pixelBytes;
    @PostConstruct
    public void init() throws IOException {
        pixelBytes = Files.readAllBytes(new File(PIXEL_PATH).toPath());
    }

    @RequestMapping(value="/image/track", method=RequestMethod.GET, produces=MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> track(@RequestParam String cId,
                                        @RequestParam String aId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {

        // Create the cookie, using a structure name and value scheme
        String cookieName = COOKIE_PREFIX + DELIM + cId;
        String cookieValue = cId + DELIM + aId;
        Cookie c = new Cookie(cookieName, cookieValue);
        response.addCookie(c);

        // TODO:
        // Should log this information back to our servers
        // the user session, the headers, the cookies
        // This is important for tracking impressions/ eyeballs

        return new ResponseEntity<>(pixelBytes, HttpStatus.OK);
    }

    @RequestMapping(value="/image/convert", method=RequestMethod.GET, produces=MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> convert(@RequestParam String cId,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        final String cIdCookieName = COOKIE_PREFIX + DELIM + cId;

        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            // NOTE: Based on how we structure the Cookie name we can see the other campaign/ads that this user had viewed
            // record that information and send it back for further data science discovery
            String cookieName = c.getName();
            if(cIdCookieName.equals(cookieName)) {
                String cookieValue = c.getValue();
                String[] tokens = cookieValue.split(DELIM);
                String cookieCId = tokens[0];
                String aId = tokens[1];

                // TODO:
                // record which campaign and ad combo created this event
                System.out.println("You saw my ad, this is my conversion cid="+cId+" aId="aId);
            }
        }
        return new ResponseEntity<>(pixelBytes, HttpStatus.OK);
    }
}
