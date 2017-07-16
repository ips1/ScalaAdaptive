package scalaadaptive.core.runtime.selection.strategies.support

/**
  * Created by Petr Kubat on 7/8/17.
  */
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.strategies.SelectionStrategy
import scalaadaptive.math.TestResult
import scalaadaptive.math.TestResult.TestResult

/**
  * Common mixin for a mean based selection strategy based on a statistical test.
  *
  * Needs two basic functions:
  * - runTestTwo to run the statistical test between two functions
  * - runTestMultiple allowing to test one function against multiple functions
  *
  * Using these test accessors, the whole selection process is accessed from here.
  *
  * At the moment of creation, two specific test implementations exist:
  * - [[scalaadaptive.core.runtime.selection.strategies.TTestSelectionStrategy]]
  * - [[scalaadaptive.core.runtime.selection.strategies.UTestSelectionStrategy]]
  *
  * @tparam TMeasurement The type of a function run measurement, can contain data used to evaluate function runs.
  *                      A Long type is used for simple run time measurement.
  *
  */
trait TestBasedSelectionStrategy[TMeasurement] extends SelectionStrategy[TMeasurement] {
  protected val logger: Logger
  protected val secondaryStrategy: SelectionStrategy[TMeasurement]
  protected val alpha: Double
  protected val name: String

  protected val runTestTwo: (RunHistory[TMeasurement], RunHistory[TMeasurement], Double) => Option[TestResult]
  protected val runTestMultiple: (RunHistory[TMeasurement], Iterable[RunHistory[TMeasurement]], Double) => Option[TestResult]

  private def selectFromTwo(firstRecord: RunHistory[TMeasurement],
                            secondRecord: RunHistory[TMeasurement],
                            inputDescriptor: Option[Long]): HistoryKey = {
    val result = runTestTwo(firstRecord, secondRecord, alpha)
    result match {
      case Some(TestResult.ExpectedLower) => firstRecord.key
      case Some(TestResult.ExpectedHigher) => secondRecord.key
      case _ => secondaryStrategy.selectOption(List(firstRecord, secondRecord), inputDescriptor)
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

    // We test every function against all the others, waiting for an ExpectedLower result
    // Using Scala's view for lazy mapping and filtering - we need just the first result
    val positiveResults = records
      .view
      .map(elem => {
        val remaining = records
          .filter(r => r != elem)

        val result = runTestMultiple(elem, remaining, oneTestAlpha)

        (elem, result)
      })


    // The find call evaluates the view element by element, performing the tests, and stops when a value containing
    // ExpectedLower is found.
    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1.key)
      .getOrElse({
        logger.log(s"Multiple $name can't select, falling back to secondary selector")
        secondaryStrategy.selectOption(records, inputDescriptor)
      })
  }
}
