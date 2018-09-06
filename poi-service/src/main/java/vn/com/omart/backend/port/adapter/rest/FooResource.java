//package vn.com.omart.backend.port.adapter.rest;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
//import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
//
//@RestController
//public class FooResource {
//
//    public FooResource() {
//        super();
//    }
//
//    // API - read
//    @PreAuthorize("#oauth2.hasScope('foo') and #oauth2.hasScope('read')")
//    @RequestMapping(method = RequestMethod.GET, value = "/foos/{id}")
//    @ResponseBody
//    public Foo findById(@PathVariable final long id) {
//        return new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
//    }
//
//    // API - write
//    @PreAuthorize("#oauth2.hasScope('foo') and #oauth2.hasScope('write')")
//    @RequestMapping(method = RequestMethod.POST, value = "/foos")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public Foo create(@RequestBody final Foo foo) {
//        foo.setId(Long.parseLong(randomNumeric(2)));
//        return foo;
//    }
//
//    @Data
//    @AllArgsConstructor
//    public static class Foo {
//        private long id;
//        private String name;
//    }
//
//}
