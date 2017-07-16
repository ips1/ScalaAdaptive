package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 7/7/17.
  *
  * Default mixin implementation of takeWhile and filter methods. It requires from the target class to implement
  * the makeHistoryFromFilteredItems, which basically initializes a new corresponding instance with given data.
  */
trait DefaultFilterRunHistory[TMeasurement] extends RunHistory[TMeasurement] {
  protected def makeHistoryFromFilteredItems(items: Iterable[EvaluationData[TMeasurement]]): RunHistory[TMeasurement]

  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = {
    val filteredItems = runItems.takeWhile(filter)
    makeHistoryFromFilteredItems(filteredItems)
  }

  override def filter(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = {
    val filteredItems = runItems.filter(filter)
    makeHistoryFromFilteredItems(filteredItems)
  }
}
