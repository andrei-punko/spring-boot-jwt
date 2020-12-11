package by.andd3dfx.controller;

import by.andd3dfx.domain.City;
import by.andd3dfx.domain.Foo;
import by.andd3dfx.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@RestController
public class FooController {

    @Autowired
    private CityRepository cityRepository;

    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    @GetMapping("/foos/{id}")
    public Foo findById(@PathVariable long id) {
        return new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

    @PreAuthorize("hasAuthority('ADMIN_USER')")
    @GetMapping("/cities")
    public Iterable<City> getCities() {
        return cityRepository.findAll();
    }
}
