import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
object one {
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("text1")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("C:\\Users\\Administrator\\Desktop\\Data01.txt")
    //该系总共有多少学生；
    val par=rdd.map( row=>row.split(",")(0))
    var count=par.distinct()
    println("学生总人数："+count.count())
    //该系共开设来多少门课程；
    val couse=rdd.map( row=>row.split(",")(1))
    println("课程数："+couse.distinct().count())
   //Tom 同学的总成绩平均分是多少；
    val pare = rdd.filter(row=>row.split(",")(0)=="Tom")
    /*pare.foreach(println)*/
    pare.map(row=>(row.split(",")(0),row.split(",")(2).toInt))
      .mapValues(x=>(x,1))
      .reduceByKey((x,y) => (x._1+y._1,x._2 + y._2))
      .mapValues(x => (x._1 / x._2))
      .collect().foreach(x=>println("Tom的平均成绩："+x._2))
    //求每名同学的选修的课程门数；
    val pare2 = rdd.map(row=>(row.split(",")(0),row.split(",")(1)))
    pare2.mapValues(x => (x,1)).reduceByKey((x,y) => (" ",x._2 + y._2)).mapValues(x => x._2).foreach(println)
   //该系 DataBase 课程共有多少人选修；
    val pare3 = rdd.filter(row=>row.split(",")(1)=="DataBase")
    println("DataBase的选修人数："+pare3.count)
    // 各门课程的平均分是多少；
    val pare4 = rdd.map(row=>(row.split(",")(1),row.split(",")(2).toInt))
    pare4.mapValues(x=>(x,1))
      .reduceByKey((x,y) => (x._1+y._1,x._2 + y._2))
      .mapValues(x => (x._1/ x._2))
      .collect().foreach(println)
    //使用累加器计算共有多少人选了 DataBase 这门课。
    val pare5 = rdd.filter(row=>row.split(",")(1)=="DataBase")
      .map(row=>(row.split(",")(1),1))
    val accum = sc.longAccumulator("My Accumulator")
    pare5.values.foreach(x => accum.add(x))
    println("选了 DataBase 这门课的人数："+accum.value)
  }
}
