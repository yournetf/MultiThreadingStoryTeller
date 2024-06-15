public class CustomerThread extends Thread {

    public int customerNumber;
    public boolean foundItem = false;
    public boolean turnInLine = false;
    public int slipNumber;
    public CustomerThread(int customerNumber){
        this.customerNumber = customerNumber;
    }
    public boolean customerHasNotPayed = true;
    public int numberPicked;
    @Override
    public void run() {
//        While loop to busy wait until an item is found
//        (Simulates searching for an item)
        while(foundItem == false){
//            If statement that gives a ten percent chance for an item to be found
//            (Simulates preferences and allows for unique pathing in a multithreading scenario)
            if (Math.random() < 0.2) foundItem = true;
            else {
                foundItem = false;
                System.out.println("Customer " + customerNumber + ":      Is still looking for an item... ");
                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

               // System.out.println("Customer " + customerNumber + ":      is still looking for an item...");
            }
//            Tells the thread to sleep, so there is a 1-second buffer between attempts to
//            leave the busy wait
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }//        The customer is released from the busy wait because (s)he has found what they would like to buy

        System.out.println("Customer " + customerNumber + ":      Found an item to buy! ");




//        The customer is then added to the main class' vector.This is done in order to preserve which
//        customer is on the line, in a synchronized (FCFS/FIFO) fashion.
        Main.customerLine.add(this);




//        Here we have another busy wait to simulate when a customer is waiting on the line for
//        their turn.
        while(turnInLine == false) {
            //Busy Wait
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }/*      The customer breaks out of the "while(turnInLine == false)" loop,
              meaning that they were successfully served a ticket when it became their turn in line.*/




        Main.selfCheckoutLine.add(this);
        System.out.println("Customer " + this.customerNumber + ":      Got on the checkout line");



//      The customer is now on the line and busy waiting to pay
        while(customerHasNotPayed){


//          If all the selfCheckout machines are being used, then the customer must busy wait
            while(Main.selfCheckout1isBusy && Main.selfCheckout2isBusy && Main.selfCheckout3isBusy){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }//Since the customer has released from this while loop, a selfCheckout machine is now available


//          Checks to see if this customer is the next one on the line for the selfCheckout machines.
//          If so, determine which selfCheckout machine is open and pay there.
                if(!Main.selfCheckout1isBusy){
                    Main.selfCheckout1isBusy = true;
                    try {
                        selfCheckout(this);
                        System.out.println("Customer " + this.customerNumber + ":      Just payed at self checkout 1" );
                        Main.selfCheckout1isBusy = false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(!Main.selfCheckout2isBusy){
                    Main.selfCheckout2isBusy = true;
                    try {
                        selfCheckout(this);
                        System.out.println("Customer " + this.customerNumber + ":      Just payed at self checkout 2" );
                        Main.selfCheckout2isBusy = false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(!Main.selfCheckout3isBusy){
                    Main.selfCheckout3isBusy = true;
                    try {
                        selfCheckout(this);
                        System.out.println("Customer " + this.customerNumber + ":      Just payed at self checkout 3" );
                        Main.selfCheckout3isBusy = false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        }//Customer leaves the while loop, meaning they successfully paid.

        //The customer had a lightweight item and left the store.
        if(Math.random() < 0.3){
            System.out.println("Customer " + customerNumber + ":      Bought a lightweight item and left the store. Have a nice day :)" );
        }

        //The customer had a heavyweight item and will continue the story!
        else{
                //The customer heads to the pickup service.
                System.out.println("Customer " + customerNumber + ":      Bought a heavyweight item and is heading to the pickup service");
                this.yield();
                this.yield();

                //Customer gets hungry on the way.
                System.out.println("Customer " + customerNumber + ":      Got hungry so they took a break...");

                //Customer takes a break for a random time (max 3 seconds).
                try {
                    this.sleep((int) (Math.random() * 3000) );
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //The customer ends their break and arrives at the storage room to pick their number.
                System.out.println("Customer " + customerNumber + ":      Arrived to the storage room and picked the number " + Main.customerNumberPicked);
                Main.customerNumberPicked += 1;

        }





    }

    private void selfCheckout(Thread t) throws InterruptedException {
        increasePriority(t);
        pay(t);
        decreasePriority(t);
    }

    private void increasePriority(Thread t){
        int currentPRI = t.getPriority();
        t.setPriority(currentPRI + 1);
    }

    private void pay(Thread t) throws InterruptedException {
        t.sleep((int) (Math.random() * 3000) );
        this.customerHasNotPayed = false;
    }

    private void decreasePriority(Thread t){
        int currentPRI = t.getPriority();
        t.setPriority(currentPRI - 1);
    }


}


