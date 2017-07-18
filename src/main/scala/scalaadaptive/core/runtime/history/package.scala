package scalaadaptive.core.runtime

/**
  * Created by Petr Kubat on 7/19/17.
  *
  * Package containing types specific to the run history of invoked functions.
  *
  * The history of a function is represented by [[scalaadaptive.core.runtime.history.runhistory.RunHistory]]. All
  * the histories are stored in [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]]. In case of
  * persistent history, the [[scalaadaptive.core.runtime.history.serialization.HistorySerializer]] deals with the
  * I/O operations.
  *
  */
package object history {

}
