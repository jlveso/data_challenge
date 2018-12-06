package packer

import java.util.concurrent.atomic.AtomicInteger

import com.google.gson.{Gson, GsonBuilder}
import packer.Box.ForJson

import scala.collection.mutable.ArrayBuffer

class Box(val id:Int, _weight:Double) extends Comparable[Box] {

  var weight:String = _weight.toString + " pounds"
  val contentArray:ArrayBuffer[Book] = new ArrayBuffer[Book]()

  def weightToDouble: Double = weight.replace(" pounds", "").toDouble
  override def compareTo(o: Box): Int = weightToDouble.compareTo(o.weightToDouble)

  def +=(book: Book): Boolean ={
    if (weightToDouble + book.weightToDouble > Box.maxWeight){
      false
    }else{
      contentArray += book
      addWeight(book.weightToDouble)
      true
    }
  }

  def addWeight(weight: Double): Unit ={
    val newWeight = weightToDouble + weight
    this.weight = newWeight + " pounds"
  }

  def toJson:String={
    val forJson = ForJson(id, weight, contentArray.toArray)
    Box.gson.toJson(forJson)
  }


}

object Box{
  val gson: Gson = new GsonBuilder().setPrettyPrinting().create()
  val idFactory: AtomicInteger = new AtomicInteger(0)
  val maxWeight:Double = 10.0
  val initialWeight:Double = 0.3
  def makeBox: Box = new Box(idFactory.getAndIncrement(), initialWeight)

  private case class ForJson(id:Int, weight:String, content:Array[Book])

}
