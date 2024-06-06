package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.arman.entity.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByName(String name);
    List<Location> findByAddress_CountryIgnoreCase(String country);

    @Query("select l from Location l where lower(l.address.country) = ?1 and " +
            "(lower(l.name) like %?2% or " +
            "lower(l.address.city) like %?2% or " +
            "lower(l.address.state) like %?2% or " +
            "lower(l.address.streetAddress) like %?2%)")
    List<Location> searchCountryLocations(String country, String query);
}