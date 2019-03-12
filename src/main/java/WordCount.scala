import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
object WordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("scalawordcount")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("C:\\Users\\Administrator\\Desktop\\Data01.txt")
    rdd.map(line => (line.split(",")(0), 1))
      .reduceByKey(_ + _)
      .collect()
      .foreach(println)
    sc.stop()
  }
}
