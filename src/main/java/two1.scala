import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object two1
{
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local")
      .setAppName("two1")
    val sc = new SparkContext(conf)
    var spark = SparkSession.builder().getOrCreate()
    var df = spark.read.json("D:\\Users\\Administrator\\IdeaProjects\\WordCount\\employee.json")
    //查询所有数据；
    df.show()
    //2 查询所有数据，并去除重复的数据
    df.distinct().show()
    //(3) 查询所有数据，打印时去除 id 字段
    df.drop("id").show()
    //(4) 筛选出 age>30 的记录；
    df.filter(df("age")>30).show()
    //(5) 将数据按 age 分组；
    df.groupBy("name").count().show()
    //(6) 将数据按 name 升序排列；
    df.sort(df("name").asc).show()
    //(7) 取出前 3 行数据
    df.take(3)
    //(8) 查询所有记录的 name 列，并为其取别名为 username；
    df.select(df("name")as("username")).show()
    //(9) 查询年龄 age 的平均值；
    df.agg("age"->"avg")
    //(10) 查询年龄 age 的最小值
    df.agg("age"->"min")
  }
}