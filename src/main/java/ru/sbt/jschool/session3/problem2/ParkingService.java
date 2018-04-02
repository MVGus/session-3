package ru.sbt.jschool.session3.problem2;

public interface ParkingService {


    boolean entryParking(long id, long timeParking);
    Long exitParking(long id, long timeParking);

}

