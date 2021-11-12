import com.mixer.dbserver.DB;
import com.mixer.dbserver.DBServer;
import com.mixer.raw.Index;
import com.mixer.raw.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DBBasicTests {
    private String dbFileName = "testdb.db";


    @Test
    public void testAdd() {
        try(DB db = new DBServer(dbFileName)) {
            Person p = new Person("Jonh", 44, "Berlin", "www-404", "This is a description");
            db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void testRead() {
        try(DB db = new DBServer(dbFileName)) {
            Person p = new Person("John", 44, "Berlin", "www-404", "This is a description");
            db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
            Person person = db.read(0);
            Assert.assertTrue(person.name.equals("John"));
            Assert.assertTrue(person.address.equals("Berlin"));
            Assert.assertTrue(person.carPlateNumber.equals("www-404"));
            Assert.assertTrue(person.description.equals("This is a description"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testDelete() {
        try(DB db = new DBServer(dbFileName)) {
            Person p = new Person("Jonh", 44, "Berlin", "www-404", "This is a description");

            db.add(p);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 1);
            db.delete(0);
            Assert.assertEquals(Index.getInstance().getTotalRowNumber(), 0);
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void updateByName() {
        try(DB db = new DBServer(dbFileName)) {
            Person p = new Person("John", 44, "Berlin", "www-404", "This is a description");

            db.add(p);
            Person p2 = new Person("John2", 44, "Berlin", "www-404", "This is a description");
            db.update("John", p2);

            Person result = db.read(0);
            Assert.assertEquals(result.name,"John2");
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void updateByRowNumber() {
        try(DB db = new DBServer(dbFileName)) {
            Person p = new Person("John", 44, "Berlin", "www-404", "This is a description");

            db.add(p);
            Person p2 = new Person("John2", 44, "Berlin", "www-404", "This is a description");
            db.update(0, p2);

            Person result = db.read(0);
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

    }
}
