package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.arman.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.name like %?1%")
    List<Location> findByName(String name);

    boolean existsByName(String name);
    List<Location> findByAddress_CountryIgnoreCase(String country);

    @Query("select l from Location l where lower(l.address.country) = ?1 and " +
            "(lower(l.name) like %?2% or " +
            "lower(l.address.city) like %?2% or " +
            "lower(l.address.state) like %?2% or " +
            "lower(l.address.streetAddress) like %?2%)")
    List<Location> searchCountryLocations(String country, String query);

    @Query("select l from Location l where lower(l.address.country) = ?1 and lower(l.name) = ?2")
    Optional<Location> findByCountryAndName(String country, String name);
}
