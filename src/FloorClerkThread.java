import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FloorClerkThread extends Thread{

    public int id;

    public AtomicBoolean isWorking = new AtomicBoolean(false);

    public FloorClerkThread(int id){
        this.id = id;
    }

    @Override
    public void run(){

//      Will continuously run the process of waiting to see if someone is on the line,
//      as well as helping them until all the customers in the store have been taken care of
        while(Main.customersServed.get() < 20) {
//          Busy wait, to simulate the floor clerk who is waiting for someone to get on the line.
            while((Main.customerLine.size() < (Main.customersServed.get() + 1)) && Main.customersServed.get() < 20){
                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
//          The floor clerk escapes from the busy wait, signaling that the
//          line is no empty and now the floor clerk must do the next step.


//          Adds the floor clerk to a list of floor clerks who are able to help the customer.

            Main.activeFloorClerk.add(this);


//          Checks to see who was the first floor clerk was to arrive to the customers aid.
            if ((Main.activeFloorClerk.get(Main.currentActiveFloorClerkIndex.get()) == this) && Main.customersServed.get() < 20){


//              Makes the floorClerk wait for another customer to get on the line before it continues serving
                while (Main.customersServed.get() > Main.customerLine.size()-1){}

//              Gets the current customer in line, and changes its value to in line.
//              Increments the atomic variable "slipNumber" and assigns it to the customer.
                CustomerThread currentCustomer = Main.customerLine.get(Main.customersServed.get());
                currentCustomer.turnInLine = true;
                currentCustomer.slipNumber = Main.customersServed.get() + 1;
                System.out.println("FloorClerk " +id+ ":      Served customer " + currentCustomer.customerNumber +
                        " slip number " + currentCustomer.slipNumber);
                Main.customersServed.getAndIncrement();
                //Resets the index to once again determine which floorClerk will arrive to the next customer.
                Main.currentActiveFloorClerkIndex.getAndIncrement();
            }


        }

//      Thread leaves the while(customersServed < 20) loop, meaning that
//      every customer in the store has been served
        System.out.println("FloorClerk " +id+ ":      All the customers are served, now I'll rest!");
        try {
            this.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    }




