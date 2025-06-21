package com.shongon.restaurant_backend.service.blueprint;

import com.shongon.restaurant_backend.domain.GeoLocation;
import com.shongon.restaurant_backend.domain.entities.Address;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}
