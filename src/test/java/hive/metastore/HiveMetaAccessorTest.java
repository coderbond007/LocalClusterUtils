package hive.metastore;

import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;

public class HiveMetaAccessorTest {

  @Test
  public void getDatabases() throws TException {
    Assert.assertNotNull(HiveMetaAccessor.getDatabases());
  }
}