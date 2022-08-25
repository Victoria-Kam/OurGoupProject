package com.example.countryrest.controller;

import com.example.countryrest.entity.Country;
import com.example.countryrest.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/country")
public class CountryController {

    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }


    @Operation(method = "GET", description = "Get all countries.", tags = "countries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Country not found"
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the countries.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Country.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        Collection<Country> countries = service.getAll();
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ArrayList<>(countries), HttpStatus.OK);
    }

    @Operation(method = "GET", description = "Get country by name", tags = "countries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Country not found"
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Found country",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)
                    )
            )
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Country> getByName(@PathVariable String name) {
        Country country = service.findCountry(name);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @Operation(method = "GET", description = "Find country by ID.", tags = "countries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Country not found"
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Country is found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Country> findCountry(@PathVariable int id) {
        Optional<Country> countryOptional = service.getById(id);
        if (countryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(countryOptional.get(), HttpStatus.OK);
    }


    @Operation(method = "POST", description = "save country", tags = "countries")
    @ApiResponse(
            responseCode = "200",
            description = "Country is saved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Country.class)
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Country save(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Country.class)
    )) @RequestBody Country country) {
        service.save(country);
        return country;
    }

    @Operation(method = "GET", description = "update country", tags = "countries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Country not found"
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Country is found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Country> update(@Parameter(description = "country id", required = true) @PathVariable int id,
                                          @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                  mediaType = "application/json",
                                                  schema = @Schema(implementation = Country.class)))
                                          @RequestBody Country country) {

        Optional<Country> dbCountryOptional = service.getById(id);
        if (dbCountryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Country dbCountry = dbCountryOptional.get();
        dbCountry.setName(country.getName());
        dbCountry.setCapital(country.getCapital());
        dbCountry.setCurrency(country.getCurrency());
        dbCountry.setPopulation(country.getPopulation());
        service.update(dbCountry);
        return new ResponseEntity<>(dbCountry, HttpStatus.OK);
    }

    @Operation(method = "DELETE", description = "delete country", tags = "countries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Country not found"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Country is deleted"
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@Parameter(description = "country id", required = true) @PathVariable int id) {
        Optional<Country> dbCountryOptional = service.getById(id);
        if (dbCountryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.delete(dbCountryOptional.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}