import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.HashPartitioner

object three {
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("text3")
    val sc = new SparkContext(conf)
    val dataFile = "C:\\Users\\Administrator\\Desktop\\data1"
    val data = sc.textFile(dataFile,3)
    val res = data.filter(_.trim().length>0)
      .map(line=>(line.split("\t")(0).trim()
        ,line.split("\t")(1).trim().toInt))
      .partitionBy(new HashPartitioner(1))
      .groupByKey().map(x => {
      var n = 0
      var sum = 0.0
      for(i <- x._2){
        sum = sum + i
        n = n +1
      }
      val avg = sum/n
      val format = f"$avg%1.2f".toDouble
      (x._1,format)
    })
    res.saveAsTextFile("result1")
  }
}
