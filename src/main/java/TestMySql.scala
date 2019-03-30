import java.util.Properties
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
object TestMySql {
  def main(args: Array[String])
  {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("RDDtoDF")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.getOrCreate
    import spark.implicits._
    val employeeRDD = spark.sparkContext.parallelize(Array("3 Mary F 26","4 Tom M 23"))
    .map(_.split(" "))
    val schema = StructType(List(StructField("id", IntegerType, true),StructField("name", StringType, true)
      ,StructField("gender", StringType, true)
      ,StructField("age", IntegerType, true)))
    val rowRDD = employeeRDD.map(p => Row(p(0).toInt,p(1).trim, p(2).trim,p(3).toInt))
    val employeeDF = spark.createDataFrame(rowRDD, schema)
    val prop = new Properties()
    prop.put("user","root")
    prop.put("password","123456")
    prop.put("driver","com.mysql.jdbc.Driver")
    employeeDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/sparktest","employee",prop)
    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/sparktest")
      .option("driver","com.mysql.jdbc.Driver")
      .option("dbtable","employee")
      .option("user","root")
      .option("password","123456")
      .load()
    jdbcDF.agg("age" -> "max", "age" -> "sum").show()
  }
}