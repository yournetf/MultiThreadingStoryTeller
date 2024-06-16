import java.util.concurrent.atomic.AtomicBoolean;

public class StorageClerkThread extends Thread{

    public static long time = System.currentTimeMillis();
    public int id;
    public AtomicBoolean isBusy = new AtomicBoolean(false);
    public volatile boolean isBusy2 = false;

    public StorageClerkThread(int id){
        this.id = id;
    }

    @Override
    public void run(){
        while(Main.customersFinished <= 19){
            while(!isBusy.get()){/*    Busy Wait   */}
            System.out.println("["+(System.currentTimeMillis()-time)+"] "+"StorageClerk " + id + ": has started helping Customer " +
                    Main.storageRoomLine.get(Main.currentStorageLineIndex.get()).customerNumber + " with their heavyweight item!!!" );
            try {
                sleep((int) (Math.random() * 500) +100 );
            } catch (InterruptedException e) {
                System.out.println("["+(System.currentTimeMillis()-time)+"] "+"Store is closing, StorageClerk "+ this.id + " has been interrupted...");
            }

            while((Main.currentStorageLineIndex.get()+1) == Main.currentStorageLineSize && (Main.customersFinished < 20) ){}
            if(this.id == 0 ) {
                Main.currentStorageLineIndex.getAndIncrement();

            }
            this.isBusy.set(false);
            this.isBusy2 = false;
        }

        try {
            sleep(30000);
        } catch (InterruptedException e) {
            System.out.println("["+(System.currentTimeMillis()-time)+"] "+"Store is closing, StorageClerk "+ this.id + " has been interrupted...");
        }
    }
}
