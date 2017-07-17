package scalaadaptive.core.functions

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy

/**
  * Created by Petr Kubat on 5/8/17.
  *
  * A configuration of a [[scalaadaptive.core.functions.CombinedFunction]] (and therefore transitively the adaptive
  * function that it is wrapped in).
  *
  * An immutable class with methods that return new instances rather than modifying the old one.
  *
  * @param selection         The [[scalaadaptive.api.options.Selection.Selection]], determines, which strategy will be used
  *                          (see [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]]).
  * @param storage           The [[scalaadaptive.api.options.Storage.Storage]], determines, which history storage will be used
  *                          (see [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]]).
  * @param duration          An optional maximum age of a function history record to be used in the selection process.
  * @param closureIdentifier True if the closure identifiers
  *                          ([[scalaadaptive.core.functions.identifiers.ClosureIdentifier]]) should be preferred over
  *                          special identifiers ([[scalaadaptive.core.functions.identifiers.MethodNameIdentifier]] and
  *                          [[scalaadaptive.core.functions.identifiers.CustomIdentifier]]).
  * @param startPolicy The initial [[scalaadaptive.api.policies.Policy]] of the function.
  *
  */
class FunctionConfig(val selection: Option[Selection],
                     val storage: Storage,
                     val duration: Option[Duration],
                     val closureIdentifier: Boolean,
                     val startPolicy: Policy) {
  /** Creates a new [[scalaadaptive.core.functions.FunctionConfig]] with changed selection to the value used */
  def selectUsing(newSelection: Selection) = new FunctionConfig(Some(newSelection), storage, duration, closureIdentifier, startPolicy)
  /** Creates a new [[scalaadaptive.core.functions.FunctionConfig]] with changed storage to the value used */
  def storeUsing(newStorage: Storage) = new FunctionConfig(selection, newStorage, duration, closureIdentifier, startPolicy)
  /** Creates a new [[scalaadaptive.core.functions.FunctionConfig]] with changed duration to the value used */
  def limitedTo(newDuration: Duration) = new FunctionConfig(selection, storage, Some(newDuration), closureIdentifier, startPolicy)
  /** Creates a new [[scalaadaptive.core.functions.FunctionConfig]] with changed asClosures to the value used */
  def asClosures(newClosureIdentifier: Boolean) = new FunctionConfig(selection, storage, duration, newClosureIdentifier, startPolicy)
  /** Creates a new [[scalaadaptive.core.functions.FunctionConfig]] with changed initial policy to the value used */
  def withPolicy(newPolicy: Policy) = new FunctionConfig(selection, storage, duration, closureIdentifier, newPolicy)
}