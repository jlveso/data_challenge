package packer

import java.io.PrintWriter

import org.apache.spark.sql._

import scala.collection.mutable

class Packer(dataSource: String, n_boxes:Int, targetSource:String) {

  val spark: SparkSession = SparkSession.builder()
    .config("spark.master", "local")
    .getOrCreate()

  val df: Dataset[Row] = spark
    .read.option("header", "true")
    .option("inferSchema", "true")
    .option("delimiter", ";")
    .csv(dataSource).drop("_c5")

  df.show()

  val rowArray: Array[Row] = df.head(20)
  val bookArray: Array[Book] = (for (row <- rowArray)  yield Book.buildFromRow(row)).sorted
  val boxes:List[Box] = List.fill(n_boxes)(Box.makeBox)
  val bookStack: mutable.ArrayStack[Book] = new mutable.ArrayStack[Book]()

  for (book <- bookArray) yield bookStack.push(book)

  var loops = 0
  val max_loops:Int = n_boxes * bookArray.length

  while (bookStack.nonEmpty && loops < max_loops) {
    for (box <- boxes ){
      try {
        val book = bookStack.pop()
        if (!(box+=book)) {
          bookStack.push(book)
        }
      } catch {
        case e: Exception => print("")
      }
    }
    loops += 1
  }

  val jsonBuilder = new mutable.StringBuilder()
  jsonBuilder.append("[")
  val jsons:List[String] = for (box <- boxes) yield box.toJson
  for (json <- jsons){
    jsonBuilder.append(json)
    if (json != jsons.last){
      jsonBuilder.append(",")
    }
  }
  jsonBuilder.append("]")
  val jsonString:String = jsonBuilder.toString()

  new PrintWriter(targetSource) { write(jsonString); close }

}

object Packer {
  def apply(dataSource: String, n_boxes: Int, targetSource: String): Packer = new Packer(dataSource, n_boxes, targetSource)
}
