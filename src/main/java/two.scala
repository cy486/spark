import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.HashPartitioner

object two
{
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("text2")
    val sc = new SparkContext(conf)
    val dataFile = "C:\\Users\\Administrator\\Desktop\\data"
    val data = sc.textFile(dataFile,2)
    val res = data.filter(_.trim().length>0).map(line=>(line.trim,"\t"))
      .partitionBy(new HashPartitioner(1)).groupByKey().sortByKey().keys
    res.saveAsTextFile("result")
  }
}