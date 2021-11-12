import com.mixer.dbserver.DB;
import com.mixer.dbserver.DBServer;
import com.mixer.raw.Index;
import com.mixer.raw.Person;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DBBasicTests {
    private DB db;
    private String dbFileName = "testdb.db";


    @Test
    public void testAdd() {
        try {
            Person p = new Person("Jonh", 44, "Berlin", "www-404", "This is a description");
            this.db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void testRead() {
        try {
            Person p = new Person("Jonh", 44, "Berlin", "www-404", "This is a description");
            this.db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
            Person person = this.db.read(0);
            Assert.assertEquals(person.name,"");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testDelete() {
        try {
            Person p = new Person("Jonh", 44, "Berlin", "www-404", "This is a description");

            this.db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
            this.db.delete(0);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 0);
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void updateByName() {
        try {
            Person p = new Person("John", 44, "Berlin", "www-404", "This is a description");

            this.db.add(p);
            Person p2 = new Person("John2", 44, "Berlin", "www-404", "This is a description");
            this.db.update("John", p2);

            Person result = this.db.read(0);
            Assert.assertEquals(result.name,"John2");
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void updateByRowNumber() {
        try {
            Person p = new Person("John", 44, "Berlin", "www-404", "This is a description");

            this.db.add(p);
            Person p2 = new Person("John2", 44, "Berlin", "www-404", "This is a description");
            this.db.update(0, p2);

            Person result = this.db.read(0);
            Assert.assertEquals(result.name,"John2");
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Before
    public void setup() throws IOException {
        File file = new File(dbFileName);
        if (file.exists()) {
            file.delete();
        }
        this.db = new DBServer(dbFileName);
    }

    @After
    public void after() {
        try {
            this.db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
