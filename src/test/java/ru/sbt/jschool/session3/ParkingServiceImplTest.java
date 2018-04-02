package ru.sbt.jschool.session3;

import org.junit.Test;
import ru.sbt.jschool.session3.problem2.ParkingService;
import ru.sbt.jschool.session3.problem2.ParkingServiceImpl;


import static org.junit.Assert.assertEquals;

public class ParkingServiceImplTest {

    @Test
    public void testClassicPrice() throws Exception {
        ParkingService accountService = new ParkingServiceImpl(3,1);
        accountService.entryParking(1,23);
        Long price = accountService.exitParking(1,34);
        assertEquals(18,(long)price);
    }

    @Test
    public void testClassicNull() throws Exception {
         ParkingService accountService = new ParkingServiceImpl(3,1);
         accountService.entryParking(1,23);
         Long price = accountService.exitParking(1,5);
         assertEquals(null,price);
    }

    @Test
    public void testNotSizeParking() throws Exception {
        ParkingService accountService = new ParkingServiceImpl(0,1);
        boolean a = accountService.entryParking(1,23);
        assertEquals(false,a);
    }
    @Test
    public void testRepeatCar() throws Exception {
        ParkingService accountService = new ParkingServiceImpl(4,1);
        accountService.entryParking(1,23);
        boolean a = accountService.entryParking(1,33);
        assertEquals(false,a);
    }
    @Test
    public void testErrorId() throws Exception {
        ParkingService accountService = new ParkingServiceImpl(3,1);
        accountService.entryParking(1,23);
        Long price = accountService.exitParking(2,34);
        assertEquals(null,price);
    }


}
