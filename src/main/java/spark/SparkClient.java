package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkClient {

  private static SparkSession sparkSession;

  public static SparkSession getClient() {
    if (sparkSession == null) {
      synchronized (SparkSession.class) {
        if (sparkSession == null) {
          sparkSession = createClient();
        }
      }
    }
    return sparkSession;
  }

  private static SparkSession createClient() {
    SparkConf sparkConf = new SparkConf();
    sparkConf.setMaster("local[4]");
    sparkConf.setAppName("TEST");
    sparkConf.set("spark.scheduler.mode", "FAIR");
    sparkConf.set("spark.ui.retainedJobs", "20");
    sparkConf.set("spark.ui.retainedStages", "40");
    sparkConf.set("spark.sql.ui.retainedExecutions", "15");
    sparkSession = SparkSession
        .builder()
        .config(sparkConf)
        .enableHiveSupport()
        .getOrCreate();
    return sparkSession;
  }
}
