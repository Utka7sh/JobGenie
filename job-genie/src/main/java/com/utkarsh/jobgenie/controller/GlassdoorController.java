package com.utkarsh.jobgenie.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/redirect")
public class GlassdoorController {

    @GetMapping("/glassdoor")
    public void redirectToGlassdoor(
            @RequestParam(defaultValue = "java") String role,
            @RequestParam(defaultValue = "7") String fromAge,
            @RequestParam(defaultValue = "entrylevel") String seniorityType,
            HttpServletResponse response
    ) throws IOException {
        String sanitizedRole = role.toLowerCase().replaceAll("\\s+", "-");
        String baseUrl = "https://www.glassdoor.co.in/Job/india-" + sanitizedRole + "-jobs-SRCH_IL.0,5_IN115_KO6,10.htm";
        String fullUrl = baseUrl +
                "?minRating=4.0" +
                "&fromAge=" + fromAge +
                "&jobTypeIndeed=7EQCZ" +
                "&sgocId=1007" +
                "&seniorityType=" + seniorityType;

        response.sendRedirect(fullUrl);
    }
}
