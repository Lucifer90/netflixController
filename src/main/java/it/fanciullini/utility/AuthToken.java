package it.fanciullini.utility;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@Data
public class AuthToken {

    private String session;

}
