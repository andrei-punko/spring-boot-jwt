package by.andd3dfx.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

import by.andd3dfx.domain.City;
import by.andd3dfx.domain.Foo;
import by.andd3dfx.repository.CityRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FooControllerTest {

    private static final long ID = 1235L;

    @Mock
    CityRepository cityRepository;
    @InjectMocks
    FooController fooController;

    @Test
    public void findById() {
        Foo result = fooController.findById(ID);

        assertThat(result, notNullValue());
    }

    @Test
    public void getCities() {
        final List<City> cities = Arrays.asList(new City());
        when(cityRepository.findAll()).thenReturn(cities);

        Iterable<City> result = fooController.getCities();

        assertThat(result, is(cities));
    }
}
