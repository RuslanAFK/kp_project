package helpers;

import models.Status;

import java.util.Random;

public class Helper {
    public static int randomIntInRange(int max, int min) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static Status generateRandomStatus() {
        Random random = new Random();
        if(random.nextInt()%7==0){
            return Status.DISABLED;
        }
        if(random.nextInt()%9==0){
            return Status.WITH_CHILD;
        }
        if(random.nextInt()%11==0){
            return Status.CHILD;
        }
        if(random.nextInt()%13==0){
            return Status.STUDENT;
        }
        return Status.NONE;
    }
}
