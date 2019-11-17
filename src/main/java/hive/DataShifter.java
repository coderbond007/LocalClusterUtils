package hive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import spark.SparkClient;

public class DataShifter {

  private static final String TABLE_SRC = "`default`.`page_view_csv`";
  private static final String TABLE_DEST = "`web_log`.`page_view`";
  private static final int LIMIT = 5;
  private static SparkSession sparkSession;

  public static void main(String[] args) {
    sparkSession = SparkClient.getClient();
//    deleteData(sparkSession);
    Dataset<Row> datasetSrc = sparkSession.table(TABLE_SRC);
//    datasetSrc = datasetSrc.withColumn("label", lit(1));
//    Map<String, String> aggFunc = new HashMap<String, String>();
//    aggFunc.put("label", "sum");
//    datasetSrc = datasetSrc.groupBy("ts").agg(aggFunc);
    datasetSrc.show();
//    datasetSrc.write().insertInto(TABLE_DEST);
//    StructType targetTableStructType = getModifiedStruct(datasetSrc);
//    PrintWriter out = new PrintWriter(new FileOutputStream(new File(
//        "/Users/pradyumn.ag/IdeaProjects/LocalClusterUtils/data/hive/queries/create_query_without_array.sql")));

//    out.println("INSERT INTO " + TABLE_DEST);
//    out.println("(SELECT ");
//    int colSize = targetTableStructType.size();
//    for (int i = 0; i < colSize; ++i) {
//      out.print(targetTableStructType.fieldNames()[i]);
//      if (i < colSize - 1) {
//        out.print(",");
//      }
//      out.println();
//    }
//    out.flush();
//    out.println("FROM ");
//    out.println(TABLE_SRC);
//    out.println(")");
//    out.close();

//    Dataset<Row> finDataset = sparkSession.table(TABLE_DEST);
//    finDataset.show(LIMIT);
//    datasetSrc.write().insertInto(TABLE_DEST);

//    SparkClient.getClient().sql("TRUNCATE TABLE " + TABLE_DEST);
//    datasetTarget.show(LIMIT);
  }

  private static void deleteData(SparkSession sparkSession) {
    sparkSession.sql("TRUNCATE TABLE " + TABLE_DEST);
  }

  private static List<Column> getModifiedColumnList(Dataset<Row> dataset) {
    StructType targetTableStructType = getModifiedStruct(dataset);
    int colSize = targetTableStructType.size();
    List<Column> columns = new ArrayList<Column>();
    for (int i = 0; i < colSize; ++i) {
      columns.add(dataset.col(targetTableStructType.fieldNames()[i]));
    }
    return columns;
  }

  public static Seq<Column> convertListToSeq(List<Column> inputList) {
    return JavaConverters.asScalaIteratorConverter(inputList.iterator()).asScala().toSeq();
  }

  private static List<String> convertToList(String[] cols) {
    List<String> colsList = new ArrayList<String>(cols.length);
    for (String col : cols) {
      colsList.add(col);
    }
    return colsList;
  }

  private static StructType getModifiedStruct(Dataset<Row> dataset) {
    StructType structType = dataset.schema();
    StructField[] structFields = new StructField[structType.fields().length];
    int len = structFields.length;
    int ptr = 0;
    for (int i = 1; i < len; ++i) {
      structFields[ptr++] = structType.fields()[i];
    }
    structFields[ptr++] = structType.fields()[0];
    structFields = Arrays.copyOf(structFields, ptr);
    StructType structType1 = new StructType(structFields);
    return structType1;
  }
}
