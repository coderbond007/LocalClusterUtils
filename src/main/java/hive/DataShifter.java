package hive;

import static org.apache.spark.sql.functions.lit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.thrift.TException;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import spark.SparkClient;

public class DataShifter {

  private static final String TABLE_SRC = "`default`.`page_view_csv`";
  private static final String TABLE_DEST = "`web_log`.`page_view`";
  private static final int LIMIT = 5;
  private static SparkSession sparkSession;

  public static void main(String[] args)
      throws InterruptedException, AnalysisException, TException {
    sparkSession = SparkClient.getClient();
//    deleteData(sparkSession);
    Dataset<Row> datasetSrc = sparkSession.table(TABLE_DEST);
    datasetSrc = datasetSrc.withColumn("label", lit(1));
    Map<String, String> aggFunc = new HashMap<String, String>();
    aggFunc.put("label", "sum");
    datasetSrc = datasetSrc.groupBy("ts").agg(aggFunc);
    datasetSrc.show();
    Thread.sleep(1000_000);
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
    StructType structType2 = structType1;
    return structType2;
  }
}
