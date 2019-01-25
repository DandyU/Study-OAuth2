package me.wired.learning.index;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @GetMapping()
    public ResourceSupport index() {
        ResourceSupport resourceSupport = new ResourceSupport();
        return resourceSupport;
    }

}
