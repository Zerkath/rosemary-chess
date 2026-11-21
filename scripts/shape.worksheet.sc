//> using repository m2Local
//> using dep org.openjdk.jol:jol-core:0.17
//> using dep rosemary:rosemary-chess:0.0.1-SNAPSHOT

import org.openjdk.jol.info.{ClassLayout, GraphLayout}
import org.openjdk.jol.vm.VM
import rosemary.board.BoardState
import rosemary.types.Moves

println(VM.current().details())
println()

def classLayout(cls: Class[?]): Unit =
  println(ClassLayout.parseClass(cls).toPrintable())

def objectShape(value: Any): Unit =
  println(ClassLayout.parseInstance(value).toPrintable())
  println(GraphLayout.parseInstance(value).toFootprint())

classLayout(classOf[BoardState])
classLayout(classOf[Moves])

val startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
objectShape(new BoardState(startFen))
objectShape(new Moves("e2e4 d2d4 g1f3"))

