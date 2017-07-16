package scalaadaptive.core.runtime.selection.support

/**
  * Created by Petr Kubat on 7/8/17.
  */
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.SelectionStrategy
import scalaadaptive.math.TestResult
import scalaadaptive.math.TestResult.TestResult

trait TestBasedSelectionStrategy[TMeasurement] extends SelectionStrategy[TMeasurement] {
  protected val logger: Logger
  protected val secondarySelector: SelectionStrategy[TMeasurement]
  protected val runTestTwo: (RunHistory[TMeasurement], RunHistory[TMeasurement], Double) => Option[TestResult]
  protected val runTestMultiple: (RunHistory[TMeasurement], Iterable[RunHistory[TMeasurement]], Double) => Option[TestResult]
  protected val alpha: Double
  protected val name: String

  private def selectFromTwo(firstRecord: RunHistory[TMeasurement],
                            secondRecord: RunHistory[TMeasurement],
                            inputDescriptor: Option[Long]): HistoryKey = {
    val result = runTestTwo(firstRecord, secondRecord, alpha)
    result match {
      case Some(TestResult.ExpectedLower) => firstRecord.key
      case Some(TestResult.ExpectedHigher) => secondRecord.key
      case _ => secondarySelector.selectOption(List(firstRecord, secondRecord), inputDescriptor)
    }
  }

  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): HistoryKey = {
    logger.log(s"Selecting using $name")

    // Fallback to the two sample test
    if (records.size == 2)
      return selectFromTwo(records.head, records.last, inputDescriptor)

    // Applying Bonferroni correction
    val oneTestAlpha = alpha / records.size

    // Using Scala's view for lazy mapping and filtering - we need just the first result
    val positiveResults = records
      .view
      .map(elem => {
        val remaining = records
          .filter(r => r != elem)

        val result = runTestMultiple(elem, remaining, oneTestAlpha)

        (elem, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1.key)
      .getOrElse({
        logger.log(s"Multiple $name can't select, falling back to secondary selector")
        secondarySelector.selectOption(records, inputDescriptor)
      })
  }
}
