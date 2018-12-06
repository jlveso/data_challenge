package packer

import com.google.gson.{Gson, GsonBuilder}
import org.apache.spark.sql.Row

class Book(val title:String,
           val author:String,
           val weight:String,
           val price:String,
           val isbn_10:String) extends Comparable[Book]{

  print(toJson)
  override def compareTo(o: Book): Int = weightToDouble.compareTo(o.weightToDouble)
  def weightToDouble: Double = weight.replace(" pounds", "").toDouble
  def toJson: String = Book.gson.toJson(this)

}

object Book{
  val gson:Gson = new GsonBuilder().setPrettyPrinting().create();

  def buildFromRow(row:Row): Book ={
    val data: Array[Any] = row.toSeq.toArray
    val author = data(0).toString
    val title = data(1).toString
    val isbn10 = data(2).toString
    val weight = data(4).toString + " pounds"
    val price = "$" + data(3).toString
    new Book(title = title, author = author, weight = weight, isbn_10 = isbn10, price = price)
  }
}
