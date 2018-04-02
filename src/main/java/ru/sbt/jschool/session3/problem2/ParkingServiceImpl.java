package ru.sbt.jschool.session3.problem2;

import java.util.HashMap;
import java.util.Map;

public class ParkingServiceImpl implements ParkingService {
    long sizeParking;
    long price;
    Map<Long,Long> map = new HashMap<>();

    public ParkingServiceImpl(long sizeParking, long price) {
        this.sizeParking = sizeParking;
        this.price = price;
    }



    @Override
    public boolean entryParking(long id, long timeParking){
        if(map.get(id)!=null){
            return false;
        }
        if(sizeParking<=0){
            return false;
        }
        map.put(id,timeParking);
        sizeParking--;
        return true;
    }

    @Override
    public Long exitParking(long id, long timeExit) {
        Long entryTime = map.get(id);
        if(entryTime==null){
            return null;
        }
        if(entryTime>timeExit){
            return null;
        }

        long priceParking = 0;

        for (long i = entryTime; i < timeExit; i++) {
            if((i+1)%24>6){
                priceParking+=price;
            }
            else{
                priceParking+=price*2;
            }
        }
        sizeParking++;
        return priceParking;
    }
}

