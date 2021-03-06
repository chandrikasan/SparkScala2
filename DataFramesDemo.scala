import org.apache.spark.sql.SparkSession

object DataFramesDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().master("local").appName("Hello World").getOrCreate()
    val sc = sparkSession.sparkContext
    import sparkSession.implicits._
    /*
    val df1 = sparkSession.read.csv("C:\\student_old.csv")
    df1.show()
    df1.printSchema()
    */
    val inputRdd = sc.textFile("C:\\student_old.csv")
    val studentRdd = inputRdd.map(line =>
      line.split(",")).map(x=>Student(x(0).toInt,x(1),x(2),x(3),x(4).toInt))

    val df2 = studentRdd.toDF()
    df2.createOrReplaceTempView("student")
    df2.groupBy("subject").max("marks").show()
    val df3 = sparkSession.sql("select * from student order by marks desc" )
    df3.show()

    df3.write.bucketBy(3, "marks").sortBy("marks").saveAsTable("StudentHive")

    df3.filter($"marks" > 90).show()

    df3.groupBy("gender").max("marks").show()

    df3.filter("name like '%Preeti%'").show
    df3.filter("name = '%Preeti%'").show
    df3.filter("name = 'Preeti'").show
    df3.filter($"name" === "Preeti").show
    df3.filter($"name" =!= "Preeti").show
    df3.select(df3.col("name")).show

  }

  case class Student(id:Int,name:String,gender:String,subject:String,marks:Int)
}
