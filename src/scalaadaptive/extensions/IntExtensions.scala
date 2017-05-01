package scalaadaptive.extensions

/**
  * Created by pk250187 on 4/25/17.
  */

object IntExtensions {

  implicit class IntWithExtensions(val i: Int) {
    def times[TReturn](fun: () => TReturn): Seq[TReturn] = Seq.range(0, i).map(_ => fun())
  }

}
