import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
object RDDtoDF {
  case class Employee(id:Long,name:String,age:Long)
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("RDDtoDF")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.getOrCreate
    import spark.implicits._
    val employeeDF = sc.textFile("D:\\Users\\Administrator\\IdeaProjects\\WordCount\\employee.txt")
      .map(_.split(","))
      .map(attributes => Employee(attributes(0).trim.toInt, attributes(1), attributes(2).trim.toInt)).toDF()
    employeeDF.createOrReplaceTempView("employee")
    var employeeRDD = spark.sql("select id,name,age from employee")
    employeeRDD.map(t=>"id:"+t(0)+"name:"+t(1)+"age:"+t(2)).show()
  }
}
