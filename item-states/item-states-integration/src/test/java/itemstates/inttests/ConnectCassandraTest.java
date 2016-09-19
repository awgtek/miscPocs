package itemstates.inttests;

import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ConnectCassandraTest {

	private Session session;

	@Test
	public void testConnecting() {
		Cluster cluster = Cluster.builder().addContactPoint("192.168.1.102").build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(),
					host.getRack());
		}
		session = cluster.connect();
		runCreateDelete();
		cluster.close();

	}

	private void runCreateDelete() {
		session.execute("CREATE KEYSPACE IF NOT EXISTS mytempks WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':1};");
		session.execute("CREATE TABLE IF NOT EXISTS mytempks.mytemptable (id int, name text, "
				+ "description text, PRIMARY KEY (id))");
		session.execute("CREATE INDEX IF NOT EXISTS nameIndex on mytempks.mytemptable (name)");
		session.execute("USE mytempks");
		session.execute("INSERT INTO mytemptable (id, name, description) VALUES (1, "
				+ "'a name','a description') IF NOT EXISTS");
		ResultSet results = session
				.execute("SELECT id, name, description FROM mytemptable WHERE name='a name' AND id = 1");
		for (Row row : results) {
			System.out.println(row.getString("name") + row.getString("description"));
			String nameAndDescription =row.getString("name") + row.getString("description");
			assertThat(nameAndDescription, equalTo("a namea description"));
		}
		session.execute("DELETE FROM mytemptable WHERE id=1");
		session.execute("DROP INDEX IF EXISTS nameIndex");
		session.execute("DROP TABLE IF EXISTS mytemptable");
		session.execute("DROP KEYSPACE IF EXISTS mytempks");

	}

}
