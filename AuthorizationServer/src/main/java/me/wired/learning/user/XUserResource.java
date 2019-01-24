package me.wired.learning.user;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class XUserResource extends Resource<XUser> {

    public XUserResource(XUser user, Link... links) {
        super(user, links);
        add(linkTo(XUserController.class).slash(user.getId()).withSelfRel());
        add(linkTo(XUserController.class).slash(user.getId()).withRel("update-user"));
        add(linkTo(XUserController.class).slash(user.getId()).withRel("delete-user"));
    }

}
