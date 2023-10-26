package comp603.project2.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private int bookingId;
    private int roomId; // foreign key from Room
    private Date startDate;
    private Date endDate;
    private double price;
    private String username;
    private String phoneNumber;
    private Date bookingDate;
}
