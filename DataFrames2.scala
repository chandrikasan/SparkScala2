import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object DataFrames2 {
  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder().master("local").appName("Hello World").getOrCreate()
    val sc = sparkSession.sparkContext

    val inputRDD = sc.textFile("C:\\student_old.csv")
    //val schemaString = "id,name,gender,subject,marks"
    //val schema = StructType(schemaString.split(",").map(fieldName => StructField(fieldName, StringType, true)))

    val schema = StructType ( Array(StructField( "id" , IntegerType, nullable = true),
      StructField("name", StringType, nullable = true),
      StructField("gender",StringType,nullable = true),
      StructField("subject", StringType,nullable = true),
      StructField("marks", IntegerType, nullable = true)))

    val studentRowRdd = inputRDD.map(x => x.split(",")).map(x => Row.fromSeq(Seq(x(0).toInt, x(1),x(2),x(3),x(4).toInt)))

    val studentDF = sparkSession.createDataFrame(studentRowRdd, schema)
    studentDF.show()

    //studentDF.groupBy("gender").max("marks").show()
    studentDF.createOrReplaceGlobalTempView("student")

    sparkSession.sql("select * from global_temp.student").show()
  }


}
