import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {


    public static List<CustomerThread> customerLine = new Vector<>();
    public static List<FloorClerkThread> activeFloorClerk = new Vector<>();
    public static AtomicInteger currentActiveFloorClerkIndex= new AtomicInteger(0);
    public static AtomicInteger customersServed = new AtomicInteger(0);
    public static volatile boolean selfCheckout1isBusy = false;
    public static volatile boolean selfCheckout2isBusy = false;
    public static volatile boolean selfCheckout3isBusy = false;
    public static List<CustomerThread> selfCheckoutLine = new Vector<>();
    public static AtomicInteger customerNumberPicked = new AtomicInteger(1);
    public static Vector<CustomerThread> storageRoomLine = new Vector<>();
    public static AtomicInteger currentStorageLineIndex = new AtomicInteger(0);
    public static volatile int currentStorageLineSize = 0;
    public static ArrayList<StorageClerkThread> availableStorageClerks = new ArrayList<>();
    public static ArrayList<FloorClerkThread> availableFloorClerks = new ArrayList<>();
    public static volatile int customersFinished = 0;
    public static Vector<CustomerThread> customersInOrder = new Vector<>();
        public static volatile int highestId;
    public static volatile int lowestId;
    public static void main(String[] args) throws InterruptedException {

         int num_customers = 20;
         int num_floorClerks = 3;
         int num_storageClerks = 4;

        for (int i = 0; i< num_customers; i++){


//          4 storageClerks are created before any other thread, because all other threads are dependent on storageClerks to progress.

            if (i < num_storageClerks){
                StorageClerkThread storageClerkThread = new StorageClerkThread(i);
                availableStorageClerks.add(storageClerkThread);
                storageClerkThread.start();
            }

/*
            3 floorClerks are created afterward, because the progress of customerThreads are dependent on whether
            floorClerks can process them.
 */
            if (i < num_floorClerks){
                FloorClerkThread floorClerkThread = new FloorClerkThread(i);
                availableFloorClerks.add(floorClerkThread);
                floorClerkThread.start();
            }

//          Finally, 20 customers are created and the storytelling begins!
            CustomerThread customerThread = new CustomerThread(i);
            customersInOrder.add(customerThread);
            customerThread.start();
        }

        int k = 20;
        while(k !=0 ){
            k--;
            while(customersInOrder.get(k).isAlive()){
                highestId = customersInOrder.get(k).customerNumber;
            }
        }






    }
}