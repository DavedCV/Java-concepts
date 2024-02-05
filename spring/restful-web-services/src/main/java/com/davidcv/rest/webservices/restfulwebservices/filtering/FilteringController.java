package com.davidcv.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public SomeBean filtering() {
        return new SomeBean("value1", "value2", "value3");
    }

    @GetMapping("/filtering/list")
    public List<SomeBean> filteringList() {
        return List.of(new SomeBean("value1", "value2", "value3"), new SomeBean("value1.2", "value2.2", "value3.2"),
                       new SomeBean("value1.3", "value2.3", "value3.3"));
    }

    @GetMapping("/filteringDynamic")
    public MappingJacksonValue filteringDynamic() { // Return field 1 - field 3
        SomeBean someBean = new SomeBean("value1", "value2", "value3");

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field3");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    @GetMapping("/filteringDynamic/list")
    public MappingJacksonValue filteringListDynamic() { // Return field 2 - field 3
        List<SomeBean> someBeans = List.of(new SomeBean("value1", "value2", "value3"),
                                           new SomeBean("value1.2", "value2.2", "value3.2"),
                                           new SomeBean("value1.3", "value2.3", "value3.3"));

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBeans);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }
}
