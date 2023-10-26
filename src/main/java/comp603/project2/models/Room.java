package comp603.project2.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int roomId;
    private int hotelId; // foreign key from Hotel
    private String roomType; // e.g. "SINGLE", "DOUBLE"
    private double pricePerNight;
    private int capacity;
}
