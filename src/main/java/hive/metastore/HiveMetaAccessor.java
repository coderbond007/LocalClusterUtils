package hive.metastore;

import java.util.List;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveMetaAccessor {

  private static final Logger logger = LoggerFactory.getLogger(HiveMetaAccessor.class);
  private static IMetaStoreClient hiveMetaStoreClient = null;

  public static List<String> getDatabases() throws TException {
    if (hiveMetaStoreClient == null) {
      createClient();
    }
    return hiveMetaStoreClient.getAllDatabases();
  }

  private static void createClient() throws MetaException {
    if (hiveMetaStoreClient == null) {
      hiveMetaStoreClient = new HiveMetaStoreClient(new HiveConf());
      hiveMetaStoreClient = HiveMetaStoreClient.newSynchronizedClient(hiveMetaStoreClient);
    }
  }
}
