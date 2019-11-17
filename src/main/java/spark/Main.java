package spark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.parser.ParseException;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;

public class Main {

  private static final String QUERY = "SELECT abtest_bucket_id.value AS `dimension467`, Coalesce(Concat(bucket_bucket_id.`value`, \"- [\", `bucket_id`, \"]\"), `bucket_id`) AS `dimension527`, Coalesce(Concat(device_device_id.`value`, \"- [\", `device_id`, \"]\"), `device_id` ) AS `dimension586`, Sum(CASE WHEN(( `is_container` NOT IN ( '0' ) OR Isnull(`is_container`) )) THEN Coalesce(`views`, 0) ELSE NULL end) `filteredmetric1001758` FROM web_log.page_view LEFT JOIN analytics_master.abtest AS abtest_bucket_id ON abtest_bucket_id.key = `bucket_id` LEFT JOIN analytics_master.device AS device_device_id ON device_device_id.key = `device_id` LEFT JOIN analytics_master.bucket AS bucket_bucket_id ON bucket_bucket_id.key = `bucket_id` WHERE (( abtest_bucket_id.value IN ( '74204 : E2_SERP PROMPT', '74216 : E2_SERP Prompt_Batch 2', '74218 : E2_PushPrompt_Batch 3', '74220 : Premium_Serp Prompt_US', '74156 : E1_US SERP Prompt' ) )) GROUP BY Coalesce(Concat(bucket_bucket_id.`value`, \"- [\", `bucket_id`, \"]\"), `bucket_id` ), abtest_bucket_id.`value`, Coalesce(Concat(device_device_id.`value`, \"- [\", `device_id`, \"]\"), `device_id` ) ";
  private static final String DASH_LINE = "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

  public static void main(String[] args) throws ParseException, FileNotFoundException {
    SparkSession sparkSession = SparkClient.getClient();
//    SessionState sessionState = sparkSession.sessionState();
//    LogicalPlan logicalPlan = sessionState.sqlParser().parsePlan(QUERY);
//    QueryExecution queryExecution = sessionState.executePlan(logicalPlan);
//    queryExecution.assertSupported();
//    queryExecution.assertAnalyzed();

//    LogicalPlan optimizedPlan = queryExecution.optimizedPlan();
//    SparkPlan sparkPlan = queryExecution.executedPlan();
//    SparkPlan sparkPlan1 = queryExecution.sparkPlan();
//    System.out.println(sparkPlan1.treeString());
//    doWithOptiPlan(optimizedPlan);
    Dataset<Row> dataset = sparkSession.sql("SHOW CREATE TABLE `default`.`page_view_csv`");
    List<Row> rows = dataset.collectAsList();
    PrintWriter out = new PrintWriter(new FileOutputStream(new File(
        "/Users/pradyumn.ag/IdeaProjects/LocalClusterUtils/data/hive/queries/create_query_without_array.sql")));
    out.println(rows.get(0));
    out.flush();
  }

  private static void doWithOptiPlan(LogicalPlan optimizedPlan) {
    System.out.println(optimizedPlan.treeString());
    System.out.println(DASH_LINE);
  }

}
