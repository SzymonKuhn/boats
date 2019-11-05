package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressToLngLat {
    private Results[] results;
    private String status;
}
