package me.wired.learning.user;

import me.wired.learning.common.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//@Controller
@RequestMapping(value = "/api/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class XUserController extends BaseController {

    private final XUserService xUserService;
    private final ModelMapper modelMapper;

    private XUserController(XUserService xUserService, ModelMapper modelMapper) {
        this.xUserService = xUserService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid XUserDto userDto, Errors errors) {
        if (errors.hasErrors())
            return badRequest(errors);

        Optional<XUser> optionalTempUser = xUserService.findByVariableId(userDto.getVariableId());
        if (optionalTempUser.isPresent()) {
            errors.rejectValue("variableId", "exists", "variableId already exists");
            errors.reject("exists", "variableId already exists");
            return badRequest(errors);
        }

        XUser user = modelMapper.map(userDto, XUser.class);
        XUser newUser = xUserService.save(user);
        XUserResource userResource = new XUserResource(newUser);
        userResource.add(new Link("/static/docs/index.html#resources-user-create").withRel("profile"));
        return ResponseEntity.created(URI.create(userResource.getLink("self").getHref())).body(userResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity readUser(@PathVariable String id) {
        Optional<XUser> optionalUser = xUserService.findById(id);
        if (!optionalUser.isPresent())
            return ResponseEntity.notFound().build();

        XUser user = optionalUser.get();
        XUserResource userResource = new XUserResource(user);
        userResource.add(new Link("/static/docs/index.html#resources-user-read").withRel("profile"));
        return ResponseEntity.ok().body(userResource);
    }

    @GetMapping
    public ResponseEntity readUsers(Pageable pageable, PagedResourcesAssembler<XUser> assembler) {
        Page<XUser> page = xUserService.findAll(pageable);
        PagedResources<Resource<XUser>> resources = assembler.toResource(page, e -> new XUserResource(e));
        resources.add(linkTo(XUserController.class).withRel("create-user"));
        resources.add(new Link("/static/docs/index.html#resources-user-create").withRel("profile"));
        return ResponseEntity.ok().body(resources);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody @Valid XUserDto userDto, Errors errors, @CurrentXUser XUser xUser) {
        if (errors.hasErrors())
            return badRequest(errors);

        Optional<XUser> optionalUser = xUserService.findById(id);
        if (!optionalUser.isPresent())
            return ResponseEntity.notFound().build();

        XUser oldUser = optionalUser.get();
        if (!xUser.isAdmin() && !oldUser.getVariableId().equals(xUser.getVariableId()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        modelMapper.map(userDto, oldUser);
        oldUser.setId(id); // map() 수행했을 때 id 값이 variableId 값으로 변경되는 문제로 추가
        XUser newUser = xUserService.save(oldUser);
        XUserResource userResource = new XUserResource(newUser);
        userResource.add(new Link("/static/docs/index.html#resources-user-update").withRel("profile"));
        return ResponseEntity.ok().body(userResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id, @CurrentXUser XUser xUser) {
        Optional<XUser> optionalUser = xUserService.findById(id);
        if (!optionalUser.isPresent())
            return ResponseEntity.notFound().build();

        if (!xUser.isAdmin() && !optionalUser.get().getVariableId().equals(xUser.getVariableId()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        xUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
