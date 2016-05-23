import akka.NotUsed
import akka.stream.scaladsl.{Source, Flow, GraphDSL}
import akka.util.ByteString

/**
  * Created by jay on 22/05/2016.
  */
package object streams {

	val BytesToString = Flow[ByteString].map(bytes => bytes.decodeString("UTF-8"))

}
