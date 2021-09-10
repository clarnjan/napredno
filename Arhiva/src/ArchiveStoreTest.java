import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде
abstract class Archive{
    int id;
    Date dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }
}

class LockedArchive extends Archive{
    Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen=dateToOpen;
    }
}

class SpecialArchive extends Archive{
    int maxOpen;
    int numOpened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        numOpened=0;
    }
}

class ArchiveStore {
    List<Archive> archiveList;
    String log;
    SimpleDateFormat formatter;

    public ArchiveStore() {
        archiveList=new ArrayList<>();
        log="";
        formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    public void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archiveList.add(item);
        addToLog(String.format("Item %d archived at %s",item.getId(),formatter.format(date)));

    }
    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive a=null;
        if(archiveList.stream().anyMatch(x->x.getId()==id)) {
            a = archiveList.stream().filter(x -> x.getId() == id).findFirst().get();
            if(a.getClass()==LockedArchive.class){
                LockedArchive a1=(LockedArchive) a;
                if(a1.dateToOpen.after(date))
                    addToLog(String.format("Item %d cannot be opened before %s", id, formatter.format(a1.dateToOpen)));
                else
                    addToLog(String.format("Item %d opened at %s", id, formatter.format(date)));
            }
            else if(a.getClass()==SpecialArchive.class){
                SpecialArchive a1=(SpecialArchive) a;
                if(a1.numOpened>=a1.maxOpen)
                    addToLog(String.format("Item %d cannot be opened more than %d times", id, a1.maxOpen ));
                else {
                    addToLog(String.format("Item %d opened at %s", id, formatter.format(date)));
                    a1.numOpened++;
                }
            }
        }
        else
            throw new NonExistingItemException(id);
    }
    public String getLog(){
        return log;
    }
    public void addToLog(String str){
        if(!log.equals(""))
            log+="\n";
        log+=str;
    }
}

class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist",id));
    }
}
